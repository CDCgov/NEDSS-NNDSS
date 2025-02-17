SELECT 'nrt_investigation' AS table_name, COUNT(*) AS count FROM nrt_investigation
UNION ALL
SELECT 'nrt_investigation_key' AS table_name, COUNT(*) AS count FROM nrt_investigation_key
UNION ALL
SELECT 'nrt_investigation_notification' AS table_name, COUNT(*) AS count FROM nrt_investigation_notification
UNION ALL
SELECT 'nrt_investigation_observation' AS table_name, COUNT(*) AS count FROM nrt_investigation_observation
UNION ALL
SELECT 'nrt_observation' AS table_name, COUNT(*) AS count FROM nrt_observation
UNION ALL
SELECT 'nrt_observation_coded' AS table_name, COUNT(*) AS count FROM nrt_observation_coded
UNION ALL
SELECT 'nrt_observation_date' AS table_name, COUNT(*) AS count FROM nrt_observation_date
UNION ALL
SELECT 'nrt_observation_numeric' AS table_name, COUNT(*) AS count FROM nrt_observation_numeric
UNION ALL
SELECT 'nrt_observation_txt' AS table_name, COUNT(*) AS count FROM nrt_observation_txt
UNION ALL
SELECT 'nrt_organization' AS table_name, COUNT(*) AS count FROM nrt_organization
UNION ALL
SELECT 'nrt_organization_key' AS table_name, COUNT(*) AS count FROM nrt_organization_key
UNION ALL
SELECT 'nrt_patient' AS table_name, COUNT(*) AS count FROM nrt_patient
UNION ALL
SELECT 'nrt_patient_key' AS table_name, COUNT(*) AS count FROM nrt_patient_key
UNION ALL
SELECT 'nrt_provider' AS table_name, COUNT(*) AS count FROM nrt_provider
UNION ALL
SELECT 'nrt_provider_key' AS table_name, COUNT(*) AS count FROM nrt_provider_key;
