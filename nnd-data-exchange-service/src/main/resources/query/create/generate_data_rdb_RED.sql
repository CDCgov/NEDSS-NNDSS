DELETE FROM [dbo].[data_sync_config]
WHERE table_name IN (
                     'BMIRD_MULTI_VALUE_FIELD',
                     'BMIRD_MULTI_VALUE_FIELD_GROUP',
                     'ANTIMICROBIAL',
                     'ANTIMICROBIAL_GROUP',
                     'D_PCR_SOURCE_GROUP',
                    'D_PCR_SOURCE',
                    'D_RASH_LOC_GEN_GROUP',
                    'D_RASH_LOC_GEN',
                    'D_VAR_PAM',
                    'HEP_MULTI_VALUE_FIELD',
                    'HEP_MULTI_VALUE_FIELD_GROUP',
                    'LDF_DATA',
                    'LDF_FOODBORNE',
                    'LDF_GROUP',
                    'NOTIFICATION',
                    'PERTUSSIS_SUSPECTED_SOURCE_FLD',
                    'PERTUSSIS_SUSPECTED_SOURCE_GRP',
                    'PERTUSSIS_TREATMENT_FIELD',
                    'PERTUSSIS_TREATMENT_GROUP',
                    'TREATMENT',
                    'TREATMENT_EVENT',
                    'VAR_PAM_LDF',
                    'STD_HIV_DATAMART'

    );


INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('ANTIMICROBIAL_GROUP', 'RDB',
     'SELECT ANTIMICROBIAL_GROUP.* FROM ANTIMICROBIAL_GROUP',
     NULL,
     'SELECT COUNT(*) FROM ANTIMICROBIAL_GROUP',
     'WITH PaginatedResults AS (
        SELECT ANTIMICROBIAL_GROUP.*,
               ROW_NUMBER() OVER (ORDER BY ANTIMICROBIAL_GROUP.ANTIMICROBIAL_GRP_KEY) AS RowNum
        FROM ANTIMICROBIAL_GROUP
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow'),
    ('ANTIMICROBIAL', 'RDB',
     'SELECT ANTIMICROBIAL.* FROM ANTIMICROBIAL',
     NULL,
     'SELECT COUNT(*) FROM ANTIMICROBIAL',
     'WITH PaginatedResults AS (
        SELECT ANTIMICROBIAL.*,
               ROW_NUMBER() OVER (ORDER BY ANTIMICROBIAL.ANTIMICROBIAL_GRP_KEY, ANTIMICROBIAL.ANTIMICROBIAL_KEY) AS RowNum
        FROM ANTIMICROBIAL
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('D_PCR_SOURCE_GROUP', 'RDB',
     'SELECT D_PCR_SOURCE_GROUP.* FROM D_PCR_SOURCE_GROUP',
     NULL,
     'SELECT COUNT(*) FROM D_PCR_SOURCE_GROUP',
     'WITH PaginatedResults AS (
        SELECT D_PCR_SOURCE_GROUP.*,
               ROW_NUMBER() OVER (ORDER BY D_PCR_SOURCE_GROUP.D_PCR_SOURCE_GROUP_KEY) AS RowNum
        FROM D_PCR_SOURCE_GROUP
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow'),

    ('D_PCR_SOURCE', 'RDB',
     'SELECT D_PCR_SOURCE.* FROM D_PCR_SOURCE',
     NULL,
     'SELECT COUNT(*) FROM D_PCR_SOURCE',
     'WITH PaginatedResults AS (
        SELECT D_PCR_SOURCE.*,
               ROW_NUMBER() OVER (ORDER BY D_PCR_SOURCE.D_PCR_SOURCE_KEY, D_PCR_SOURCE.VAR_PAM_UID) AS RowNum
        FROM D_PCR_SOURCE
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('D_RASH_LOC_GEN_GROUP', 'RDB',
     'SELECT D_RASH_LOC_GEN_GROUP.* FROM D_RASH_LOC_GEN_GROUP',
     NULL,
     'SELECT COUNT(*) FROM D_RASH_LOC_GEN_GROUP',
     'WITH PaginatedResults AS (
        SELECT D_RASH_LOC_GEN_GROUP.*,
               ROW_NUMBER() OVER (ORDER BY D_RASH_LOC_GEN_GROUP.D_RASH_LOC_GEN_GROUP_KEY) AS RowNum
        FROM D_RASH_LOC_GEN_GROUP
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow'),

    ('D_RASH_LOC_GEN', 'RDB',
     'SELECT D_RASH_LOC_GEN.* FROM D_RASH_LOC_GEN',
     NULL,
     'SELECT COUNT(*) FROM D_RASH_LOC_GEN',
     'WITH PaginatedResults AS (
        SELECT D_RASH_LOC_GEN.*,
               ROW_NUMBER() OVER (ORDER BY D_RASH_LOC_GEN.D_RASH_LOC_GEN_KEY, D_RASH_LOC_GEN.VAR_PAM_UID) AS RowNum
        FROM D_RASH_LOC_GEN
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('BMIRD_MULTI_VALUE_FIELD_GROUP', 'RDB',
     'SELECT BMIRD_MULTI_VALUE_FIELD_GROUP.* FROM BMIRD_MULTI_VALUE_FIELD_GROUP',
     NULL,
     'SELECT COUNT(*) FROM BMIRD_MULTI_VALUE_FIELD_GROUP',
     'WITH PaginatedResults AS (
        SELECT BMIRD_MULTI_VALUE_FIELD_GROUP.*,
               ROW_NUMBER() OVER (ORDER BY BMIRD_MULTI_VALUE_FIELD_GROUP.BMIRD_MULTI_VAL_GRP_KEY) AS RowNum
        FROM BMIRD_MULTI_VALUE_FIELD_GROUP
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow'),

    ('BMIRD_MULTI_VALUE_FIELD', 'RDB',
     'SELECT BMIRD_MULTI_VALUE_FIELD.* FROM BMIRD_MULTI_VALUE_FIELD',
     NULL,
     'SELECT COUNT(*) FROM BMIRD_MULTI_VALUE_FIELD',
     'WITH PaginatedResults AS (
        SELECT BMIRD_MULTI_VALUE_FIELD.*,
               ROW_NUMBER() OVER (ORDER BY BMIRD_MULTI_VALUE_FIELD.BMIRD_MULTI_VAL_FIELD_KEY, BMIRD_MULTI_VALUE_FIELD.BMIRD_MULTI_VAL_GRP_KEY) AS RowNum
        FROM BMIRD_MULTI_VALUE_FIELD
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('D_VAR_PAM', 'RDB',
     'SELECT D_VAR_PAM.* FROM D_VAR_PAM',
     NULL,
     'SELECT COUNT(*) FROM D_VAR_PAM',
     'WITH PaginatedResults AS (
        SELECT D_VAR_PAM.*,
               ROW_NUMBER() OVER (ORDER BY D_VAR_PAM.D_VAR_PAM_KEY) AS RowNum
        FROM D_VAR_PAM
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('HEP_MULTI_VALUE_FIELD_GROUP', 'RDB',
     'SELECT HEP_MULTI_VALUE_FIELD_GROUP.* FROM HEP_MULTI_VALUE_FIELD_GROUP',
     NULL,
     'SELECT COUNT(*) FROM HEP_MULTI_VALUE_FIELD_GROUP',
     'WITH PaginatedResults AS (
        SELECT HEP_MULTI_VALUE_FIELD_GROUP.*,
               ROW_NUMBER() OVER (ORDER BY HEP_MULTI_VALUE_FIELD_GROUP.HEP_MULTI_VAL_GRP_KEY) AS RowNum
        FROM HEP_MULTI_VALUE_FIELD_GROUP
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow'),

    ('HEP_MULTI_VALUE_FIELD', 'RDB',
     'SELECT HEP_MULTI_VALUE_FIELD.* FROM HEP_MULTI_VALUE_FIELD',
     NULL,
     'SELECT COUNT(*) FROM HEP_MULTI_VALUE_FIELD',
     'WITH PaginatedResults AS (
        SELECT HEP_MULTI_VALUE_FIELD.*,
               ROW_NUMBER() OVER (ORDER BY HEP_MULTI_VALUE_FIELD.HEP_MULTI_VAL_GRP_KEY, HEP_MULTI_VALUE_FIELD.HEP_MULTI_VAL_DATA_KEY) AS RowNum
        FROM HEP_MULTI_VALUE_FIELD
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('LDF_DATA', 'RDB',
     'SELECT LDF_DATA.* FROM LDF_DATA',
     NULL,
     'SELECT COUNT(*) FROM LDF_DATA',
     'WITH PaginatedResults AS (
        SELECT LDF_DATA.*,
               ROW_NUMBER() OVER (ORDER BY LDF_DATA.LDF_DATA_KEY) AS RowNum
        FROM LDF_DATA
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow'),

    ('LDF_FOODBORNE', 'RDB',
     'SELECT LDF_FOODBORNE.* FROM LDF_FOODBORNE',
     NULL,
     'SELECT COUNT(*) FROM LDF_FOODBORNE',
     'WITH PaginatedResults AS (
        SELECT LDF_FOODBORNE.*,
               ROW_NUMBER() OVER (ORDER BY LDF_FOODBORNE.INVESTIGATION_KEY) AS RowNum
        FROM LDF_FOODBORNE
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow'),

    ('LDF_GROUP', 'RDB',
     'SELECT LDF_GROUP.* FROM LDF_GROUP',
     NULL,
     'SELECT COUNT(*) FROM LDF_GROUP',
     'WITH PaginatedResults AS (
        SELECT LDF_GROUP.*,
               ROW_NUMBER() OVER (ORDER BY LDF_GROUP.LDF_GROUP_KEY) AS RowNum
        FROM LDF_GROUP
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('NOTIFICATION', 'RDB',
     'SELECT NOTIFICATION.* FROM NOTIFICATION',
     NULL,
     'SELECT COUNT(*) FROM NOTIFICATION',
     'WITH PaginatedResults AS (
        SELECT NOTIFICATION.*,
               ROW_NUMBER() OVER (ORDER BY NOTIFICATION.NOTIFICATION_KEY) AS RowNum
        FROM NOTIFICATION
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('PERTUSSIS_SUSPECTED_SOURCE_GRP', 'RDB',
     'SELECT PERTUSSIS_SUSPECTED_SOURCE_GRP.* FROM PERTUSSIS_SUSPECTED_SOURCE_GRP',
     NULL,
     'SELECT COUNT(*) FROM PERTUSSIS_SUSPECTED_SOURCE_GRP',
     'WITH PaginatedResults AS (
        SELECT PERTUSSIS_SUSPECTED_SOURCE_GRP.*,
               ROW_NUMBER() OVER (ORDER BY PERTUSSIS_SUSPECTED_SOURCE_GRP.PERTUSSIS_SUSPECT_SRC_GRP_KEY) AS RowNum
        FROM PERTUSSIS_SUSPECTED_SOURCE_GRP
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow'),
    ('PERTUSSIS_SUSPECTED_SOURCE_FLD', 'RDB',
     'SELECT PERTUSSIS_SUSPECTED_SOURCE_FLD.* FROM PERTUSSIS_SUSPECTED_SOURCE_FLD',
     NULL,
     'SELECT COUNT(*) FROM PERTUSSIS_SUSPECTED_SOURCE_FLD',
     'WITH PaginatedResults AS (
        SELECT PERTUSSIS_SUSPECTED_SOURCE_FLD.*,
               ROW_NUMBER() OVER (ORDER BY PERTUSSIS_SUSPECTED_SOURCE_FLD.PERTUSSIS_SUSPECT_SRC_FLD_KEY, PERTUSSIS_SUSPECTED_SOURCE_FLD.PERTUSSIS_SUSPECT_SRC_GRP_KEY) AS RowNum
        FROM PERTUSSIS_SUSPECTED_SOURCE_FLD
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('PERTUSSIS_TREATMENT_GROUP', 'RDB',
     'SELECT PERTUSSIS_TREATMENT_GROUP.* FROM PERTUSSIS_TREATMENT_GROUP',
     NULL,
     'SELECT COUNT(*) FROM PERTUSSIS_TREATMENT_GROUP',
     'WITH PaginatedResults AS (
        SELECT PERTUSSIS_TREATMENT_GROUP.*,
               ROW_NUMBER() OVER (ORDER BY PERTUSSIS_TREATMENT_GROUP.PERTUSSIS_TREATMENT_GRP_KEY) AS RowNum
        FROM PERTUSSIS_TREATMENT_GROUP
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow'),
    ('PERTUSSIS_TREATMENT_FIELD', 'RDB',
     'SELECT PERTUSSIS_TREATMENT_FIELD.* FROM PERTUSSIS_TREATMENT_FIELD',
     NULL,
     'SELECT COUNT(*) FROM PERTUSSIS_TREATMENT_FIELD',
     'WITH PaginatedResults AS (
        SELECT PERTUSSIS_TREATMENT_FIELD.*,
               ROW_NUMBER() OVER (ORDER BY PERTUSSIS_TREATMENT_FIELD.PERTUSSIS_TREATMENT_FLD_KEY, PERTUSSIS_TREATMENT_FIELD.PERTUSSIS_TREATMENT_GRP_KEY) AS RowNum
        FROM PERTUSSIS_TREATMENT_FIELD
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');


INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('TREATMENT', 'RDB',
     'SELECT TREATMENT.* FROM TREATMENT',
     NULL,
     'SELECT COUNT(*) FROM TREATMENT',
     'WITH PaginatedResults AS (
        SELECT TREATMENT.*,
               ROW_NUMBER() OVER (ORDER BY TREATMENT.TREATMENT_KEY) AS RowNum
        FROM TREATMENT
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow'),

    ('TREATMENT_EVENT', 'RDB',
     'SELECT TREATMENT_EVENT.* FROM TREATMENT_EVENT',
     NULL,
     'SELECT COUNT(*) FROM TREATMENT_EVENT',
     'WITH PaginatedResults AS (
        SELECT TREATMENT_EVENT.*,
               ROW_NUMBER() OVER (ORDER BY TREATMENT_EVENT.TREATMENT_DT_KEY, TREATMENT_EVENT.TREATMENT_PROVIDING_ORG_KEY, TREATMENT_EVENT.PATIENT_KEY,' +
                    'TREATMENT_EVENT.TREATMENT_KEY, TREATMENT_EVENT.MORB_RPT_KEY, TREATMENT_EVENT.TREATMENT_PHYSICIAN_KEY,' +
                    'TREATMENT_EVENT.INVESTIGATION_KEY, TREATMENT_EVENT.CONDITION_KEY, TREATMENT_EVENT.LDF_GROUP_KEY) AS RowNum
        FROM TREATMENT_EVENT
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');


INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('VAR_PAM_LDF', 'RDB',
     'SELECT VAR_PAM_LDF.* FROM VAR_PAM_LDF',
     NULL,
     'SELECT COUNT(*) FROM VAR_PAM_LDF',
     'WITH PaginatedResults AS (
        SELECT VAR_PAM_LDF.*,
               ROW_NUMBER() OVER (ORDER BY VAR_PAM_LDF.INVESTIGATION_KEY) AS RowNum
        FROM VAR_PAM_LDF
     )
     SELECT * FROM PaginatedResults
     WHERE RowNum BETWEEN :startRow AND :endRow');



INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('STD_HIV_DATAMART', 'RDB',
     'SELECT STD_HIV_DATAMART.*
      FROM STD_HIV_DATAMART
      JOIN INVESTIGATION ON STD_HIV_DATAMART.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
      WHERE STD_HIV_DATAMART.add_time :operator :timestamp
         OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
     NULL,
     'SELECT COUNT(*)
      FROM STD_HIV_DATAMART
      JOIN INVESTIGATION ON STD_HIV_DATAMART.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
      WHERE STD_HIV_DATAMART.add_time :operator :timestamp
         OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
     'WITH PaginatedResults AS (
        SELECT STD_HIV_DATAMART.*,
               ROW_NUMBER() OVER (
                   ORDER BY
                       CASE
                           WHEN STD_HIV_DATAMART.add_time :operator :timestamp
                           THEN STD_HIV_DATAMART.add_time
                           ELSE INVESTIGATION.LAST_CHG_TIME
                       END DESC,
                       STD_HIV_DATAMART.INVESTIGATION_KEY
               ) AS RowNum
        FROM STD_HIV_DATAMART
        JOIN INVESTIGATION ON STD_HIV_DATAMART.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
        WHERE STD_HIV_DATAMART.add_time :operator :timestamp
           OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp
    )
    SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');