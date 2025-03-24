delete from data_sync_config where table_name in ('PERSON', 'PARTICIPATION');

IF
NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PERSON')
BEGIN

INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                            query_with_pagination)
VALUES
    ('PERSON', 'NBS_ODSE',
    'SELECT *
FROM PERSON
WHERE last_chg_time :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM PERSON
    WHERE last_chg_time :operator :timestamp;',
    'WITH NumberedResults AS (
        SELECT person_uid,
               ROW_NUMBER() OVER (ORDER BY last_chg_time ASC, person_uid) AS RowNum
        FROM PERSON
        WHERE last_chg_time :operator :timestamp
    )
    SELECT *
    FROM PERSON
    WHERE person_uid IN (
        SELECT person_uid
        FROM NumberedResults
        WHERE RowNum BETWEEN :startRow AND :endRow
    );');

END;




IF
NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PARTICIPATION')
BEGIN

INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                            query_with_pagination)
VALUES
    ('PARTICIPATION', 'NBS_ODSE',
    'SELECT DISTINCT PARTICIPATION.*
FROM PARTICIPATION
INNER JOIN ACT
 ON ACT.act_uid = PARTICIPATION.act_uid
INNER JOIN OBSERVATION
 ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
INNER JOIN ENTITY
 ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
LEFT JOIN ROLE
 ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
 AND ROLE.role_seq = PARTICIPATION.role_seq
 AND ROLE.cd = PARTICIPATION.cd
WHERE (OBSERVATION.add_time :operator :timestamp
    OR OBSERVATION.last_chg_time :operator :timestamp);',
    NULL,
    'SELECT COUNT(*) FROM (
SELECT DISTINCT PARTICIPATION.subject_entity_uid, PARTICIPATION.act_uid, PARTICIPATION.type_cd
FROM PARTICIPATION
INNER JOIN ACT
    ON ACT.act_uid = PARTICIPATION.act_uid
INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
INNER JOIN ENTITY
    ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
LEFT JOIN ROLE
    ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
    AND ROLE.role_seq = PARTICIPATION.role_seq
    AND ROLE.cd = PARTICIPATION.cd
WHERE (OBSERVATION.add_time :operator :timestamp
       OR OBSERVATION.last_chg_time :operator :timestamp)
) AS PARTICIPATION_COUNT;',
    'WITH PaginatedResults AS (
        SELECT DISTINCT PARTICIPATION.*,
               COALESCE(OBSERVATION.add_time, OBSERVATION.last_chg_time) AS latest_timestamp
        FROM PARTICIPATION
        INNER JOIN ACT
            ON ACT.act_uid = PARTICIPATION.act_uid
        INNER JOIN OBSERVATION
            ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
        INNER JOIN ENTITY
            ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
        LEFT JOIN ROLE
            ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
            AND ROLE.role_seq = PARTICIPATION.role_seq
            AND ROLE.cd = PARTICIPATION.cd
        WHERE (OBSERVATION.add_time :operator :timestamp
               OR OBSERVATION.last_chg_time :operator :timestamp)
    ),
    NumberedResults AS (
        SELECT *,
               ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, subject_entity_uid, act_uid, type_cd) AS RowNum
        FROM PaginatedResults
    )
    SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

END;
