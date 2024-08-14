SELECT * FROM CONDITIONS;
SELECT * FROM RdbDates;
SELECT * FROM D_PATIENT WHERE D_PATIENT.PATIENT_LAST_CHANGE_TIME >= '2024-07-11';
SELECT * FROM INVESTIGATION WHERE INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT A.*
    FROM CASE_COUNT as A
    INNER JOIN  INVESTIGATION as B
    ON A.INVESTIGATION_KEY = B.INVESTIGATION_KEY
    WHERE B.ADD_TIME >= '2024-07-11'
    OR B.LAST_CHG_TIME >= '2024-07-11';

SELECT
    BMIRD_CASE.*
FROM
    BMIRD_CASE BMIRD_CASE
JOIN
    INVESTIGATION INVESTIGATION
    ON BMIRD_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
    INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    RUBELLA_CASE.*
FROM
    RUBELLA_CASE
JOIN
    INVESTIGATION
    ON RUBELLA_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
    INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';
SELECT
    PERTUSSIS_CASE.*
FROM
    PERTUSSIS_CASE
        JOIN
    INVESTIGATION
    ON PERTUSSIS_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    HEPATITIS_CASE.*
FROM
    HEPATITIS_CASE
        JOIN
    INVESTIGATION
    ON HEPATITIS_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    GENERIC_CASE.*
FROM
    GENERIC_CASE
        JOIN
    INVESTIGATION
    ON GENERIC_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    MEASLES_CASE.*
FROM
    MEASLES_CASE
        JOIN
    INVESTIGATION
    ON MEASLES_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT * FROM HEPATITIS_DATAMART WHERE REFRESH_DATETIME >= '2024-07-11';

SELECT * FROM CONFIRMATION_METHOD;

SELECT
    CONFIRMATION_METHOD_GROUP.*
FROM
    CONFIRMATION_METHOD_GROUP
        JOIN
    INVESTIGATION
    ON CONFIRMATION_METHOD_GROUP.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    D_INTERVIEW.*
FROM
    D_INTERVIEW
WHERE
        D_INTERVIEW.ADD_TIME >= '2024-07-11' OR D_INTERVIEW.LAST_CHG_TIME >= '2024-07-11';

SELECT
    D_CASE_MANAGEMENT.*
FROM
    D_CASE_MANAGEMENT
        JOIN
    INVESTIGATION
    ON D_CASE_MANAGEMENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    VAR_PAM_LDF.*
FROM
    VAR_PAM_LDF
        JOIN
    INVESTIGATION
    ON VAR_PAM_LDF.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        VAR_PAM_LDF.add_time >= '2024-07-11' OR INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    F_VAR_PAM.*
FROM
    F_VAR_PAM
        JOIN
    INVESTIGATION
    ON F_VAR_PAM.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.ADD_TIME >= '2024-07-11' OR INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    LDF_DATA.*
FROM
    LDF_DATA
        JOIN
    LDF_GROUP
    ON LDF_DATA.LDF_GROUP_KEY = LDF_GROUP.LDF_GROUP_KEY
        JOIN
    INVESTIGATION
    ON LDF_GROUP.BUSINESS_OBJECT_UID = INVESTIGATION.CASE_UID
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    LDF_FOODBORNE.*
FROM
    LDF_FOODBORNE
        JOIN
    INVESTIGATION
    ON LDF_FOODBORNE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    NOTIFICATION_EVENT.*
FROM
    NOTIFICATION_EVENT
        JOIN
    INVESTIGATION
    ON NOTIFICATION_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    D_INV_ADMINISTRATIVE.*
FROM
    D_INV_ADMINISTRATIVE
        JOIN
    F_PAGE_CASE
    ON D_INV_ADMINISTRATIVE.D_INV_ADMINISTRATIVE_KEY = F_PAGE_CASE.INVESTIGATION_KEY
        JOIN
    INVESTIGATION
    ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    D_INV_EPIDEMIOLOGY.*
FROM
    D_INV_EPIDEMIOLOGY
        JOIN
    F_PAGE_CASE
    ON D_INV_EPIDEMIOLOGY.D_INV_EPIDEMIOLOGY_KEY = F_PAGE_CASE.INVESTIGATION_KEY
        JOIN
    INVESTIGATION
    ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    D_INV_HIV.*
FROM
    D_INV_HIV
        JOIN
    F_PAGE_CASE
    ON D_INV_HIV.D_INV_HIV_KEY = F_PAGE_CASE.INVESTIGATION_KEY
        JOIN
    INVESTIGATION
    ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    D_INV_LAB_FINDING.*
FROM
    D_INV_LAB_FINDING
        JOIN
    F_PAGE_CASE
    ON D_INV_LAB_FINDING.D_INV_LAB_FINDING_KEY = F_PAGE_CASE.INVESTIGATION_KEY
        JOIN
    INVESTIGATION
    ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    D_INV_MEDICAL_HISTORY.*
FROM
    D_INV_MEDICAL_HISTORY
        JOIN
    F_PAGE_CASE
    ON D_INV_MEDICAL_HISTORY.D_INV_MEDICAL_HISTORY_KEY = F_PAGE_CASE.INVESTIGATION_KEY
        JOIN
    INVESTIGATION
    ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    D_INV_RISK_FACTOR.*
FROM
    D_INV_RISK_FACTOR
        JOIN
    F_PAGE_CASE
    ON D_INV_RISK_FACTOR.D_INV_RISK_FACTOR_KEY = F_PAGE_CASE.INVESTIGATION_KEY
        JOIN
    INVESTIGATION
    ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    D_INV_TREATMENT.*
FROM
    D_INV_TREATMENT
        JOIN
    F_PAGE_CASE
    ON D_INV_TREATMENT.D_INV_TREATMENT_KEY = F_PAGE_CASE.INVESTIGATION_KEY
        JOIN
    INVESTIGATION
    ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    D_INV_VACCINATION.*
FROM
    D_INV_VACCINATION
        JOIN
    F_PAGE_CASE
    ON D_INV_VACCINATION.D_INV_VACCINATION_KEY = F_PAGE_CASE.INVESTIGATION_KEY
        JOIN
    INVESTIGATION
    ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';


SELECT
    F_INTERVIEW_CASE.*
FROM
    F_INTERVIEW_CASE
        JOIN
    INVESTIGATION
    ON F_INTERVIEW_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
        JOIN
    D_INTERVIEW
    ON F_INTERVIEW_CASE.D_INTERVIEW_KEY = D_INTERVIEW.D_INTERVIEW_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11'
   OR D_INTERVIEW.ADD_TIME >= '2024-07-11'
   OR D_INTERVIEW.LAST_CHG_TIME >= '2024-07-11';

SELECT
    F_PAGE_CASE.*
FROM
    F_PAGE_CASE
        JOIN
    INVESTIGATION
    ON F_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    F_STD_PAGE_CASE.*
FROM
    F_STD_PAGE_CASE
        JOIN
    INVESTIGATION
    ON F_STD_PAGE_CASE.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';

SELECT
    TREATMENT.*
FROM
    TREATMENT
        JOIN
    TREATMENT_EVENT
    ON TREATMENT.TREATMENT_KEY = TREATMENT_EVENT.TREATMENT_KEY
        JOIN
    INVESTIGATION
    ON TREATMENT_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';


SELECT
    TREATMENT_EVENT.*
FROM
    TREATMENT_EVENT
        JOIN
    INVESTIGATION
    ON TREATMENT_EVENT.INVESTIGATION_KEY = INVESTIGATION.INVESTIGATION_KEY
WHERE
        INVESTIGATION.LAST_CHG_TIME >= '2024-07-11';












