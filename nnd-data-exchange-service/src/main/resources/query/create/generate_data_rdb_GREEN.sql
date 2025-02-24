DELETE FROM [dbo].[data_sync_config]
WHERE table_name IN (
                     'CONDITION',
                     'CONFIRMATION_METHOD',
                     'Rdb_Date',
                     'NRT_OBSERVATION',
                     'NRT_OBSERVATION_CODED',
                     'CONDITION_CODE',
                     'Program_area_code',
                     'JURISDICTION_CODE',
                     'COVID_CASE_DATAMART',
                     'COVID_LAB_DATAMART',
                     'COVID_LAB_CELR_DATAMART',
                     'ENTITY',
                     'ROLE',
                     'PERSON',
                     'ACT',
                     'OBSERVATION',
                     'PARTICIPATION',
                     'Act_relationship'
    );

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'CONDITION')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('CONDITION', 'RDB',
             'SELECT * FROM CONDITION;',
             NULL,
             'SELECT COUNT(*) FROM CONDITION;',
             'WITH PaginatedResults AS (
                 SELECT DISTINCT *
                 FROM CONDITION
             ),
             NumberedResults AS (
                 SELECT *, ROW_NUMBER() OVER (ORDER BY CONDITION_KEY) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'CONFIRMATION_METHOD')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('CONFIRMATION_METHOD', 'RDB',

             'SELECT DISTINCT CM.*
              FROM CONFIRMATION_METHOD CM
              INNER JOIN CONFIRMATION_METHOD_GROUP CMG
                  ON CMG.CONFIRMATION_METHOD_KEY = CM.CONFIRMATION_METHOD_KEY
              INNER JOIN INVESTIGATION INV
                  ON CMG.INVESTIGATION_KEY = INV.INVESTIGATION_KEY
              WHERE INV.LAST_CHG_TIME :operator :timestamp;',

             'SELECT DISTINCT CM.*
              FROM CONFIRMATION_METHOD CM
              INNER JOIN CONFIRMATION_METHOD_GROUP CMG
                  ON CMG.CONFIRMATION_METHOD_KEY = CM.CONFIRMATION_METHOD_KEY
              INNER JOIN INVESTIGATION INV
                  ON CMG.INVESTIGATION_KEY = INV.INVESTIGATION_KEY
              WHERE INV.LAST_CHG_TIME IS NULL;',

             'SELECT COUNT(DISTINCT CM.CONFIRMATION_METHOD_KEY)
              FROM CONFIRMATION_METHOD CM
              INNER JOIN CONFIRMATION_METHOD_GROUP CMG
                  ON CMG.CONFIRMATION_METHOD_KEY = CM.CONFIRMATION_METHOD_KEY
              INNER JOIN INVESTIGATION INV
                  ON CMG.INVESTIGATION_KEY = INV.INVESTIGATION_KEY
              WHERE INV.LAST_CHG_TIME :operator :timestamp;',

             'WITH PaginatedResults AS (
                 SELECT DISTINCT CM.*,
                        ROW_NUMBER() OVER (ORDER BY INV.LAST_CHG_TIME ASC, CM.CONFIRMATION_METHOD_KEY) AS RowNum
                 FROM CONFIRMATION_METHOD CM
                 INNER JOIN CONFIRMATION_METHOD_GROUP CMG
                     ON CMG.CONFIRMATION_METHOD_KEY = CM.CONFIRMATION_METHOD_KEY
                 INNER JOIN INVESTIGATION INV
                     ON CMG.INVESTIGATION_KEY = INV.INVESTIGATION_KEY
                 WHERE INV.LAST_CHG_TIME :operator :timestamp
             )
             SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;



IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'Rdb_Date')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('Rdb_Date', 'RDB',
             'SELECT * FROM Rdb_Date;',
             NULL,
             'SELECT COUNT(*) FROM Rdb_Date;',
             'WITH PaginatedResults AS (
                 SELECT DISTINCT *
                 FROM Rdb_Date
             ),
             NumberedResults AS (
                 SELECT *, ROW_NUMBER() OVER (ORDER BY DATE_KEY) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'NRT_OBSERVATION')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('NRT_OBSERVATION', 'RDB_MODERN',
             'SELECT * FROM nrt_observation WHERE last_chg_time :operator :timestamp OR refresh_datetime :operator :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM nrt_observation WHERE last_chg_time :operator :timestamp OR refresh_datetime :operator :timestamp;',
             'WITH PaginatedResults AS (
                 SELECT DISTINCT *
                 FROM nrt_observation
                 WHERE last_chg_time :operator :timestamp OR refresh_datetime :operator :timestamp
             ),
             NumberedResults AS (
                 SELECT *, ROW_NUMBER() OVER (
                     ORDER BY
                         CASE
                             WHEN last_chg_time >= refresh_datetime THEN last_chg_time
                             ELSE refresh_datetime
                         END ASC,
                         observation_uid
                 ) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'NRT_OBSERVATION_CODED')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('NRT_OBSERVATION_CODED', 'RDB_MODERN',

             'SELECT *
              FROM rdb_modern.dbo.nrt_observation_coded noc
              JOIN rdb_modern.dbo.nrt_observation no
                  ON noc.observation_uid = no.observation_uid
              WHERE noc.refresh_datetime :operator :timestamp
                 OR no.last_chg_time :operator :timestamp
                 OR no.refresh_datetime :operator :timestamp;',

             NULL,

             'SELECT COUNT(*)
              FROM rdb_modern.dbo.nrt_observation_coded noc
              JOIN rdb_modern.dbo.nrt_observation no
                  ON noc.observation_uid = no.observation_uid
              WHERE noc.refresh_datetime :operator :timestamp
                 OR no.last_chg_time :operator :timestamp
                 OR no.refresh_datetime :operator :timestamp;',

             'WITH PaginatedResults AS (
                 SELECT noc.*,
                        ROW_NUMBER() OVER (ORDER BY noc.refresh_datetime ASC) AS RowNum
                 FROM rdb_modern.dbo.nrt_observation_coded noc
                 JOIN rdb_modern.dbo.nrt_observation no
                     ON noc.observation_uid = no.observation_uid
                 WHERE noc.refresh_datetime :operator :timestamp
                    OR no.last_chg_time :operator :timestamp
                    OR no.refresh_datetime :operator :timestamp
             )
             SELECT * FROM PaginatedResults
             WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'CONDITION_CODE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('CONDITION_CODE', 'SRTE',
             'SELECT * FROM CONDITION_CODE',
             NULL,
             'SELECT COUNT(*) FROM CONDITION_CODE;',
             'WITH PaginatedResults AS (
                 SELECT DISTINCT *
                 FROM CONDITION_CODE
             ),
             NumberedResults AS (
                 SELECT *, ROW_NUMBER() OVER (ORDER BY condition_cd) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'CODE_TO_CONDITION')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('CODE_TO_CONDITION', 'SRTE',
             'SELECT * FROM CODE_TO_CONDITION',
             NULL,
             'SELECT COUNT(*) FROM CODE_TO_CONDITION;',
             NULL);

    END;


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'Program_area_code')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('Program_area_code', 'SRTE',
             'SELECT * FROM Program_area_code;',
             NULL,
             'SELECT COUNT(*) FROM Program_area_code;',
             'WITH PaginatedResults AS (
                 SELECT DISTINCT *
                 FROM Program_area_code
             ),
             NumberedResults AS (
                 SELECT *, ROW_NUMBER() OVER (ORDER BY prog_area_cd) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'JURISDICTION_CODE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('JURISDICTION_CODE', 'SRTE',
             'SELECT * FROM JURISDICTION_CODE;',
             NULL,
             'SELECT COUNT(*) FROM JURISDICTION_CODE;',
             'WITH PaginatedResults AS (
                 SELECT DISTINCT *
                 FROM JURISDICTION_CODE
             ),
             NumberedResults AS (
                 SELECT *, ROW_NUMBER() OVER (ORDER BY code) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'COVID_CASE_DATAMART')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('COVID_CASE_DATAMART', 'RDB',
             'SELECT rdb_modern.dbo.COVID_CASE_DATAMART.*
              FROM rdb_modern.dbo.COVID_CASE_DATAMART
              WHERE rdb_modern.dbo.COVID_CASE_DATAMART.ADD_TIME :operator :timestamp
              OR rdb_modern.dbo.COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp;',
             NULL,
             'SELECT COUNT(*)
              FROM rdb_modern.dbo.COVID_CASE_DATAMART
              WHERE rdb_modern.dbo.COVID_CASE_DATAMART.ADD_TIME :operator :timestamp
              OR rdb_modern.dbo.COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp;',
             'WITH PaginatedResults AS (
                 SELECT DISTINCT rdb_modern.dbo.COVID_CASE_DATAMART.*
                 FROM rdb_modern.dbo.COVID_CASE_DATAMART
                 WHERE rdb_modern.dbo.COVID_CASE_DATAMART.ADD_TIME :operator :timestamp
                 OR rdb_modern.dbo.COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp
             ),
             NumberedResults AS (
                 SELECT *, ROW_NUMBER() OVER (
                     ORDER BY
                         CASE
                             WHEN ADD_TIME >= LAST_CHG_TIME THEN ADD_TIME
                             ELSE LAST_CHG_TIME
                         END ASC,
                         COVID_CASE_DATAMART_KEY
                 ) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'COVID_LAB_DATAMART')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('COVID_LAB_DATAMART', 'RDB',
             'SELECT COVID_LAB_DATAMART.*
              FROM COVID_LAB_DATAMART
              WHERE COVID_LAB_DATAMART.Lab_Added_Dt :operator :timestamp OR COVID_LAB_DATAMART.Lab_Update_Dt :operator :timestamp;',
             NULL,
             'SELECT COUNT(*)
              FROM COVID_LAB_DATAMART
              WHERE COVID_LAB_DATAMART.Lab_Added_Dt :operator :timestamp OR COVID_LAB_DATAMART.Lab_Update_Dt :operator :timestamp;',
             'WITH PaginatedResults AS (
                 SELECT DISTINCT COVID_LAB_DATAMART.*
                 FROM COVID_LAB_DATAMART
                 WHERE COVID_LAB_DATAMART.Lab_Added_Dt :operator :timestamp OR COVID_LAB_DATAMART.Lab_Update_Dt :operator :timestamp
             ),
             NumberedResults AS (
                 SELECT *, ROW_NUMBER() OVER (
                     ORDER BY
                         CASE
                             WHEN Lab_Added_Dt >= Lab_Update_Dt THEN Lab_Added_Dt
                             ELSE Lab_Update_Dt
                         END ASC,
                         COVID_LAB_DATAMART_KEY
                 ) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'COVID_LAB_CELR_DATAMART')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('COVID_LAB_CELR_DATAMART', 'RDB', 'SELECT COVID_LAB_CELR_DATAMART.* FROM COVID_LAB_CELR_DATAMART',
             NULL, 'SELECT COUNT(*)
FROM COVID_LAB_CELR_DATAMART', '')
        ;

    END;


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ENTITY')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('ENTITY', 'NBS_ODSE', 'WITH EntityResults AS (
    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PARTICIPATION
    ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
    INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
)
SELECT ENTITY.*
FROM ENTITY
INNER JOIN EntityResults
ON ENTITY.entity_uid = EntityResults.entity_uid;', NULL, 'SELECT COUNT(*) FROM (
    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PARTICIPATION
    ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
    INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
) AS ENTITY_COUNT;
', 'WITH PaginatedResults AS (
    SELECT DISTINCT ENTITY.entity_uid,
           MAX(CASE WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time ELSE OBSERVATION.last_chg_time END) AS latest_timestamp
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
    GROUP BY ENTITY.entity_uid

    UNION

    SELECT DISTINCT ENTITY.entity_uid,
           MAX(CASE WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time ELSE OBSERVATION.last_chg_time END) AS latest_timestamp
    FROM ENTITY
    INNER JOIN PARTICIPATION
    ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
    INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
    GROUP BY ENTITY.entity_uid

    UNION

    SELECT DISTINCT ENTITY.entity_uid,
           MAX(CASE WHEN OBS_MAIN.add_time >= OBS_MAIN.last_chg_time THEN OBS_MAIN.add_time ELSE OBS_MAIN.last_chg_time END) AS latest_timestamp
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
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



IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ROLE')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('ROLE', 'NBS_ODSE',

             'SELECT DISTINCT ROLE.*
              FROM ROLE
              INNER JOIN PARTICIPATION
                  ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
                  AND ROLE.role_seq = PARTICIPATION.role_seq
                  AND ROLE.cd = PARTICIPATION.cd
              INNER JOIN OBSERVATION
                  ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
              WHERE (OBSERVATION.add_time :operator :timestamp
                     OR OBSERVATION.last_chg_time :operator :timestamp);',

             NULL,

             'SELECT COUNT(*) FROM (
                 SELECT DISTINCT ROLE.subject_entity_uid, ROLE.role_seq, ROLE.cd
                 FROM ROLE
                 INNER JOIN PARTICIPATION
                     ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
                     AND ROLE.role_seq = PARTICIPATION.role_seq
                     AND ROLE.cd = PARTICIPATION.cd
                 INNER JOIN OBSERVATION
                     ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                 WHERE (OBSERVATION.add_time :operator :timestamp
                        OR OBSERVATION.last_chg_time :operator :timestamp)
             ) AS ROLE_COUNT;',

             'WITH PaginatedResults AS (
                 SELECT DISTINCT ROLE.*,
                        COALESCE(OBSERVATION.add_time, OBSERVATION.last_chg_time) AS latest_timestamp
                 FROM ROLE
                 INNER JOIN PARTICIPATION
                     ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
                     AND ROLE.role_seq = PARTICIPATION.role_seq
                     AND ROLE.cd = PARTICIPATION.cd
                 INNER JOIN OBSERVATION
                     ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                 WHERE (OBSERVATION.add_time :operator :timestamp
                        OR OBSERVATION.last_chg_time :operator :timestamp)
             ),
             NumberedResults AS (
                 SELECT *,
                        ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, subject_entity_uid, role_seq, cd) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PERSON')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('PERSON', 'NBS_ODSE',
             'WITH PersonResults AS (
     SELECT DISTINCT PERSON.person_uid
     FROM PERSON
     INNER JOIN OBSERVATION
     ON PERSON.person_uid = OBSERVATION.subject_person_uid
     WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

     UNION

     SELECT DISTINCT PERSON.person_uid
     FROM PERSON
     INNER JOIN OBSERVATION as OBS_DOMAIN
     ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
     INNER JOIN OBSERVATION as OBS_MAIN
     ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
     WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
 )
 SELECT PERSON.*
 FROM PERSON
 INNER JOIN PersonResults
 ON PERSON.person_uid = PersonResults.person_uid;',
             NULL,
             'SELECT COUNT(*) FROM (
     SELECT DISTINCT PERSON.person_uid
     FROM PERSON
     INNER JOIN OBSERVATION
     ON PERSON.person_uid = OBSERVATION.subject_person_uid
     WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

     UNION

     SELECT DISTINCT PERSON.person_uid
     FROM PERSON
     INNER JOIN OBSERVATION as OBS_DOMAIN
     ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
     INNER JOIN OBSERVATION as OBS_MAIN
     ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
     WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
 ) AS PERSON_COUNT;',
             'WITH PersonResults AS (
     SELECT DISTINCT PERSON.person_uid,
            MAX(CASE
                WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
                ELSE OBSERVATION.last_chg_time
            END) AS latest_timestamp
     FROM PERSON
     INNER JOIN OBSERVATION
     ON PERSON.person_uid = OBSERVATION.subject_person_uid
     WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
     GROUP BY PERSON.person_uid

     UNION

     SELECT DISTINCT PERSON.person_uid,
            MAX(CASE
                WHEN OBS_MAIN.add_time >= OBS_MAIN.last_chg_time THEN OBS_MAIN.add_time
                ELSE OBS_MAIN.last_chg_time
            END) AS latest_timestamp
     FROM PERSON
     INNER JOIN OBSERVATION as OBS_DOMAIN
     ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
     INNER JOIN OBSERVATION as OBS_MAIN
     ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
     WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
     GROUP BY PERSON.person_uid
 ),
 NumberedResults AS (
     SELECT person_uid, ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, person_uid) AS RowNum
     FROM PersonResults
 )
 SELECT PERSON.*
 FROM PERSON
 INNER JOIN NumberedResults
 ON PERSON.person_uid = NumberedResults.person_uid
 WHERE NumberedResults.RowNum BETWEEN :startRow AND :endRow;');

    END;



IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ACT')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('ACT', 'NBS_ODSE',
             'WITH ActResults AS (
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

     UNION

     SELECT DISTINCT ACT.act_uid
     FROM ACT
     INNER JOIN OBSERVATION as OBS_DOMAIN
     ON ACT.act_uid = OBS_DOMAIN.subject_person_uid
     INNER JOIN OBSERVATION as OBS_MAIN
     ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
     WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
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
     WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

     UNION

     SELECT DISTINCT ACT.act_uid
     FROM ACT
     INNER JOIN OBSERVATION as OBS_DOMAIN
     ON ACT.act_uid = OBS_DOMAIN.subject_person_uid
     INNER JOIN OBSERVATION as OBS_MAIN
     ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
     WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
 ) AS ACT_COUNT;',
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

     UNION

     SELECT DISTINCT ACT.act_uid,
            MAX(CASE
                WHEN OBS_MAIN.add_time >= OBS_MAIN.last_chg_time THEN OBS_MAIN.add_time
                ELSE OBS_MAIN.last_chg_time
            END) AS latest_timestamp
     FROM ACT
     INNER JOIN OBSERVATION as OBS_DOMAIN
     ON ACT.act_uid = OBS_DOMAIN.subject_person_uid
     INNER JOIN OBSERVATION as OBS_MAIN
     ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
     WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
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


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'OBSERVATION')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('OBSERVATION', 'NBS_ODSE',
             'WITH ObservationResults AS (
     SELECT DISTINCT OBSERVATION.observation_uid
     FROM OBSERVATION
     INNER JOIN ACT
     ON ACT.act_uid = OBSERVATION.observation_uid
     INNER JOIN PERSON
     ON PERSON.person_uid = OBSERVATION.subject_person_uid
     WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

     UNION

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
     SELECT DISTINCT OBSERVATION.observation_uid
     FROM OBSERVATION
     INNER JOIN ACT
     ON ACT.act_uid = OBSERVATION.observation_uid
     INNER JOIN PERSON
     ON PERSON.person_uid = OBSERVATION.subject_person_uid
     WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

     UNION

     SELECT DISTINCT OBS_DOMAIN.observation_uid
     FROM OBSERVATION as OBS_DOMAIN
     INNER JOIN OBSERVATION as OBS_MAIN
     ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
     WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
 ) AS OBSERVATION_COUNT;
 ',
             'WITH ObservationResults AS (
     SELECT DISTINCT OBSERVATION.observation_uid,
            MAX(CASE
                WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
                ELSE OBSERVATION.last_chg_time
            END) AS latest_timestamp
     FROM OBSERVATION
     INNER JOIN ACT
     ON ACT.act_uid = OBSERVATION.observation_uid
     INNER JOIN PERSON
     ON PERSON.person_uid = OBSERVATION.subject_person_uid
     WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
     GROUP BY OBSERVATION.observation_uid

     UNION

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


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PARTICIPATION')
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
              INNER JOIN ROLE
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
                 INNER JOIN ROLE
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
                 INNER JOIN ROLE
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


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'Act_relationship')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('Act_relationship', 'NBS_ODSE',

             'SELECT DISTINCT Act_relationship.*
              FROM Act_relationship
              INNER JOIN OBSERVATION
                  ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
              INNER JOIN ACT AS ACT_SOURCE
                  ON ACT_SOURCE.act_uid = Act_relationship.source_act_uid
              INNER JOIN ACT AS ACT_TARGET
                  ON ACT_TARGET.act_uid = Act_relationship.target_act_uid
              WHERE (OBSERVATION.add_time :operator :timestamp
                     OR OBSERVATION.last_chg_time :operator :timestamp);',

             NULL,

             'SELECT COUNT(*) FROM (
                 SELECT DISTINCT Act_relationship.source_act_uid, Act_relationship.target_act_uid, Act_relationship.type_cd
                 FROM Act_relationship
                 INNER JOIN OBSERVATION
                     ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
                 INNER JOIN ACT AS ACT_SOURCE
                     ON ACT_SOURCE.act_uid = Act_relationship.source_act_uid
                 INNER JOIN ACT AS ACT_TARGET
                     ON ACT_TARGET.act_uid = Act_relationship.target_act_uid
                 WHERE (OBSERVATION.add_time :operator :timestamp
                        OR OBSERVATION.last_chg_time :operator :timestamp)
             ) AS ACT_RELATIONSHIP_COUNT;',

             'WITH PaginatedResults AS (
                 SELECT DISTINCT Act_relationship.*,
                        COALESCE(OBSERVATION.add_time, OBSERVATION.last_chg_time) AS latest_timestamp
                 FROM Act_relationship
                 INNER JOIN OBSERVATION
                     ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
                 INNER JOIN ACT AS ACT_SOURCE
                     ON ACT_SOURCE.act_uid = Act_relationship.source_act_uid
                 INNER JOIN ACT AS ACT_TARGET
                     ON ACT_TARGET.act_uid = Act_relationship.target_act_uid
                 WHERE (OBSERVATION.add_time :operator :timestamp
                        OR OBSERVATION.last_chg_time :operator :timestamp)
             ),
             NumberedResults AS (
                 SELECT *,
                        ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, source_act_uid, target_act_uid, type_cd) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;
