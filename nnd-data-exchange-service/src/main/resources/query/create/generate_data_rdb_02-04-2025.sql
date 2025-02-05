-- COVID_CASE_DATAMART
WITH PaginatedResults AS (
    SELECT COVID_CASE_DATAMART.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM COVID_CASE_DATAMART
    WHERE COVID_CASE_DATAMART.ADD_TIME :operator :timestamp
    OR COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT COVID_CASE_DATAMART.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM COVID_CASE_DATAMART
WHERE COVID_CASE_DATAMART.ADD_TIME :operator :timestamp
    OR COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp;

SELECT COUNT(*)
FROM COVID_CASE_DATAMART
WHERE COVID_CASE_DATAMART.ADD_TIME :operator :timestamp
   OR COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp;


-- COVID_LAB_DATAMART
WITH PaginatedResults AS (
    SELECT COVID_LAB_DATAMART.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM COVID_LAB_DATAMART
    WHERE COVID_LAB_DATAMART.Lab_Added_Dt :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT COVID_LAB_DATAMART.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM COVID_LAB_DATAMART
WHERE COVID_LAB_DATAMART.Lab_Added_Dt :operator :timestamp;

SELECT COUNT(*)
FROM COVID_LAB_DATAMART
WHERE COVID_LAB_DATAMART.Lab_Added_Dt :operator :timestamp;

-- COVID_LAB_CELR_DATAMART

WITH PaginatedResults AS (
    SELECT COVID_LAB_CELR_DATAMART.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM COVID_LAB_CELR_DATAMART
    WHERE COVID_LAB_CELR_DATAMART.lab_update_dt :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT COVID_LAB_CELR_DATAMART.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM COVID_LAB_CELR_DATAMART
WHERE COVID_LAB_CELR_DATAMART.lab_update_dt :operator :timestamp;

SELECT COUNT(*)
FROM COVID_LAB_CELR_DATAMART
WHERE COVID_LAB_CELR_DATAMART.lab_update_dt :operator :timestamp;



