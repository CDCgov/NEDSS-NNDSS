SELECT 'COVID_CASE_DATAMART' AS TableName, COUNT(*) AS RecordCount FROM COVID_CASE_DATAMART
UNION ALL
SELECT 'COVID_LAB_CELR_DATAMART', COUNT(*) FROM COVID_LAB_CELR_DATAMART
UNION ALL
SELECT 'COVID_LAB_DATAMART', COUNT(*) FROM COVID_LAB_DATAMART;
