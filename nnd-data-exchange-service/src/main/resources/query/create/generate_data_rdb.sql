INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination) VALUES
    ('CONDITION', 'RDB', 'SELECT * FROM CONDITION;', NULL, 'SELECT COUNT(*) FROM CONDITION;', 'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum FROM CONDITION) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),
    ('D_PATIENT', 'RDB', 'SELECT * FROM D_PATIENT WHERE D_PATIENT.PATIENT_LAST_CHANGE_TIME :operator :timestamp;', 'SELECT * FROM D_PATIENT WHERE D_PATIENT.PATIENT_LAST_CHANGE_TIME IS NULL;', 'SELECT COUNT(*) FROM D_PATIENT WHERE D_PATIENT.PATIENT_LAST_CHANGE_TIME :operator :timestamp;', 'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY D_PATIENT.PATIENT_LAST_CHANGE_TIME ASC) AS RowNum FROM D_PATIENT WHERE D_PATIENT.PATIENT_LAST_CHANGE_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),
    ('INVESTIGATION', 'RDB', 'SELECT * FROM INVESTIGATION WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', 'SELECT * FROM INVESTIGATION WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;', 'SELECT COUNT(*) FROM INVESTIGATION WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', 'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM INVESTIGATION WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),
    ('CASE_COUNT', 'RDB', 'SELECT A.* FROM CASE_COUNT as A INNER JOIN INVESTIGATION as B ON A.INVESTIGATION_KEY = B.INVESTIGATION_KEY WHERE B.ADD_TIME :operator :timestamp OR B.LAST_CHG_TIME :operator :timestamp;', NULL, 'SELECT COUNT(*) FROM CASE_COUNT as A INNER JOIN INVESTIGATION as B ON A.INVESTIGATION_KEY = B.INVESTIGATION_KEY WHERE B.ADD_TIME :operator :timestamp OR B.LAST_CHG_TIME :operator :timestamp;', 'WITH PaginatedResults AS (SELECT A.*, ROW_NUMBER() OVER (ORDER BY CASE WHEN B.ADD_TIME :operator :timestamp THEN B.ADD_TIME ELSE B.LAST_CHG_TIME END ASC) AS RowNum FROM CASE_COUNT as A INNER JOIN INVESTIGATION as B ON A.INVESTIGATION_KEY = B.INVESTIGATION_KEY WHERE B.ADD_TIME :operator :timestamp OR B.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),
    ('BMIRD_CASE', 'RDB', 'SELECT BMIRD_CASE.* FROM BMIRD_CASE JOIN INVESTIGATION ON BMIRD_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', NULL, 'SELECT COUNT(*) FROM BMIRD_CASE JOIN INVESTIGATION ON BMIRD_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', 'WITH PaginatedResults AS (SELECT BMIRD_CASE.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM BMIRD_CASE JOIN INVESTIGATION ON BMIRD_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),
    ('RUBELLA_CASE', 'RDB', 'SELECT RUBELLA_CASE.* FROM RUBELLA_CASE JOIN INVESTIGATION ON RUBELLA_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', NULL, 'SELECT COUNT(*) FROM RUBELLA_CASE JOIN INVESTIGATION ON RUBELLA_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', 'WITH PaginatedResults AS (SELECT RUBELLA_CASE.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM RUBELLA_CASE JOIN INVESTIGATION ON RUBELLA_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),
    ('PERTUSSIS_CASE', 'RDB', 'SELECT PERTUSSIS_CASE.* FROM PERTUSSIS_CASE JOIN INVESTIGATION ON PERTUSSIS_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', NULL, 'SELECT COUNT(*) FROM PERTUSSIS_CASE JOIN INVESTIGATION ON PERTUSSIS_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', 'WITH PaginatedResults AS (SELECT PERTUSSIS_CASE.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM PERTUSSIS_CASE JOIN INVESTIGATION ON PERTUSSIS_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),
    ('HEPATITIS_CASE', 'RDB', 'SELECT HEPATITIS_CASE.* FROM HEPATITIS_CASE JOIN INVESTIGATION ON HEPATITIS_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', NULL, 'SELECT COUNT(*) FROM HEPATITIS_CASE JOIN INVESTIGATION ON HEPATITIS_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', 'WITH PaginatedResults AS (SELECT HEPATITIS_CASE.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM HEPATITIS_CASE JOIN INVESTIGATION ON HEPATITIS_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),
    ('GENERIC_CASE', 'RDB', 'SELECT GENERIC_CASE.* FROM GENERIC_CASE JOIN INVESTIGATION ON GENERIC_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', NULL, 'SELECT COUNT(*) FROM GENERIC_CASE JOIN INVESTIGATION ON GENERIC_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;', 'WITH PaginatedResults AS (SELECT GENERIC_CASE.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM GENERIC_CASE JOIN INVESTIGATION ON GENERIC_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;')
;

INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination) VALUES
    ('F_VAR_PAM', 'RDB',
    'SELECT F_VAR_PAM.* FROM F_VAR_PAM JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM F_VAR_PAM JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT F_VAR_PAM.*, ROW_NUMBER() OVER (ORDER BY CASE WHEN INVESTIGATION.ADD_TIME :operator :timestamp THEN INVESTIGATION.ADD_TIME ELSE INVESTIGATION.LAST_CHG_TIME END ASC) AS RowNum FROM F_VAR_PAM JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('LDF_DATA', 'RDB',
    'SELECT LDF_DATA.* FROM LDF_DATA JOIN LDF_GROUP ON LDF_DATA.LDF_GROUP_KEY = LDF_GROUP.LDF_GROUP_KEY JOIN INVESTIGATION ON LDF_GROUP.BUSINESS_OBJECT_UID = INVESTIGATION.CASE_UID WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT LDF_DATA.* FROM LDF_DATA JOIN LDF_GROUP ON LDF_DATA.LDF_GROUP_KEY = LDF_GROUP.LDF_GROUP_KEY JOIN INVESTIGATION ON LDF_GROUP.BUSINESS_OBJECT_UID = INVESTIGATION.CASE_UID WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM LDF_DATA JOIN LDF_GROUP ON LDF_DATA.LDF_GROUP_KEY = LDF_GROUP.LDF_GROUP_KEY JOIN INVESTIGATION ON LDF_GROUP.BUSINESS_OBJECT_UID = INVESTIGATION.CASE_UID WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT LDF_DATA.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM LDF_DATA JOIN LDF_GROUP ON LDF_DATA.LDF_GROUP_KEY = LDF_GROUP.LDF_GROUP_KEY JOIN INVESTIGATION ON LDF_GROUP.BUSINESS_OBJECT_UID = INVESTIGATION.CASE_UID WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('LDF_FOODBORNE', 'RDB',
    'SELECT LDF_FOODBORNE.* FROM LDF_FOODBORNE JOIN INVESTIGATION ON LDF_FOODBORNE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM LDF_FOODBORNE JOIN INVESTIGATION ON LDF_FOODBORNE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT LDF_FOODBORNE.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM LDF_FOODBORNE JOIN INVESTIGATION ON LDF_FOODBORNE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('NOTIFICATION_EVENT', 'RDB',
    'SELECT NOTIFICATION_EVENT.* FROM NOTIFICATION_EVENT JOIN INVESTIGATION ON NOTIFICATION_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT NOTIFICATION_EVENT.* FROM NOTIFICATION_EVENT JOIN INVESTIGATION ON NOTIFICATION_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM NOTIFICATION_EVENT JOIN INVESTIGATION ON NOTIFICATION_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT NOTIFICATION_EVENT.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM NOTIFICATION_EVENT JOIN INVESTIGATION ON NOTIFICATION_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_INV_ADMINISTRATIVE', 'RDB',
    'SELECT D_INV_ADMINISTRATIVE.* FROM D_INV_ADMINISTRATIVE JOIN F_PAGE_CASE ON D_INV_ADMINISTRATIVE.D_INV_ADMINISTRATIVE_KEY = F_PAGE_CASE.D_INV_ADMINISTRATIVE_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT D_INV_ADMINISTRATIVE.* FROM D_INV_ADMINISTRATIVE JOIN F_PAGE_CASE ON D_INV_ADMINISTRATIVE.D_INV_ADMINISTRATIVE_KEY = F_PAGE_CASE.D_INV_ADMINISTRATIVE_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM D_INV_ADMINISTRATIVE JOIN F_PAGE_CASE ON D_INV_ADMINISTRATIVE.D_INV_ADMINISTRATIVE_KEY = F_PAGE_CASE.D_INV_ADMINISTRATIVE_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_INV_ADMINISTRATIVE.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_INV_ADMINISTRATIVE JOIN F_PAGE_CASE ON D_INV_ADMINISTRATIVE.D_INV_ADMINISTRATIVE_KEY = F_PAGE_CASE.D_INV_ADMINISTRATIVE_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_INV_EPIDEMIOLOGY', 'RDB',
    'SELECT D_INV_EPIDEMIOLOGY.* FROM D_INV_EPIDEMIOLOGY JOIN F_PAGE_CASE ON D_INV_EPIDEMIOLOGY.D_INV_EPIDEMIOLOGY_KEY = F_PAGE_CASE.D_INV_EPIDEMIOLOGY_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT D_INV_EPIDEMIOLOGY.* FROM D_INV_EPIDEMIOLOGY JOIN F_PAGE_CASE ON D_INV_EPIDEMIOLOGY.D_INV_EPIDEMIOLOGY_KEY = F_PAGE_CASE.D_INV_EPIDEMIOLOGY_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM D_INV_EPIDEMIOLOGY JOIN F_PAGE_CASE ON D_INV_EPIDEMIOLOGY.D_INV_EPIDEMIOLOGY_KEY = F_PAGE_CASE.D_INV_EPIDEMIOLOGY_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_INV_EPIDEMIOLOGY.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_INV_EPIDEMIOLOGY JOIN F_PAGE_CASE ON D_INV_EPIDEMIOLOGY.D_INV_EPIDEMIOLOGY_KEY = F_PAGE_CASE.D_INV_EPIDEMIOLOGY_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_INV_HIV', 'RDB',
    'SELECT D_INV_HIV.* FROM D_INV_HIV JOIN F_PAGE_CASE ON D_INV_HIV.D_INV_HIV_KEY = F_PAGE_CASE.D_INV_HIV_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT D_INV_HIV.* FROM D_INV_HIV JOIN F_PAGE_CASE ON D_INV_HIV.D_INV_HIV_KEY = F_PAGE_CASE.D_INV_HIV_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM D_INV_HIV JOIN F_PAGE_CASE ON D_INV_HIV.D_INV_HIV_KEY = F_PAGE_CASE.D_INV_HIV_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_INV_HIV.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_INV_HIV JOIN F_PAGE_CASE ON D_INV_HIV.D_INV_HIV_KEY = F_PAGE_CASE.D_INV_HIV_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),


    ('D_INV_LAB_FINDING', 'RDB',
    'SELECT D_INV_LAB_FINDING.* FROM D_INV_LAB_FINDING JOIN F_PAGE_CASE ON D_INV_LAB_FINDING.D_INV_LAB_FINDING_KEY = F_PAGE_CASE.D_INV_LAB_FINDING_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT D_INV_LAB_FINDING.* FROM D_INV_LAB_FINDING JOIN F_PAGE_CASE ON D_INV_LAB_FINDING.D_INV_LAB_FINDING_KEY = F_PAGE_CASE.D_INV_LAB_FINDING_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM D_INV_LAB_FINDING JOIN F_PAGE_CASE ON D_INV_LAB_FINDING.D_INV_LAB_FINDING_KEY = F_PAGE_CASE.D_INV_LAB_FINDING_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_INV_LAB_FINDING.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_INV_LAB_FINDING JOIN F_PAGE_CASE ON D_INV_LAB_FINDING.D_INV_LAB_FINDING_KEY = F_PAGE_CASE.D_INV_LAB_FINDING_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_INV_MEDICAL_HISTORY', 'RDB',
    'SELECT D_INV_MEDICAL_HISTORY.* FROM D_INV_MEDICAL_HISTORY JOIN F_PAGE_CASE ON D_INV_MEDICAL_HISTORY.D_INV_MEDICAL_HISTORY_KEY = F_PAGE_CASE.D_INV_MEDICAL_HISTORY_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT D_INV_MEDICAL_HISTORY.* FROM D_INV_MEDICAL_HISTORY JOIN F_PAGE_CASE ON D_INV_MEDICAL_HISTORY.D_INV_MEDICAL_HISTORY_KEY = F_PAGE_CASE.D_INV_MEDICAL_HISTORY_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM D_INV_MEDICAL_HISTORY JOIN F_PAGE_CASE ON D_INV_MEDICAL_HISTORY.D_INV_MEDICAL_HISTORY_KEY = F_PAGE_CASE.D_INV_MEDICAL_HISTORY_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_INV_MEDICAL_HISTORY.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_INV_MEDICAL_HISTORY JOIN F_PAGE_CASE ON D_INV_MEDICAL_HISTORY.D_INV_MEDICAL_HISTORY_KEY = F_PAGE_CASE.D_INV_MEDICAL_HISTORY_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),


    ('D_INV_RISK_FACTOR', 'RDB',
    'SELECT D_INV_RISK_FACTOR.* FROM D_INV_RISK_FACTOR JOIN F_PAGE_CASE ON D_INV_RISK_FACTOR.D_INV_RISK_FACTOR_KEY = F_PAGE_CASE.D_INV_RISK_FACTOR_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT D_INV_RISK_FACTOR.* FROM D_INV_RISK_FACTOR JOIN F_PAGE_CASE ON D_INV_RISK_FACTOR.D_INV_RISK_FACTOR_KEY = F_PAGE_CASE.D_INV_RISK_FACTOR_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM D_INV_RISK_FACTOR JOIN F_PAGE_CASE ON D_INV_RISK_FACTOR.D_INV_RISK_FACTOR_KEY = F_PAGE_CASE.D_INV_RISK_FACTOR_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_INV_RISK_FACTOR.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_INV_RISK_FACTOR JOIN F_PAGE_CASE ON D_INV_RISK_FACTOR.D_INV_RISK_FACTOR_KEY = F_PAGE_CASE.D_INV_RISK_FACTOR_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_INV_TREATMENT', 'RDB',
    'SELECT D_INV_TREATMENT.* FROM D_INV_TREATMENT JOIN F_PAGE_CASE ON D_INV_TREATMENT.D_INV_TREATMENT_KEY = F_PAGE_CASE.D_INV_TREATMENT_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT D_INV_TREATMENT.* FROM D_INV_TREATMENT JOIN F_PAGE_CASE ON D_INV_TREATMENT.D_INV_TREATMENT_KEY = F_PAGE_CASE.D_INV_TREATMENT_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM D_INV_TREATMENT JOIN F_PAGE_CASE ON D_INV_TREATMENT.D_INV_TREATMENT_KEY = F_PAGE_CASE.D_INV_TREATMENT_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_INV_TREATMENT.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_INV_TREATMENT JOIN F_PAGE_CASE ON D_INV_TREATMENT.D_INV_TREATMENT_KEY = F_PAGE_CASE.D_INV_TREATMENT_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_INV_VACCINATION', 'RDB',
    'SELECT D_INV_VACCINATION.* FROM D_INV_VACCINATION JOIN F_PAGE_CASE ON D_INV_VACCINATION.D_INV_VACCINATION_KEY = F_PAGE_CASE.D_INV_VACCINATION_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT D_INV_VACCINATION.* FROM D_INV_VACCINATION JOIN F_PAGE_CASE ON D_INV_VACCINATION.D_INV_VACCINATION_KEY = F_PAGE_CASE.D_INV_VACCINATION_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM D_INV_VACCINATION JOIN F_PAGE_CASE ON D_INV_VACCINATION.D_INV_VACCINATION_KEY = F_PAGE_CASE.D_INV_VACCINATION_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_INV_VACCINATION.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_INV_VACCINATION JOIN F_PAGE_CASE ON D_INV_VACCINATION.D_INV_VACCINATION_KEY = F_PAGE_CASE.D_INV_VACCINATION_KEY JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),


    ('F_INTERVIEW_CASE', 'RDB',
    'SELECT F_INTERVIEW_CASE.* FROM F_INTERVIEW_CASE JOIN INVESTIGATION ON F_INTERVIEW_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY JOIN D_INTERVIEW ON F_INTERVIEW_CASE.D_INTERVIEW_KEY = D_INTERVIEW.D_INTERVIEW_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp OR D_INTERVIEW.ADD_TIME :operator :timestamp OR D_INTERVIEW.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM F_INTERVIEW_CASE JOIN INVESTIGATION ON F_INTERVIEW_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY JOIN D_INTERVIEW ON F_INTERVIEW_CASE.D_INTERVIEW_KEY = D_INTERVIEW.D_INTERVIEW_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp OR D_INTERVIEW.ADD_TIME :operator :timestamp OR D_INTERVIEW.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT F_INTERVIEW_CASE.*, ROW_NUMBER() OVER (ORDER BY CASE WHEN D_INTERVIEW.ADD_TIME :operator :timestamp THEN D_INTERVIEW.ADD_TIME ELSE D_INTERVIEW.LAST_CHG_TIME END ASC) AS RowNum FROM F_INTERVIEW_CASE JOIN INVESTIGATION ON F_INTERVIEW_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY JOIN D_INTERVIEW ON F_INTERVIEW_CASE.D_INTERVIEW_KEY = D_INTERVIEW.D_INTERVIEW_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp OR D_INTERVIEW.ADD_TIME :operator :timestamp OR D_INTERVIEW.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;')
;


INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination) VALUES
    ('F_PAGE_CASE', 'RDB',
    'SELECT F_PAGE_CASE.* FROM F_PAGE_CASE JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM F_PAGE_CASE JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT F_PAGE_CASE.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM F_PAGE_CASE JOIN INVESTIGATION ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('F_STD_PAGE_CASE', 'RDB',
    'SELECT F_STD_PAGE_CASE.* FROM F_STD_PAGE_CASE JOIN INVESTIGATION ON F_STD_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM F_STD_PAGE_CASE JOIN INVESTIGATION ON F_STD_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT F_STD_PAGE_CASE.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM F_STD_PAGE_CASE JOIN INVESTIGATION ON F_STD_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('TREATMENT', 'RDB',
    'SELECT TREATMENT.* FROM TREATMENT JOIN TREATMENT_EVENT ON TREATMENT.TREATMENT_KEY = TREATMENT_EVENT.TREATMENT_KEY JOIN INVESTIGATION ON TREATMENT_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT TREATMENT.* FROM TREATMENT JOIN TREATMENT_EVENT ON TREATMENT.TREATMENT_KEY = TREATMENT_EVENT.TREATMENT_KEY JOIN INVESTIGATION ON TREATMENT_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM TREATMENT JOIN TREATMENT_EVENT ON TREATMENT.TREATMENT_KEY = TREATMENT_EVENT.TREATMENT_KEY JOIN INVESTIGATION ON TREATMENT_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT TREATMENT.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM TREATMENT JOIN TREATMENT_EVENT ON TREATMENT.TREATMENT_KEY = TREATMENT_EVENT.TREATMENT_KEY JOIN INVESTIGATION ON TREATMENT_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('TREATMENT_EVENT', 'RDB',
    'SELECT TREATMENT_EVENT.* FROM TREATMENT_EVENT JOIN INVESTIGATION ON TREATMENT_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM TREATMENT_EVENT JOIN INVESTIGATION ON TREATMENT_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT TREATMENT_EVENT.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM TREATMENT_EVENT JOIN INVESTIGATION ON TREATMENT_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_ORGANIZATION', 'RDB',
    'SELECT * FROM D_ORGANIZATION WHERE D_ORGANIZATION.ORGANIZATION_LAST_CHANGE_TIME :operator :timestamp;',
    'SELECT * FROM D_ORGANIZATION WHERE D_ORGANIZATION.ORGANIZATION_LAST_CHANGE_TIME IS NULL;',
    'SELECT COUNT(*) FROM D_ORGANIZATION WHERE D_ORGANIZATION.ORGANIZATION_LAST_CHANGE_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY D_ORGANIZATION.ORGANIZATION_LAST_CHANGE_TIME ASC) AS RowNum FROM D_ORGANIZATION WHERE D_ORGANIZATION.ORGANIZATION_LAST_CHANGE_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_PROVIDER', 'RDB',
    'SELECT * FROM D_PROVIDER WHERE D_PROVIDER.PROVIDER_LAST_CHANGE_TIME :operator :timestamp;',
    'SELECT * FROM D_PROVIDER WHERE D_PROVIDER.PROVIDER_LAST_CHANGE_TIME IS NULL;',
    'SELECT COUNT(*) FROM D_PROVIDER WHERE D_PROVIDER.PROVIDER_LAST_CHANGE_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY D_PROVIDER.PROVIDER_LAST_CHANGE_TIME ASC) AS RowNum FROM D_PROVIDER WHERE D_PROVIDER.PROVIDER_LAST_CHANGE_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('NOTIFICATION', 'RDB',
    'SELECT N.* FROM NOTIFICATION N WHERE (N.NOTIFICATION_LAST_CHANGE_TIME :operator :timestamp) OR (N.NOTIFICATION_LAST_CHANGE_TIME IS NULL AND EXISTS (SELECT 1 FROM NOTIFICATION_EVENT NE JOIN INVESTIGATION I ON NE.INVESTIGATION_KEY = I.INVESTIGATION_KEY WHERE NE.NOTIFICATION_KEY = N.NOTIFICATION_KEY AND I.LAST_CHG_TIME :operator :timestamp));',
    NULL,
    'SELECT COUNT(*) FROM NOTIFICATION N WHERE (N.NOTIFICATION_LAST_CHANGE_TIME :operator :timestamp) OR (N.NOTIFICATION_LAST_CHANGE_TIME IS NULL AND EXISTS (SELECT 1 FROM NOTIFICATION_EVENT NE JOIN INVESTIGATION I ON NE.INVESTIGATION_KEY = I.INVESTIGATION_KEY WHERE NE.NOTIFICATION_KEY = N.NOTIFICATION_KEY AND I.LAST_CHG_TIME :operator :timestamp));',
    'WITH PaginatedResults AS (SELECT N.*, ROW_NUMBER() OVER (ORDER BY N.NOTIFICATION_LAST_CHANGE_TIME ASC) AS RowNum FROM NOTIFICATION N WHERE (N.NOTIFICATION_LAST_CHANGE_TIME :operator :timestamp) OR (N.NOTIFICATION_LAST_CHANGE_TIME IS NULL AND EXISTS (SELECT 1 FROM NOTIFICATION_EVENT NE JOIN INVESTIGATION I ON NE.INVESTIGATION_KEY = I.INVESTIGATION_KEY WHERE NE.NOTIFICATION_KEY = N.NOTIFICATION_KEY AND I.LAST_CHG_TIME :operator :timestamp))) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),
    ('BMIRD_MULTI_VALUE_FIELD_GROUP', 'RDB',
    'SELECT BMIRD_MULTI_VALUE_FIELD_GROUP.* FROM BMIRD_MULTI_VALUE_FIELD_GROUP JOIN BMIRD_CASE ON BMIRD_MULTI_VALUE_FIELD_GROUP.BMIRD_MULTI_VAL_GRP_KEY = BMIRD_CASE.BMIRD_MULTI_VAL_GRP_KEY JOIN INVESTIGATION ON BMIRD_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM BMIRD_MULTI_VALUE_FIELD_GROUP JOIN BMIRD_CASE ON BMIRD_MULTI_VALUE_FIELD_GROUP.BMIRD_MULTI_VAL_GRP_KEY = BMIRD_CASE.BMIRD_MULTI_VAL_GRP_KEY JOIN INVESTIGATION ON BMIRD_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT BMIRD_MULTI_VALUE_FIELD_GROUP.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM BMIRD_MULTI_VALUE_FIELD_GROUP JOIN BMIRD_CASE ON BMIRD_MULTI_VALUE_FIELD_GROUP.BMIRD_MULTI_VAL_GRP_KEY = BMIRD_CASE.BMIRD_MULTI_VAL_GRP_KEY JOIN INVESTIGATION ON BMIRD_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;')
;


INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination) VALUES
    ('D_VAR_PAM', 'RDB',
    'SELECT D_VAR_PAM.* FROM D_VAR_PAM JOIN F_VAR_PAM ON F_VAR_PAM.D_VAR_PAM_KEY = D_VAR_PAM.D_VAR_PAM_KEY JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT D_VAR_PAM.* FROM D_VAR_PAM JOIN F_VAR_PAM ON F_VAR_PAM.D_VAR_PAM_KEY = D_VAR_PAM.D_VAR_PAM_KEY JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME IS NULL AND INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM D_VAR_PAM JOIN F_VAR_PAM ON F_VAR_PAM.D_VAR_PAM_KEY = D_VAR_PAM.D_VAR_PAM_KEY JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_VAR_PAM.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_VAR_PAM JOIN F_VAR_PAM ON F_VAR_PAM.D_VAR_PAM_KEY = D_VAR_PAM.D_VAR_PAM_KEY JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_PCR_SOURCE_GROUP', 'RDB',
    'SELECT D_PCR_SOURCE_GROUP.* FROM D_PCR_SOURCE_GROUP JOIN F_VAR_PAM ON F_VAR_PAM.D_PCR_SOURCE_GROUP_KEY = D_PCR_SOURCE_GROUP.D_PCR_SOURCE_GROUP_KEY JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM D_PCR_SOURCE_GROUP JOIN F_VAR_PAM ON F_VAR_PAM.D_PCR_SOURCE_GROUP_KEY = D_PCR_SOURCE_GROUP.D_PCR_SOURCE_GROUP_KEY JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_PCR_SOURCE_GROUP.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_PCR_SOURCE_GROUP JOIN F_VAR_PAM ON F_VAR_PAM.D_PCR_SOURCE_GROUP_KEY = D_PCR_SOURCE_GROUP.D_PCR_SOURCE_GROUP_KEY JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_RASH_LOC_GEN_GROUP', 'RDB',
    'SELECT D_RASH_LOC_GEN_GROUP.* FROM D_RASH_LOC_GEN_GROUP JOIN F_VAR_PAM ON F_VAR_PAM.D_RASH_LOC_GEN_GROUP_KEY = D_RASH_LOC_GEN_GROUP.D_RASH_LOC_GEN_GROUP_KEY JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM D_RASH_LOC_GEN_GROUP JOIN F_VAR_PAM ON F_VAR_PAM.D_RASH_LOC_GEN_GROUP_KEY = D_RASH_LOC_GEN_GROUP.D_RASH_LOC_GEN_GROUP_KEY JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_RASH_LOC_GEN_GROUP.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_RASH_LOC_GEN_GROUP JOIN F_VAR_PAM ON F_VAR_PAM.D_RASH_LOC_GEN_GROUP_KEY = D_RASH_LOC_GEN_GROUP.D_RASH_LOC_GEN_GROUP_KEY JOIN INVESTIGATION ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.ADD_TIME :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;')
;


INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination) VALUES
    ('VAR_PAM_LDF', 'RDB',
    'SELECT VAR_PAM_LDF.* FROM VAR_PAM_LDF JOIN INVESTIGATION ON VAR_PAM_LDF.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE VAR_PAM_LDF.add_time :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM VAR_PAM_LDF JOIN INVESTIGATION ON VAR_PAM_LDF.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE VAR_PAM_LDF.add_time :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT VAR_PAM_LDF.*, ROW_NUMBER() OVER (ORDER BY CASE WHEN VAR_PAM_LDF.add_time :operator :timestamp THEN VAR_PAM_LDF.add_time ELSE INVESTIGATION.LAST_CHG_TIME END ASC) AS RowNum FROM VAR_PAM_LDF JOIN INVESTIGATION ON VAR_PAM_LDF.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE VAR_PAM_LDF.add_time :operator :timestamp OR INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('HEPATITIS_DATAMART', 'RDB',
    'SELECT * FROM HEPATITIS_DATAMART WHERE REFRESH_DATETIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM HEPATITIS_DATAMART WHERE REFRESH_DATETIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY REFRESH_DATETIME ASC) AS RowNum FROM HEPATITIS_DATAMART WHERE REFRESH_DATETIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_CASE_MANAGEMENT', 'RDB',
    'SELECT D_CASE_MANAGEMENT.* FROM D_CASE_MANAGEMENT JOIN INVESTIGATION ON D_CASE_MANAGEMENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM D_CASE_MANAGEMENT JOIN INVESTIGATION ON D_CASE_MANAGEMENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_CASE_MANAGEMENT.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM D_CASE_MANAGEMENT JOIN INVESTIGATION ON D_CASE_MANAGEMENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('D_INTERVIEW', 'RDB',
    'SELECT D_INTERVIEW.* FROM D_INTERVIEW WHERE D_INTERVIEW.ADD_TIME :operator :timestamp OR D_INTERVIEW.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM D_INTERVIEW WHERE D_INTERVIEW.ADD_TIME :operator :timestamp OR D_INTERVIEW.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT D_INTERVIEW.*, ROW_NUMBER() OVER (ORDER BY CASE WHEN D_INTERVIEW.ADD_TIME :operator :timestamp THEN D_INTERVIEW.ADD_TIME ELSE D_INTERVIEW.LAST_CHG_TIME END ASC) AS RowNum FROM D_INTERVIEW WHERE D_INTERVIEW.ADD_TIME :operator :timestamp OR D_INTERVIEW.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('MEASLES_CASE', 'RDB',
    'SELECT MEASLES_CASE.* FROM MEASLES_CASE JOIN INVESTIGATION ON MEASLES_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM MEASLES_CASE JOIN INVESTIGATION ON MEASLES_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT MEASLES_CASE.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM MEASLES_CASE JOIN INVESTIGATION ON MEASLES_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('CONFIRMATION_METHOD_GROUP', 'RDB',
    'SELECT CONFIRMATION_METHOD_GROUP.* FROM CONFIRMATION_METHOD_GROUP JOIN INVESTIGATION ON CONFIRMATION_METHOD_GROUP.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'SELECT CONFIRMATION_METHOD_GROUP.* FROM CONFIRMATION_METHOD_GROUP JOIN INVESTIGATION ON CONFIRMATION_METHOD_GROUP.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME IS NULL;',
    'SELECT COUNT(*) FROM CONFIRMATION_METHOD_GROUP JOIN INVESTIGATION ON CONFIRMATION_METHOD_GROUP.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp;',
    'WITH PaginatedResults AS (SELECT CONFIRMATION_METHOD_GROUP.*, ROW_NUMBER() OVER (ORDER BY INVESTIGATION.LAST_CHG_TIME ASC) AS RowNum FROM CONFIRMATION_METHOD_GROUP JOIN INVESTIGATION ON CONFIRMATION_METHOD_GROUP.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY WHERE INVESTIGATION.LAST_CHG_TIME :operator :timestamp) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('CONFIRMATION_METHOD', 'RDB',
    'SELECT * FROM CONFIRMATION_METHOD;',
    NULL,
    'SELECT COUNT(*) FROM CONFIRMATION_METHOD;',
    'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum FROM CONFIRMATION_METHOD) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),

    ('Rdb_Date', 'RDB',
    'SELECT * FROM Rdb_Date;',
    NULL,
    'SELECT COUNT(*) FROM Rdb_Date;',
    'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum FROM Rdb_Date) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;')
;
