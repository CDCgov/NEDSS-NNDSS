-- Declare the variable
DECLARE @timestamp DATETIME;

-- Set the variable value
SET @timestamp = '2024-01-01 12:00:00';

SELECT 'nrt_observation' AS table_name, COUNT(*) AS count_value
FROM nrt_observation
WHERE last_chg_time <= @timestamp OR refresh_datetime <= @timestamp