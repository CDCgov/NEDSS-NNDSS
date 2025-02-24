-- Declare the variable
DECLARE @timestamp DATETIME;

-- Set the variable value
SET @timestamp = '2024-01-01 12:00:00';


SELECT 'CODE_TO_CONDITION' AS table_name, COUNT(*) AS count_value
FROM CODE_TO_CONDITION

UNION ALL

SELECT 'CONDITION_CODE' AS table_name, COUNT(*) AS count_value
FROM CONDITION_CODE

UNION ALL

SELECT 'Program_area_code' AS table_name, COUNT(*) AS count_value
FROM Program_area_code

UNION ALL

SELECT 'jurisdiction_code' AS table_name, COUNT(*) AS count_value
FROM jurisdiction_code