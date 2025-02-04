-- COVID_CASE_DATAMART
WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.COVID_CASE_DATAMART.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.COVID_CASE_DATAMART
    WHERE rdb_modern.dbo.COVID_CASE_DATAMART.ADD_TIME :operator :timestamp
    OR rdb_modern.dbo.COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.COVID_CASE_DATAMART.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.COVID_CASE_DATAMART
WHERE rdb_modern.dbo.COVID_CASE_DATAMART.ADD_TIME :operator :timestamp
    OR rdb_modern.dbo.COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp;

SELECT COUNT(*)
FROM rdb_modern.dbo.COVID_CASE_DATAMART
WHERE rdb_modern.dbo.COVID_CASE_DATAMART.ADD_TIME :operator :timestamp
   OR rdb_modern.dbo.COVID_CASE_DATAMART.LAST_CHG_TIME :operator :timestamp;


-- COVID_LAB
WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.COVID_LAB.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.COVID_LAB
    WHERE rdb_modern.dbo.COVID_LAB.Lab_Added_Dt :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.COVID_LAB.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.COVID_LAB
WHERE rdb_modern.dbo.COVID_LAB.Lab_Added_Dt :operator :timestamp;

SELECT COUNT(*)
FROM rdb_modern.dbo.COVID_LAB
WHERE rdb_modern.dbo.COVID_LAB.Lab_Added_Dt :operator :timestamp;

-- COVID_LAB_CELR

WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.COVID_LAB_CELR.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.COVID_LAB_CELR
    WHERE rdb_modern.dbo.COVID_LAB_CELR.lab_update_dt :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.COVID_LAB_CELR.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.COVID_LAB_CELR
WHERE rdb_modern.dbo.COVID_LAB_CELR.lab_update_dt :operator :timestamp;

SELECT COUNT(*)
FROM rdb_modern.dbo.COVID_LAB_CELR
WHERE rdb_modern.dbo.COVID_LAB_CELR.lab_update_dt :operator :timestamp;



