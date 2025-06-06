USE RDB
GO

DELETE FROM [dbo].[data_sync_config];

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_PATIENT')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_PATIENT',  'RDB',
             'SELECT * FROM D_PATIENT WHERE D_PATIENT.PATIENT_LAST_CHANGE_TIME :operator :timestamp;',
             'SELECT * FROM D_PATIENT WHERE D_PATIENT.PATIENT_LAST_CHANGE_TIME IS NULL;',
             'SELECT COUNT(*) FROM D_PATIENT WHERE D_PATIENT.PATIENT_LAST_CHANGE_TIME :operator :timestamp;',
             'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY PATIENT_KEY) AS RowNum FROM D_PATIENT WHERE D_PATIENT.PATIENT_LAST_CHANGE_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'INVESTIGATION')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('INVESTIGATION',  'RDB',
             'SELECT * FROM INVESTIGATION WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             'SELECT * FROM INVESTIGATION WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
             'SELECT COUNT(*) FROM INVESTIGATION WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY INVESTIGATION_KEY) AS RowNum FROM INVESTIGATION WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'CASE_COUNT')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('CASE_COUNT', 'RDB',
             'SELECT * FROM CASE_COUNT;',
             NULL,
             'SELECT COUNT(*) FROM CASE_COUNT;',
             'WITH PaginatedResults AS (SELECT A.*, ROW_NUMBER() OVER (ORDER BY CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY) AS RowNum FROM CASE_COUNT as A) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'BMIRD_CASE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('BMIRD_CASE', 'RDB',
             'SELECT BMIRD_CASE.* FROM BMIRD_CASE;',
             NULL,
             'SELECT COUNT(*) FROM BMIRD_CASE;',
             'WITH PaginatedResults AS (SELECT BMIRD_CASE.*, ROW_NUMBER()
             OVER (ORDER BY ADT_HSPTL_KEY, ANTIMICROBIAL_GRP_KEY, BMIRD_MULTI_VAL_GRP_KEY, CONDITION_KEY, DAYCARE_FACILITY_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, NURSING_HOME_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY) AS RowNum FROM BMIRD_CASE) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'RUBELLA_CASE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('RUBELLA_CASE', 'RDB',
             'SELECT RUBELLA_CASE.* FROM RUBELLA_CASE;',
             NULL,
             'SELECT COUNT(*) FROM RUBELLA_CASE;',
             'WITH PaginatedResults AS (SELECT RUBELLA_CASE.*, ROW_NUMBER() OVER (ORDER BY ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY) AS RowNum FROM RUBELLA_CASE) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PERTUSSIS_CASE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('PERTUSSIS_CASE', 'RDB',
             'SELECT PERTUSSIS_CASE.* FROM PERTUSSIS_CASE',
             NULL,
             'SELECT COUNT(*) FROM PERTUSSIS_CASE;',
             'WITH PaginatedResults AS (SELECT PERTUSSIS_CASE.*, ROW_NUMBER() OVER (ORDER BY ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PERTUSSIS_SUSPECT_SRC_GRP_KEY, PERTUSSIS_TREATMENT_GRP_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY) AS RowNum FROM PERTUSSIS_CASE) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'HEPATITIS_CASE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('HEPATITIS_CASE', 'RDB',
             'SELECT HEPATITIS_CASE.* FROM HEPATITIS_CASE;',
             NULL, 'SELECT COUNT(*) FROM HEPATITIS_CASE;',
             'WITH PaginatedResults AS (SELECT HEPATITIS_CASE.*, ROW_NUMBER() OVER (ORDER BY ADT_HSPTL_KEY, CONDITION_KEY, HEP_MULTI_VAL_GRP_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY) AS RowNum FROM HEPATITIS_CASE) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'GENERIC_CASE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('GENERIC_CASE', 'RDB',
             'SELECT GENERIC_CASE.* FROM GENERIC_CASE;',
             NULL,
             'SELECT COUNT(*) FROM GENERIC_CASE;',
             'WITH PaginatedResults AS (SELECT GENERIC_CASE.*, ROW_NUMBER() OVER (ORDER BY ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY) AS RowNum FROM GENERIC_CASE) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'F_VAR_PAM')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('F_VAR_PAM', 'RDB',
             'SELECT F_VAR_PAM.* FROM F_VAR_PAM JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', NULL,
             'SELECT COUNT(*) FROM F_VAR_PAM JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             'WITH PaginatedResults AS (SELECT F_VAR_PAM.*, ROW_NUMBER() OVER (ORDER BY F_VAR_PAM.INVESTIGATION_KEY) AS RowNum FROM F_VAR_PAM JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'NOTIFICATION_EVENT')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('NOTIFICATION_EVENT', 'RDB',
             'SELECT NOTIFICATION_EVENT.* FROM NOTIFICATION_EVENT;',
             NULL,
             'SELECT COUNT(*) FROM NOTIFICATION_EVENT;',
             'WITH PaginatedResults AS (SELECT NOTIFICATION_EVENT.*, ROW_NUMBER() OVER (ORDER BY CONDITION_KEY, INVESTIGATION_KEY, NOTIFICATION_KEY, NOTIFICATION_SENT_DT_KEY, NOTIFICATION_SUBMIT_DT_KEY, PATIENT_KEY) AS RowNum FROM NOTIFICATION_EVENT) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;




IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'F_INTERVIEW_CASE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('F_INTERVIEW_CASE', 'RDB',
             'SELECT F_INTERVIEW_CASE.* FROM F_INTERVIEW_CASE JOIN INVESTIGATION ON F_INTERVIEW_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM F_INTERVIEW_CASE JOIN INVESTIGATION ON F_INTERVIEW_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             'WITH PaginatedResults AS (SELECT F_INTERVIEW_CASE.*, ROW_NUMBER() OVER (ORDER BY F_INTERVIEW_CASE.INVESTIGATION_KEY) AS RowNum FROM F_INTERVIEW_CASE JOIN INVESTIGATION ON F_INTERVIEW_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'F_PAGE_CASE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('F_PAGE_CASE', 'RDB',
             'SELECT F_PAGE_CASE.* FROM F_PAGE_CASE JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM F_PAGE_CASE JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             'WITH PaginatedResults AS (SELECT F_PAGE_CASE.*, ROW_NUMBER() OVER (ORDER BY F_PAGE_CASE.INVESTIGATION_KEY) AS RowNum FROM F_PAGE_CASE JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'F_STD_PAGE_CASE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('F_STD_PAGE_CASE', 'RDB',
             'SELECT F_STD_PAGE_CASE.* FROM F_STD_PAGE_CASE JOIN INVESTIGATION ON F_STD_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM F_STD_PAGE_CASE JOIN INVESTIGATION ON F_STD_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             'WITH PaginatedResults AS (SELECT F_STD_PAGE_CASE.*, ROW_NUMBER() OVER (ORDER BY F_STD_PAGE_CASE.INVESTIGATION_KEY) AS RowNum FROM F_STD_PAGE_CASE JOIN INVESTIGATION ON F_STD_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_ORGANIZATION')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_ORGANIZATION', 'RDB',
             'SELECT * FROM D_ORGANIZATION WHERE D_ORGANIZATION.ORGANIZATION_LAST_CHANGE_TIME :operator :timestamp;',
             'SELECT * FROM D_ORGANIZATION WHERE D_ORGANIZATION.ORGANIZATION_LAST_CHANGE_TIME IS NULL;',
             'SELECT COUNT(*) FROM D_ORGANIZATION WHERE D_ORGANIZATION.ORGANIZATION_LAST_CHANGE_TIME :operator :timestamp;',
             'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY ORGANIZATION_KEY) AS RowNum FROM D_ORGANIZATION WHERE D_ORGANIZATION.ORGANIZATION_LAST_CHANGE_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'HEPATITIS_DATAMART')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('HEPATITIS_DATAMART', 'RDB', 'SELECT * FROM HEPATITIS_DATAMART WHERE REFRESH_DATETIME :operator :timestamp;', NULL,
             'SELECT COUNT(*) FROM HEPATITIS_DATAMART WHERE REFRESH_DATETIME :operator :timestamp;',
             'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY INVESTIGATION_KEY) AS RowNum FROM HEPATITIS_DATAMART WHERE REFRESH_DATETIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_CASE_MANAGEMENT')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_CASE_MANAGEMENT', 'RDB',
             'SELECT D_CASE_MANAGEMENT.* FROM D_CASE_MANAGEMENT JOIN INVESTIGATION ON D_CASE_MANAGEMENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM D_CASE_MANAGEMENT JOIN INVESTIGATION ON D_CASE_MANAGEMENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp',
             'WITH PaginatedResults AS (SELECT D_CASE_MANAGEMENT.*, ROW_NUMBER() OVER (ORDER BY D_CASE_MANAGEMENT.D_CASE_MANAGEMENT_KEY) AS RowNum FROM D_CASE_MANAGEMENT JOIN INVESTIGATION ON D_CASE_MANAGEMENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;




IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_INTERVIEW')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_INTERVIEW', 'RDB',
             'SELECT D_INTERVIEW.* FROM D_INTERVIEW WHERE D_INTERVIEW.LAST_CHG_TIME :operator :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM D_INTERVIEW WHERE D_INTERVIEW.LAST_CHG_TIME :operator :timestamp',
             'WITH PaginatedResults AS (SELECT D_INTERVIEW.*, ROW_NUMBER() OVER (ORDER BY D_INTERVIEW.D_INTERVIEW_KEY) AS RowNum FROM D_INTERVIEW WHERE D_INTERVIEW.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'MEASLES_CASE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('MEASLES_CASE', 'RDB',
             'SELECT MEASLES_CASE.* FROM MEASLES_CASE;',
             NULL,
             'SELECT COUNT(*) FROM MEASLES_CASE;',
             'WITH PaginatedResults AS (SELECT MEASLES_CASE.*, ROW_NUMBER() OVER (ORDER BY ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INV_RPT_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY) AS RowNum FROM MEASLES_CASE) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'CONFIRMATION_METHOD_GROUP')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('CONFIRMATION_METHOD_GROUP',
             'RDB',
             'SELECT CONFIRMATION_METHOD_GROUP.* FROM CONFIRMATION_METHOD_GROUP JOIN INVESTIGATION ON CONFIRMATION_METHOD_GROUP.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             'SELECT CONFIRMATION_METHOD_GROUP.* FROM CONFIRMATION_METHOD_GROUP JOIN INVESTIGATION ON CONFIRMATION_METHOD_GROUP.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
             'SELECT COUNT(*) FROM CONFIRMATION_METHOD_GROUP JOIN INVESTIGATION ON CONFIRMATION_METHOD_GROUP.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             'WITH PaginatedResults AS (SELECT CONFIRMATION_METHOD_GROUP.*, ROW_NUMBER() OVER (ORDER BY CONFIRMATION_METHOD_KEY, CONFIRMATION_METHOD_GROUP.INVESTIGATION_KEY) AS RowNum FROM CONFIRMATION_METHOD_GROUP JOIN INVESTIGATION ON CONFIRMATION_METHOD_GROUP.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;





IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_INV_RISK_FACTOR')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_INV_RISK_FACTOR', 'RDB',
             'SELECT D_INV_RISK_FACTOR.* FROM D_INV_RISK_FACTOR WHERE D_INV_RISK_FACTOR_KEY > :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM D_INV_RISK_FACTOR WHERE D_INV_RISK_FACTOR_KEY > :timestamp;',
             'WITH PaginatedResults AS (SELECT D_INV_RISK_FACTOR.*, ROW_NUMBER() OVER (ORDER BY D_INV_RISK_FACTOR.D_INV_RISK_FACTOR_KEY) AS RowNum FROM D_INV_RISK_FACTOR  WHERE D_INV_RISK_FACTOR_KEY > :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_INV_ADMINISTRATIVE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_INV_ADMINISTRATIVE', 'RDB',
             'SELECT D_INV_ADMINISTRATIVE.* FROM D_INV_ADMINISTRATIVE  WHERE D_INV_ADMINISTRATIVE_KEY > :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM D_INV_ADMINISTRATIVE  WHERE D_INV_ADMINISTRATIVE_KEY > :timestamp;',
             'WITH PaginatedResults AS (SELECT D_INV_ADMINISTRATIVE.*, ROW_NUMBER() OVER (ORDER BY D_INV_ADMINISTRATIVE.D_INV_ADMINISTRATIVE_KEY) AS RowNum FROM D_INV_ADMINISTRATIVE  WHERE D_INV_ADMINISTRATIVE_KEY > :timestamp ) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');


    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_INV_EPIDEMIOLOGY')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_INV_EPIDEMIOLOGY', 'RDB',
             'SELECT D_INV_EPIDEMIOLOGY.* FROM D_INV_EPIDEMIOLOGY WHERE D_INV_EPIDEMIOLOGY_KEY > :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM D_INV_EPIDEMIOLOGY WHERE D_INV_EPIDEMIOLOGY_KEY > :timestamp;',
             'WITH PaginatedResults AS (SELECT D_INV_EPIDEMIOLOGY.*, ROW_NUMBER() OVER (ORDER BY D_INV_EPIDEMIOLOGY.D_INV_EPIDEMIOLOGY_KEY) AS RowNum FROM D_INV_EPIDEMIOLOGY WHERE D_INV_EPIDEMIOLOGY_KEY > :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');


    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_INV_HIV')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_INV_HIV', 'RDB',
             'SELECT D_INV_HIV.* FROM D_INV_HIV WHERE D_INV_HIV_KEY > :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM D_INV_HIV WHERE D_INV_HIV_KEY > :timestamp;',
             'WITH PaginatedResults AS (SELECT D_INV_HIV.*, ROW_NUMBER() OVER (ORDER BY D_INV_HIV.D_INV_HIV_KEY) AS RowNum FROM D_INV_HIV WHERE D_INV_HIV_KEY > :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');


    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_INV_LAB_FINDING')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_INV_LAB_FINDING', 'RDB',
             'SELECT D_INV_LAB_FINDING.* FROM D_INV_LAB_FINDING WHERE D_INV_LAB_FINDING_KEY > :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM D_INV_LAB_FINDING WHERE D_INV_LAB_FINDING_KEY > :timestamp;',
             'WITH PaginatedResults AS (SELECT D_INV_LAB_FINDING.*, ROW_NUMBER() OVER (ORDER BY D_INV_LAB_FINDING.D_INV_LAB_FINDING_KEY) AS RowNum FROM D_INV_LAB_FINDING WHERE D_INV_LAB_FINDING_KEY > :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');



    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_INV_MEDICAL_HISTORY')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_INV_MEDICAL_HISTORY', 'RDB',
             'SELECT D_INV_MEDICAL_HISTORY.* FROM D_INV_MEDICAL_HISTORY WHERE D_INV_MEDICAL_HISTORY_KEY > :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM D_INV_MEDICAL_HISTORY WHERE D_INV_MEDICAL_HISTORY_KEY > :timestamp;',
             'WITH PaginatedResults AS (SELECT D_INV_MEDICAL_HISTORY.*, ROW_NUMBER() OVER (ORDER BY D_INV_MEDICAL_HISTORY.D_INV_MEDICAL_HISTORY_KEY) AS RowNum FROM D_INV_MEDICAL_HISTORY WHERE D_INV_MEDICAL_HISTORY_KEY > :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');



    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_INV_TREATMENT')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_INV_TREATMENT', 'RDB', 'SELECT D_INV_TREATMENT.* FROM D_INV_TREATMENT WHERE D_INV_TREATMENT_KEY > :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM D_INV_TREATMENT WHERE D_INV_TREATMENT_KEY > :timestamp;',
             'WITH PaginatedResults AS (SELECT D_INV_TREATMENT.*, ROW_NUMBER() OVER (ORDER BY D_INV_TREATMENT.D_INV_TREATMENT_KEY) AS RowNum FROM D_INV_TREATMENT WHERE D_INV_TREATMENT_KEY > :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_INV_VACCINATION')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_INV_VACCINATION', 'RDB',
             'SELECT D_INV_VACCINATION.* FROM D_INV_VACCINATION WHERE D_INV_VACCINATION_KEY > :timestamp;',
             NULL,
             'SELECT COUNT(*) FROM D_INV_VACCINATION WHERE D_INV_VACCINATION_KEY > :timestamp;',
             'WITH PaginatedResults AS (SELECT D_INV_VACCINATION.*, ROW_NUMBER() OVER (ORDER BY D_INV_VACCINATION.D_INV_VACCINATION_KEY) AS RowNum FROM D_INV_VACCINATION  WHERE D_INV_VACCINATION_KEY > :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;




IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'HEP_MULTI_VALUE_FIELD')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('HEP_MULTI_VALUE_FIELD', 'RDB', 'SELECT HEP_MULTI_VALUE_FIELD.* FROM HEP_MULTI_VALUE_FIELD', NULL, 'SELECT COUNT(*) FROM HEP_MULTI_VALUE_FIELD', 'WITH PaginatedResults AS (
       SELECT HEP_MULTI_VALUE_FIELD.*,
              ROW_NUMBER() OVER (ORDER BY HEP_MULTI_VALUE_FIELD.HEP_MULTI_VAL_GRP_KEY, HEP_MULTI_VALUE_FIELD.HEP_MULTI_VAL_DATA_KEY) AS RowNum
       FROM HEP_MULTI_VALUE_FIELD
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');

    END;








IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'EDX_ACTIVITY_LOG')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('EDX_ACTIVITY_LOG', 'NBS_ODSE', 'SELECT *
     FROM EDX_ACTIVITY_LOG
     WHERE record_status_time :operator :timestamp;', NULL, 'SELECT COUNT(*)
     FROM EDX_ACTIVITY_LOG
     WHERE record_status_time :operator :timestamp;', 'WITH PaginatedResults AS (
        SELECT *, ROW_NUMBER() OVER (ORDER BY edx_activity_log_uid) AS RowNum
        FROM EDX_ACTIVITY_LOG
        WHERE record_status_time :operator :timestamp
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'EDX_ACTIVITY_DETAIL_LOG')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('EDX_ACTIVITY_DETAIL_LOG', 'NBS_ODSE', 'SELECT logg.record_status_time, detail.*
     FROM EDX_ACTIVITY_DETAIL_LOG AS detail
     INNER JOIN EDX_ACTIVITY_LOG AS logg
     ON detail.edx_activity_log_uid = logg.edx_activity_log_uid
     WHERE logg.record_status_time :operator :timestamp;', NULL, 'SELECT COUNT(*)
     FROM EDX_ACTIVITY_DETAIL_LOG AS detail
     INNER JOIN EDX_ACTIVITY_LOG AS logg
     ON detail.edx_activity_log_uid = logg.edx_activity_log_uid
     WHERE logg.record_status_time :operator :timestamp;', 'WITH PaginatedResults AS (
        SELECT logg.record_status_time, detail.*, ROW_NUMBER() OVER (ORDER BY detail.edx_activity_detail_log_uid) AS RowNum
        FROM EDX_ACTIVITY_DETAIL_LOG AS detail
        INNER JOIN EDX_ACTIVITY_LOG AS logg
        ON detail.edx_activity_log_uid = logg.edx_activity_log_uid
        WHERE logg.record_status_time :operator :timestamp
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;






IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ANTIMICROBIAL_GROUP')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('ANTIMICROBIAL_GROUP', 'RDB', 'SELECT ANTIMICROBIAL_GROUP.* FROM ANTIMICROBIAL_GROUP', NULL, 'SELECT COUNT(*) FROM ANTIMICROBIAL_GROUP', 'WITH PaginatedResults AS (
       SELECT ANTIMICROBIAL_GROUP.*,
              ROW_NUMBER() OVER (ORDER BY ANTIMICROBIAL_GROUP.ANTIMICROBIAL_GRP_KEY) AS RowNum
       FROM ANTIMICROBIAL_GROUP
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');

    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ANTIMICROBIAL')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
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

    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_PCR_SOURCE_GROUP')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_PCR_SOURCE_GROUP', 'RDB', 'SELECT D_PCR_SOURCE_GROUP.* FROM D_PCR_SOURCE_GROUP', NULL, 'SELECT COUNT(*) FROM D_PCR_SOURCE_GROUP', 'WITH PaginatedResults AS (
       SELECT D_PCR_SOURCE_GROUP.*,
              ROW_NUMBER() OVER (ORDER BY D_PCR_SOURCE_GROUP.D_PCR_SOURCE_GROUP_KEY) AS RowNum
       FROM D_PCR_SOURCE_GROUP
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_PCR_SOURCE')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_PCR_SOURCE', 'RDB', 'SELECT D_PCR_SOURCE.* FROM D_PCR_SOURCE', NULL, 'SELECT COUNT(*) FROM D_PCR_SOURCE', 'WITH PaginatedResults AS (
       SELECT D_PCR_SOURCE.*,
              ROW_NUMBER() OVER (ORDER BY D_PCR_SOURCE.D_PCR_SOURCE_KEY, D_PCR_SOURCE.VAR_PAM_UID) AS RowNum
       FROM D_PCR_SOURCE
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_RASH_LOC_GEN_GROUP')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_RASH_LOC_GEN_GROUP', 'RDB', 'SELECT D_RASH_LOC_GEN_GROUP.* FROM D_RASH_LOC_GEN_GROUP', NULL, 'SELECT COUNT(*) FROM D_RASH_LOC_GEN_GROUP', 'WITH PaginatedResults AS (
       SELECT D_RASH_LOC_GEN_GROUP.*,
              ROW_NUMBER() OVER (ORDER BY D_RASH_LOC_GEN_GROUP.D_RASH_LOC_GEN_GROUP_KEY) AS RowNum
       FROM D_RASH_LOC_GEN_GROUP
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_RASH_LOC_GEN')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
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
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'BMIRD_MULTI_VALUE_FIELD_GROUP')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('BMIRD_MULTI_VALUE_FIELD_GROUP', 'RDB', 'SELECT BMIRD_MULTI_VALUE_FIELD_GROUP.* FROM BMIRD_MULTI_VALUE_FIELD_GROUP', NULL, 'SELECT COUNT(*) FROM BMIRD_MULTI_VALUE_FIELD_GROUP', 'WITH PaginatedResults AS (
       SELECT BMIRD_MULTI_VALUE_FIELD_GROUP.*,
              ROW_NUMBER() OVER (ORDER BY BMIRD_MULTI_VALUE_FIELD_GROUP.BMIRD_MULTI_VAL_GRP_KEY) AS RowNum
       FROM BMIRD_MULTI_VALUE_FIELD_GROUP
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'BMIRD_MULTI_VALUE_FIELD')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
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
    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_VAR_PAM')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_VAR_PAM', 'RDB', 'SELECT D_VAR_PAM.* FROM D_VAR_PAM', NULL, 'SELECT COUNT(*) FROM D_VAR_PAM', 'WITH PaginatedResults AS (
       SELECT D_VAR_PAM.*,
              ROW_NUMBER() OVER (ORDER BY D_VAR_PAM.D_VAR_PAM_KEY) AS RowNum
       FROM D_VAR_PAM
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'HEP_MULTI_VALUE_FIELD_GROUP')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('HEP_MULTI_VALUE_FIELD_GROUP', 'RDB', 'SELECT HEP_MULTI_VALUE_FIELD_GROUP.* FROM HEP_MULTI_VALUE_FIELD_GROUP', NULL, 'SELECT COUNT(*) FROM HEP_MULTI_VALUE_FIELD_GROUP', 'WITH PaginatedResults AS (
       SELECT HEP_MULTI_VALUE_FIELD_GROUP.*,
              ROW_NUMBER() OVER (ORDER BY HEP_MULTI_VALUE_FIELD_GROUP.HEP_MULTI_VAL_GRP_KEY) AS RowNum
       FROM HEP_MULTI_VALUE_FIELD_GROUP
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'LDF_DATA')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('LDF_DATA', 'RDB', 'SELECT LDF_DATA.* FROM LDF_DATA', NULL, 'SELECT COUNT(*) FROM LDF_DATA', 'WITH PaginatedResults AS (
       SELECT LDF_DATA.*,
              ROW_NUMBER() OVER (ORDER BY LDF_DATA.LDF_DATA_KEY) AS RowNum
       FROM LDF_DATA
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'LDF_FOODBORNE')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('LDF_FOODBORNE', 'RDB', 'SELECT LDF_FOODBORNE.* FROM LDF_FOODBORNE', NULL, 'SELECT COUNT(*) FROM LDF_FOODBORNE', 'WITH PaginatedResults AS (
       SELECT LDF_FOODBORNE.*,
              ROW_NUMBER() OVER (ORDER BY LDF_FOODBORNE.INVESTIGATION_KEY, LDF_FOODBORNE.PATIENT_KEY) AS RowNum
       FROM LDF_FOODBORNE
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'LDF_GROUP')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('LDF_GROUP', 'RDB', 'SELECT LDF_GROUP.* FROM LDF_GROUP', NULL, 'SELECT COUNT(*) FROM LDF_GROUP', 'WITH PaginatedResults AS (
       SELECT LDF_GROUP.*,
              ROW_NUMBER() OVER (ORDER BY LDF_GROUP.LDF_GROUP_KEY) AS RowNum
       FROM LDF_GROUP
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'NOTIFICATION')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('NOTIFICATION', 'RDB', 'SELECT NOTIFICATION.* FROM NOTIFICATION', NULL, 'SELECT COUNT(*) FROM NOTIFICATION', 'WITH PaginatedResults AS (
       SELECT NOTIFICATION.*,
              ROW_NUMBER() OVER (ORDER BY NOTIFICATION.NOTIFICATION_KEY) AS RowNum
       FROM NOTIFICATION
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PERTUSSIS_SUSPECTED_SOURCE_GRP')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('PERTUSSIS_SUSPECTED_SOURCE_GRP', 'RDB', 'SELECT PERTUSSIS_SUSPECTED_SOURCE_GRP.* FROM PERTUSSIS_SUSPECTED_SOURCE_GRP', NULL, 'SELECT COUNT(*) FROM PERTUSSIS_SUSPECTED_SOURCE_GRP', 'WITH PaginatedResults AS (
       SELECT PERTUSSIS_SUSPECTED_SOURCE_GRP.*,
              ROW_NUMBER() OVER (ORDER BY PERTUSSIS_SUSPECTED_SOURCE_GRP.PERTUSSIS_SUSPECT_SRC_GRP_KEY) AS RowNum
       FROM PERTUSSIS_SUSPECTED_SOURCE_GRP
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PERTUSSIS_SUSPECTED_SOURCE_FLD')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('PERTUSSIS_SUSPECTED_SOURCE_FLD', 'RDB', 'SELECT PERTUSSIS_SUSPECTED_SOURCE_FLD.* FROM PERTUSSIS_SUSPECTED_SOURCE_FLD', NULL, 'SELECT COUNT(*) FROM PERTUSSIS_SUSPECTED_SOURCE_FLD', 'WITH PaginatedResults AS (
       SELECT PERTUSSIS_SUSPECTED_SOURCE_FLD.*,
              ROW_NUMBER() OVER (ORDER BY PERTUSSIS_SUSPECTED_SOURCE_FLD.PERTUSSIS_SUSPECT_SRC_FLD_KEY, PERTUSSIS_SUSPECTED_SOURCE_FLD.PERTUSSIS_SUSPECT_SRC_GRP_KEY) AS RowNum
       FROM PERTUSSIS_SUSPECTED_SOURCE_FLD
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PERTUSSIS_TREATMENT_GROUP')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('PERTUSSIS_TREATMENT_GROUP', 'RDB', 'SELECT PERTUSSIS_TREATMENT_GROUP.* FROM PERTUSSIS_TREATMENT_GROUP', NULL, 'SELECT COUNT(*) FROM PERTUSSIS_TREATMENT_GROUP', 'WITH PaginatedResults AS (
       SELECT PERTUSSIS_TREATMENT_GROUP.*,
              ROW_NUMBER() OVER (ORDER BY PERTUSSIS_TREATMENT_GROUP.PERTUSSIS_TREATMENT_GRP_KEY) AS RowNum
       FROM PERTUSSIS_TREATMENT_GROUP
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PERTUSSIS_TREATMENT_FIELD')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('PERTUSSIS_TREATMENT_FIELD', 'RDB', 'SELECT PERTUSSIS_TREATMENT_FIELD.* FROM PERTUSSIS_TREATMENT_FIELD', NULL, 'SELECT COUNT(*) FROM PERTUSSIS_TREATMENT_FIELD', 'WITH PaginatedResults AS (
       SELECT PERTUSSIS_TREATMENT_FIELD.*,
              ROW_NUMBER() OVER (ORDER BY PERTUSSIS_TREATMENT_FIELD.PERTUSSIS_TREATMENT_FLD_KEY, PERTUSSIS_TREATMENT_FIELD.PERTUSSIS_TREATMENT_GRP_KEY) AS RowNum
       FROM PERTUSSIS_TREATMENT_FIELD
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'TREATMENT')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('TREATMENT', 'RDB', 'SELECT TREATMENT.* FROM TREATMENT', NULL, 'SELECT COUNT(*) FROM TREATMENT', 'WITH PaginatedResults AS (
       SELECT TREATMENT.*,
              ROW_NUMBER() OVER (ORDER BY TREATMENT.TREATMENT_KEY) AS RowNum
       FROM TREATMENT
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'TREATMENT_EVENT')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('TREATMENT_EVENT', 'RDB', 'SELECT TREATMENT_EVENT.* FROM TREATMENT_EVENT', NULL, 'SELECT COUNT(*) FROM TREATMENT_EVENT', 'WITH PaginatedResults AS (
       SELECT TREATMENT_EVENT.*,
              ROW_NUMBER() OVER (ORDER BY TREATMENT_EVENT.TREATMENT_DT_KEY, TREATMENT_EVENT.TREATMENT_PROVIDING_ORG_KEY, TREATMENT_EVENT.PATIENT_KEY, TREATMENT_EVENT.TREATMENT_KEY, TREATMENT_EVENT.MORB_RPT_KEY, TREATMENT_EVENT.TREATMENT_PHYSICIAN_KEY,TREATMENT_EVENT.INVESTIGATION_KEY, TREATMENT_EVENT.CONDITION_KEY, TREATMENT_EVENT.LDF_GROUP_KEY) AS RowNum
FROM TREATMENT_EVENT
)
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow');

    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'VAR_PAM_LDF')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('VAR_PAM_LDF', 'RDB', 'SELECT VAR_PAM_LDF.* FROM VAR_PAM_LDF', NULL, 'SELECT COUNT(*) FROM VAR_PAM_LDF', 'WITH PaginatedResults AS (
        SELECT  *
        FROM VAR_PAM_LDF
    ),
    NumberedResults AS (
        SELECT *, ROW_NUMBER() OVER (ORDER BY INVESTIGATION_KEY) AS RowNum
        FROM PaginatedResults
    )
    SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'STD_HIV_DATAMART')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('STD_HIV_DATAMART', 'RDB', 'SELECT STD_HIV_DATAMART.*
     FROM STD_HIV_DATAMART
     JOIN INVESTIGATION ON STD_HIV_DATAMART.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
     WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', NULL, 'SELECT COUNT(*)
     FROM STD_HIV_DATAMART
     JOIN INVESTIGATION ON STD_HIV_DATAMART.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
     WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
             'WITH PaginatedResults AS (
               SELECT STD_HIV_DATAMART.*,
                      ROW_NUMBER() OVER (
                          ORDER BY
                              STD_HIV_DATAMART.INVESTIGATION_KEY
                      ) AS RowNum
               FROM STD_HIV_DATAMART
               JOIN INVESTIGATION ON STD_HIV_DATAMART.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
               WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp
           )
           SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'CONDITION')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('CONDITION', 'RDB', 'SELECT * FROM CONDITION;', NULL, 'SELECT COUNT(*) FROM CONDITION;', 'WITH PaginatedResults AS (
        SELECT DISTINCT *
        FROM CONDITION
    ),
    NumberedResults AS (
        SELECT *, ROW_NUMBER() OVER (ORDER BY CONDITION_KEY) AS RowNum
        FROM PaginatedResults
    )
    SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'CONFIRMATION_METHOD')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('CONFIRMATION_METHOD', 'RDB', 'SELECT CM.* FROM CONFIRMATION_METHOD CM', NULL,
             'SELECT COUNT(CM.CONFIRMATION_METHOD_KEY) FROM CONFIRMATION_METHOD CM',
             '');
    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'Rdb_Date')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('Rdb_Date', 'RDB', 'SELECT * FROM Rdb_Date;', NULL, 'SELECT COUNT(*) FROM Rdb_Date;', 'WITH PaginatedResults AS (
        SELECT DISTINCT *
        FROM Rdb_Date
    ),
    NumberedResults AS (
        SELECT *, ROW_NUMBER() OVER (ORDER BY DATE_KEY) AS RowNum
        FROM PaginatedResults
    )
    SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'NRT_OBSERVATION')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('NRT_OBSERVATION', 'RDB_MODERN', 'SELECT * FROM nrt_observation WHERE refresh_datetime :operator :timestamp;', NULL,
             'SELECT COUNT(*) FROM nrt_observation WHERE refresh_datetime :operator :timestamp;', 'WITH PaginatedResults AS (
        SELECT DISTINCT *
        FROM nrt_observation
        WHERE refresh_datetime :operator :timestamp
    ),
    NumberedResults AS (
        SELECT *, ROW_NUMBER() OVER (
            ORDER BY
                refresh_datetime ASC,
                observation_uid
        ) AS RowNum
        FROM PaginatedResults
    )
    SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'NRT_OBSERVATION_CODED')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('NRT_OBSERVATION_CODED', 'RDB_MODERN', 'SELECT *
     FROM nrt_observation_coded noc
     JOIN nrt_observation no
         ON noc.observation_uid = no.observation_uid
     WHERE noc.refresh_datetime :operator :timestamp;', NULL, 'SELECT COUNT(*)
     FROM nrt_observation_coded noc
     JOIN nrt_observation no
         ON noc.observation_uid = no.observation_uid
     WHERE noc.refresh_datetime :operator :timestamp;', 'WITH PaginatedResults AS (
        SELECT noc.*,
               ROW_NUMBER() OVER (ORDER BY noc.observation_uid ASC) AS RowNum
        FROM nrt_observation_coded noc
        JOIN nrt_observation no
            ON noc.observation_uid = no.observation_uid
        WHERE noc.refresh_datetime :operator :timestamp
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'CONDITION_CODE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('CONDITION_CODE', 'SRTE', 'SELECT * FROM CONDITION_CODE', NULL, 'SELECT COUNT(*) FROM CONDITION_CODE;', 'WITH PaginatedResults AS (
        SELECT DISTINCT *
        FROM CONDITION_CODE
    ),
    NumberedResults AS (
        SELECT *, ROW_NUMBER() OVER (ORDER BY condition_cd) AS RowNum
        FROM PaginatedResults
    )
    SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'CODE_TO_CONDITION')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('CODE_TO_CONDITION', 'SRTE', 'SELECT * FROM CODE_TO_CONDITION', NULL, 'SELECT COUNT(*) FROM CODE_TO_CONDITION;', '');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'Program_area_code')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('Program_area_code', 'SRTE', 'SELECT * FROM Program_area_code;', NULL, 'SELECT COUNT(*) FROM Program_area_code;', 'WITH PaginatedResults AS (
        SELECT DISTINCT *
        FROM Program_area_code
    ),
    NumberedResults AS (
        SELECT *, ROW_NUMBER() OVER (ORDER BY prog_area_cd) AS RowNum
        FROM PaginatedResults
    )
    SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'JURISDICTION_CODE')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('JURISDICTION_CODE', 'SRTE', 'SELECT * FROM JURISDICTION_CODE;', NULL, 'SELECT COUNT(*) FROM JURISDICTION_CODE;', 'WITH PaginatedResults AS (
        SELECT DISTINCT *
        FROM JURISDICTION_CODE
    ),
    NumberedResults AS (
        SELECT *, ROW_NUMBER() OVER (ORDER BY code) AS RowNum
        FROM PaginatedResults
    )
    SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'COVID_CASE_DATAMART')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('COVID_CASE_DATAMART', 'RDB', 'SELECT COVID_CASE_DATAMART.*
     FROM COVID_CASE_DATAMART
     WHERE COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp;', NULL, 'SELECT COUNT(*)
     FROM COVID_CASE_DATAMART
     WHERE COVID_CASE_DATAMART.ADD_TIME :operator :timestamp
     OR COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp;', 'WITH PaginatedResults AS (
        SELECT DISTINCT COVID_CASE_DATAMART.*
        FROM COVID_CASE_DATAMART
        WHERE COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp
    ),
    NumberedResults AS (
        SELECT *, ROW_NUMBER() OVER (
            ORDER BY
                LAST_CHG_TIME ASC,
                COVID_CASE_DATAMART_KEY
        ) AS RowNum
        FROM PaginatedResults
    )
    SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'COVID_LAB_DATAMART')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('COVID_LAB_DATAMART', 'RDB', 'SELECT COVID_LAB_DATAMART.*
     FROM COVID_LAB_DATAMART
     WHERE COVID_LAB_DATAMART.Lab_Update_Dt :operator :timestamp;', NULL, 'SELECT COUNT(*)
     FROM COVID_LAB_DATAMART
     WHERE COVID_LAB_DATAMART.Lab_Update_Dt :operator :timestamp;', 'WITH PaginatedResults AS (
        SELECT DISTINCT COVID_LAB_DATAMART.*
        FROM COVID_LAB_DATAMART
        WHERE COVID_LAB_DATAMART.Lab_Update_Dt :operator :timestamp
    ),
    NumberedResults AS (
        SELECT *, ROW_NUMBER() OVER (
            ORDER BY
                Lab_Update_Dt ASC,
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
            ('COVID_LAB_CELR_DATAMART', 'RDB', 'SELECT COVID_LAB_CELR_DATAMART.* FROM COVID_LAB_CELR_DATAMART', NULL, 'SELECT COUNT(*)
FROM COVID_LAB_CELR_DATAMART', '')
        ;

    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ENTITY')
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




IF
NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ROLE')
BEGIN

INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                            query_with_pagination)
VALUES
    ('ROLE', 'NBS_ODSE', 'SELECT ROLE.*
     FROM ROLE
  	INNER JOIN ENTITY
        ON ROLE.subject_entity_uid = ENTITY.entity_uid
	INNER JOIN PARTICIPATION
        ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
    INNER JOIN OBSERVATION
 	ON OBSERVATION.OBSERVATION_UID = PARTICIPATION.act_uid
    WHERE (OBSERVATION.add_time :operator :timestamp
            OR OBSERVATION.last_chg_time :operator :timestamp);',
    NULL,
    'SELECT COUNT(DISTINCT SUB.subject_entity_uid)
    FROM
    (SELECT ROLE.*
        FROM ROLE
        INNER JOIN ENTITY
            ON ROLE.subject_entity_uid = ENTITY.entity_uid
        INNER JOIN PARTICIPATION
            ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
        INNER JOIN OBSERVATION
            ON OBSERVATION.OBSERVATION_UID = PARTICIPATION.act_uid
        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)) SUB;',

    'WITH PaginatedResults AS (
        SELECT ROLE.*,
               COALESCE(OBSERVATION.add_time, OBSERVATION.last_chg_time) AS latest_timestamp
        FROM ROLE
        INNER JOIN ENTITY
        ON ROLE.subject_entity_uid = ENTITY.entity_uid
		INNER JOIN PARTICIPATION
	        ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
	    INNER JOIN OBSERVATION
	 	ON OBSERVATION.OBSERVATION_UID = PARTICIPATION.act_uid
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

IF
NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PERSON')
BEGIN

INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                            query_with_pagination)
VALUES
    ('PERSON', 'NBS_ODSE',
    'SELECT DISTINCT PERSON.person_uid,
   PERSON.add_time,
   1 AS GroupOrder
FROM PERSON
INNER JOIN ENTITY
ON ENTITY.entity_uid = PERSON.person_uid
INNER JOIN PARTICIPATION
ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
INNER JOIN OBSERVATION
ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time  :operator :timestamp) AND person_parent_uid = person_uid

UNION ALL

SELECT DISTINCT PERSON.person_uid,
   PERSON.add_time,
   2 AS GroupOrder
FROM PERSON
INNER JOIN ENTITY
ON ENTITY.entity_uid = PERSON.person_uid
INNER JOIN PARTICIPATION
ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
INNER JOIN OBSERVATION
ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp) AND person_parent_uid != person_uid; ',
    NULL,
    'SELECT COUNT(*)
    FROM (
        SELECT DISTINCT PERSON.person_uid
        FROM PERSON
        INNER JOIN ENTITY
            ON ENTITY.entity_uid = PERSON.person_uid
        INNER JOIN PARTICIPATION
            ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
        INNER JOIN OBSERVATION
            ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
          AND PERSON.person_parent_uid = PERSON.person_uid

        UNION ALL

        SELECT DISTINCT PERSON.person_uid
        FROM PERSON
        INNER JOIN ENTITY
            ON ENTITY.entity_uid = PERSON.person_uid
        INNER JOIN PARTICIPATION
            ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
        INNER JOIN OBSERVATION
            ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
          AND PERSON.person_parent_uid != PERSON.person_uid
    ) AS Combined;',
    'WITH OrderedPersons AS (
        SELECT DISTINCT PERSON.person_uid,
               PERSON.add_time,
               1 AS GroupOrder
        FROM PERSON
        INNER JOIN ENTITY
            ON ENTITY.entity_uid = PERSON.person_uid
        INNER JOIN PARTICIPATION
            ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
        INNER JOIN OBSERVATION
            ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
          AND PERSON.person_parent_uid = PERSON.person_uid

        UNION ALL

        SELECT DISTINCT PERSON.person_uid,
               PERSON.add_time,
               2 AS GroupOrder
        FROM PERSON
        INNER JOIN ENTITY
            ON ENTITY.entity_uid = PERSON.person_uid
        INNER JOIN PARTICIPATION
            ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
        INNER JOIN OBSERVATION
            ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
          AND PERSON.person_parent_uid != PERSON.person_uid
    ),
    NumberedResults AS (
        SELECT person_uid,
               ROW_NUMBER() OVER (ORDER BY GroupOrder, add_time ASC, person_uid) AS RowNum
        FROM OrderedPersons
    )
    SELECT P.*
    FROM PERSON P
    INNER JOIN NumberedResults NR
        ON P.person_uid = NR.person_uid
    WHERE NR.RowNum BETWEEN :startRow AND :endRow;
    ');

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
ON ACT.act_uid = ActResults.act_uid;', NULL, 'SELECT COUNT(*) FROM (
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
) AS ACT_COUNT;', 'WITH ActResults AS (
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


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'OBSERVATION')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('OBSERVATION', 'NBS_ODSE', 'WITH ObservationResults AS (
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
ON OBSERVATION.observation_uid = ObservationResults.observation_uid;', NULL, 'SELECT COUNT(*) FROM (
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
', 'WITH ObservationResults AS (
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



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'Act_relationship')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('Act_relationship', 'NBS_ODSE', 'SELECT DISTINCT Act_relationship.*
     FROM Act_relationship
     INNER JOIN OBSERVATION
         ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
     INNER JOIN ACT AS ACT_SOURCE
         ON ACT_SOURCE.act_uid = Act_relationship.source_act_uid
     INNER JOIN ACT AS ACT_TARGET
         ON ACT_TARGET.act_uid = Act_relationship.target_act_uid
     WHERE (OBSERVATION.add_time :operator :timestamp
            OR OBSERVATION.last_chg_time :operator :timestamp);', NULL, 'SELECT COUNT(*) FROM (
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
    ) AS ACT_RELATIONSHIP_COUNT;', 'WITH PaginatedResults AS (
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



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'D_PROVIDER')
    BEGIN
        INSERT INTO [dbo].[data_sync_config]
        (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
        VALUES
            ('D_PROVIDER', 'RDB', 'SELECT * FROM D_PROVIDER WHERE D_PROVIDER.PROVIDER_LAST_CHANGE_TIME :operator :timestamp;', 'SELECT * FROM D_PROVIDER WHERE D_PROVIDER.PROVIDER_LAST_CHANGE_TIME IS NULL;', 'SELECT COUNT(*) FROM D_PROVIDER WHERE D_PROVIDER.PROVIDER_LAST_CHANGE_TIME :operator :timestamp;', 'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY D_PROVIDER.PROVIDER_KEY) AS RowNum FROM D_PROVIDER WHERE D_PROVIDER.PROVIDER_LAST_CHANGE_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');
    END;
