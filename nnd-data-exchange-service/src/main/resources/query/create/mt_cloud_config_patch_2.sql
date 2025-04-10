delete from data_sync_config where table_name in ('ENTITY', 'ACT', 'OBSERVATION');

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ENTITY')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('ENTITY', 'NBS_ODSE',
             'WITH EntityResults AS (
                SELECT DISTINCT ENTITY.entity_uid
                FROM ENTITY
                INNER JOIN PARTICIPATION
                ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
                INNER JOIN OBSERVATION
                ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                )
                SELECT ENTITY.*
                FROM ENTITY
                INNER JOIN EntityResults
                ON ENTITY.entity_uid = EntityResults.entity_uid;',
             NULL,
             'SELECT COUNT(*) FROM (
                SELECT DISTINCT ENTITY.entity_uid
                FROM ENTITY
                INNER JOIN PARTICIPATION
                ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
                INNER JOIN OBSERVATION
                ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
            ) AS ENTITY_COUNT;',
             'WITH PaginatedResults AS (
            SELECT DISTINCT ENTITY.entity_uid,
                   MAX(CASE WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time ELSE OBSERVATION.last_chg_time END) AS latest_timestamp
            FROM ENTITY
            INNER JOIN PARTICIPATION
            ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
            INNER JOIN OBSERVATION
            ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
            WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
            GROUP BY ENTITY.entity_uid
            ),
            NumberedResults AS (
                SELECT entity_uid, ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, entity_uid) AS RowNum
                FROM PaginatedResults
            )
            SELECT ENTITY.*
            FROM ENTITY
            INNER JOIN NumberedResults
            ON ENTITY.entity_uid = NumberedResults.entity_uid
            WHERE NumberedResults.RowNum BETWEEN :startRow AND :endRow;');

    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ACT')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('ACT', 'NBS_ODSE', 'WITH ActResults AS (
SELECT DISTINCT ACT.act_uid
FROM ACT
INNER JOIN OBSERVATION
ON ACT.act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

UNION

SELECT DISTINCT ACT.act_uid
FROM ACT
INNER JOIN PARTICIPATION
ON ACT.act_uid = PARTICIPATION.act_uid
INNER JOIN OBSERVATION
ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

UNION

SELECT DISTINCT ACT.act_uid
FROM ACT
INNER JOIN Act_relationship
ON Act.act_uid = Act_relationship.source_act_uid
INNER JOIN OBSERVATION
ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

UNION

SELECT DISTINCT ACT.act_uid
FROM ACT
INNER JOIN Act_relationship
ON Act.act_uid = Act_relationship.target_act_uid
INNER JOIN OBSERVATION
ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
)
SELECT ACT.*
FROM ACT
INNER JOIN ActResults
ON ACT.act_uid = ActResults.act_uid;',
             NULL,
             'SELECT COUNT(*) FROM (
SELECT DISTINCT ACT.act_uid
FROM ACT
INNER JOIN OBSERVATION
ON ACT.act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

UNION

SELECT DISTINCT ACT.act_uid
FROM ACT
INNER JOIN PARTICIPATION
ON ACT.act_uid = PARTICIPATION.act_uid
INNER JOIN OBSERVATION
ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

UNION

SELECT DISTINCT ACT.act_uid
FROM ACT
INNER JOIN Act_relationship
ON Act.act_uid = Act_relationship.source_act_uid
INNER JOIN OBSERVATION
ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

UNION

SELECT DISTINCT ACT.act_uid
FROM ACT
INNER JOIN Act_relationship
ON Act.act_uid = Act_relationship.target_act_uid
INNER JOIN OBSERVATION
ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)' +
             ') AS ACT_COUNT;',
'WITH ActResults AS (
SELECT DISTINCT ACT.act_uid,
   MAX(CASE
       WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
       ELSE OBSERVATION.last_chg_time
   END) AS latest_timestamp
FROM ACT
INNER JOIN OBSERVATION
ON ACT.act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
GROUP BY ACT.act_uid

UNION

SELECT DISTINCT ACT.act_uid,
   MAX(CASE
       WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
       ELSE OBSERVATION.last_chg_time
   END) AS latest_timestamp
FROM ACT
INNER JOIN PARTICIPATION
ON ACT.act_uid = PARTICIPATION.act_uid
INNER JOIN OBSERVATION
ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
GROUP BY ACT.act_uid

UNION

SELECT DISTINCT ACT.act_uid,
   MAX(CASE
       WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
       ELSE OBSERVATION.last_chg_time
   END) AS latest_timestamp
FROM ACT
INNER JOIN Act_relationship
ON Act.act_uid = Act_relationship.source_act_uid
INNER JOIN OBSERVATION
ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
GROUP BY ACT.act_uid

UNION

SELECT DISTINCT ACT.act_uid,
   MAX(CASE
       WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
       ELSE OBSERVATION.last_chg_time
   END) AS latest_timestamp
FROM ACT
INNER JOIN Act_relationship
ON Act.act_uid = Act_relationship.target_act_uid
INNER JOIN OBSERVATION
ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
GROUP BY ACT.act_uid
),
NumberedResults AS (
SELECT act_uid, ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, act_uid) AS RowNum
FROM ActResults
)
SELECT ACT.*
FROM ACT
INNER JOIN NumberedResults
ON ACT.act_uid = NumberedResults.act_uid
WHERE NumberedResults.RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'OBSERVATION')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('OBSERVATION',
             'NBS_ODSE',
             'WITH ObservationResults AS (
SELECT DISTINCT OBS_DOMAIN.observation_uid
FROM OBSERVATION as OBS_DOMAIN
INNER JOIN OBSERVATION as OBS_MAIN
ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
)
SELECT OBSERVATION.*
FROM OBSERVATION
INNER JOIN ObservationResults
ON OBSERVATION.observation_uid = ObservationResults.observation_uid;',
             NULL,
             'SELECT COUNT(*) FROM (
SELECT DISTINCT OBS_DOMAIN.observation_uid
FROM OBSERVATION as OBS_DOMAIN
INNER JOIN OBSERVATION as OBS_MAIN
ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
) AS OBSERVATION_COUNT;
',
             'WITH ObservationResults AS (
SELECT DISTINCT OBS_DOMAIN.observation_uid,
   MAX(CASE
       WHEN OBS_MAIN.add_time >= OBS_MAIN.last_chg_time THEN OBS_MAIN.add_time
       ELSE OBS_MAIN.last_chg_time
   END) AS latest_timestamp
FROM OBSERVATION as OBS_DOMAIN
INNER JOIN OBSERVATION as OBS_MAIN
ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
GROUP BY OBS_DOMAIN.observation_uid
),
NumberedResults AS (
SELECT observation_uid, ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, observation_uid) AS RowNum
FROM ObservationResults
)
SELECT OBSERVATION.*
FROM OBSERVATION
INNER JOIN NumberedResults
ON OBSERVATION.observation_uid = NumberedResults.observation_uid
WHERE NumberedResults.RowNum BETWEEN :startRow AND :endRow;');

    END;

