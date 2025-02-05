INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('NRT_OBSERVATION', 'RDB_MODERN', 'SELECT * FROM nrt_observation WHERE last_chg_time :operator :timestamp OR refresh_datetime :operator :timestamp;', NULL, 'SELECT COUNT(*) FROM nrt_observation WHERE last_chg_time :operator :timestamp OR refresh_datetime :operator :timestamp;', 'WITH PaginatedResults AS (
        SELECT *, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
        FROM nrt_observation
        WHERE last_chg_time :operator :timestamp OR refresh_datetime :operator :timestamp
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow;')
;


INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('NRT_OBSERVATION_CODED', 'RDB_MODERN', 'SELECT *
     FROM rdb_modern.dbo.nrt_observation_coded
     JOIN rdb_modern.dbo.nrt_observation
     ON rdb_modern.dbo.nrt_observation_coded.observation_uid = rdb_modern.dbo.nrt_observation.observation_uid
     WHERE rdb_modern.dbo.nrt_observation_coded.refresh_datetime :operator :timestamp
     OR rdb_modern.dbo.nrt_observation.last_chg_time :operator :timestamp
     OR rdb_modern.dbo.nrt_observation.refresh_datetime :operator :timestamp;', NULL, 'SELECT COUNT(*)
     FROM rdb_modern.dbo.nrt_observation_coded
     JOIN rdb_modern.dbo.nrt_observation
     ON rdb_modern.dbo.nrt_observation_coded.observation_uid = rdb_modern.dbo.nrt_observation.observation_uid
     WHERE rdb_modern.dbo.nrt_observation_coded.refresh_datetime :operator :timestamp
     OR rdb_modern.dbo.nrt_observation.last_chg_time :operator :timestamp
     OR rdb_modern.dbo.nrt_observation.refresh_datetime :operator :timestamp;', 'WITH PaginatedResults AS (
        SELECT *, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
        FROM rdb_modern.dbo.nrt_observation_coded
        JOIN rdb_modern.dbo.nrt_observation
        ON rdb_modern.dbo.nrt_observation_coded.observation_uid = rdb_modern.dbo.nrt_observation.observation_uid
        WHERE rdb_modern.dbo.nrt_observation_coded.refresh_datetime :operator :timestamp
        OR rdb_modern.dbo.nrt_observation.last_chg_time :operator :timestamp
        OR rdb_modern.dbo.nrt_observation.refresh_datetime :operator :timestamp
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow;')
;


INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_investigation', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_investigation.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_investigation
WHERE rdb_modern.dbo.nrt_investigation.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp
    );', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_investigation
WHERE rdb_modern.dbo.nrt_investigation.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp;
', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_investigation.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_investigation
    WHERE rdb_modern.dbo.nrt_investigation.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_investigation_notification', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_investigation_notification.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_investigation_notification
WHERE rdb_modern.dbo.nrt_investigation_notification.notif_add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation_notification.notif_last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation_notification.refresh_datetime :operator :timestamp
    );', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_investigation_notification
WHERE rdb_modern.dbo.nrt_investigation_notification.notif_add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation_notification.notif_last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation_notification.refresh_datetime :operator :timestamp;
', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_investigation_notification.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_investigation_notification
    WHERE rdb_modern.dbo.nrt_investigation_notification.notif_add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation_notification.notif_last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation_notification.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;


INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_investigation_observation', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_investigation_observation.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_investigation_observation
WHERE rdb_modern.dbo.nrt_investigation_observation.refresh_datetime :operator :timestamp
    );
', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_investigation_observation
WHERE rdb_modern.dbo.nrt_investigation_observation.refresh_datetime :operator :timestamp;
', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_investigation_observation.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_investigation_observation
    WHERE rdb_modern.dbo.nrt_investigation_observation.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_observation_date', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_observation_date.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_observation_date
WHERE rdb_modern.dbo.nrt_observation_date.refresh_datetime :operator :timestamp
    );', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_observation_date
WHERE rdb_modern.dbo.nrt_observation_date.refresh_datetime :operator :timestamp;
', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_observation_date.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_observation_date
    WHERE rdb_modern.dbo.nrt_observation_date.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;


INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_observation_numeric', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_observation_numeric.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_observation_numeric
WHERE rdb_modern.dbo.nrt_observation_numeric.refresh_datetime :operator :timestamp
    );', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_observation_numeric
WHERE rdb_modern.dbo.nrt_observation_numeric.refresh_datetime :operator :timestamp;
', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_observation_numeric.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_observation_numeric
    WHERE rdb_modern.dbo.nrt_observation_numeric.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_observation_txt', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_observation_txt.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_observation_txt
WHERE rdb_modern.dbo.nrt_observation_txt.refresh_datetime :operator :timestamp;
', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_observation_txt
WHERE rdb_modern.dbo.nrt_observation_txt.refresh_datetime :operator :timestamp;
', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_observation_txt.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_observation_txt
    WHERE rdb_modern.dbo.nrt_observation_txt.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;


INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_organization', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_organization.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_organization
WHERE rdb_modern.dbo.nrt_organization.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_organization.last_chg_time :operator :timestamp;
', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_organization
WHERE rdb_modern.dbo.nrt_organization.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_organization.last_chg_time :operator :timestamp;
', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_organization.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_organization
    WHERE rdb_modern.dbo.nrt_organization.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_organization.last_chg_time :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_patient', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_patient.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_patient
WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp;
', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_patient
WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp;
', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_patient.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_patient
    WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;


INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_provider', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_provider.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_provider
WHERE rdb_modern.dbo.nrt_provider.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.refresh_datetime :operator :timestamp;
', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_provider
WHERE rdb_modern.dbo.nrt_provider.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_provider.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_provider.refresh_datetime :operator :timestamp;
', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_provider.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_provider
    WHERE rdb_modern.dbo.nrt_provider.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_investigation_key', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_investigation_key.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_investigation_key
         JOIN rdb_modern.dbo.nrt_investigation
              ON rdb_modern.dbo.nrt_investigation_key.d_investigation_key = rdb_modern.dbo.nrt_investigation.public_health_case_uid
WHERE rdb_modern.dbo.nrt_investigation.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp;
', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_investigation_key
         JOIN rdb_modern.dbo.nrt_investigation
              ON rdb_modern.dbo.nrt_investigation_key.d_investigation_key = rdb_modern.dbo.nrt_investigation.public_health_case_uid
WHERE rdb_modern.dbo.nrt_investigation.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp;
', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_investigation_key.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_investigation_key
             JOIN rdb_modern.dbo.nrt_investigation
                  ON rdb_modern.dbo.nrt_investigation_key.d_investigation_key = rdb_modern.dbo.nrt_investigation.public_health_case_uid
    WHERE rdb_modern.dbo.nrt_investigation.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;


INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_organization_key', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_organization_key.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_organization_key
         JOIN rdb_modern.dbo.nrt_organization
              ON rdb_modern.dbo.nrt_organization_key.d_organization_key = rdb_modern.dbo.nrt_organization.organization_uid
WHERE rdb_modern.dbo.nrt_organization.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_organization.last_chg_time :operator :timestamp;
', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_organization_key
         JOIN rdb_modern.dbo.nrt_organization
              ON rdb_modern.dbo.nrt_organization_key.d_organization_key = rdb_modern.dbo.nrt_organization.organization_uid
WHERE rdb_modern.dbo.nrt_organization.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_organization.last_chg_time :operator :timestamp;
', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_organization_key.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_organization_key
             JOIN rdb_modern.dbo.nrt_organization
                  ON rdb_modern.dbo.nrt_organization_key.d_organization_key = rdb_modern.dbo.nrt_organization.organization_uid
    WHERE rdb_modern.dbo.nrt_organization.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_organization.last_chg_time :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;
')
;

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_patient_key', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_patient_key.*
FROM rdb_modern.dbo.nrt_patient_key
         JOIN rdb_modern.dbo.nrt_patient
              ON rdb_modern.dbo.nrt_patient_key.d_patient_key = rdb_modern.dbo.nrt_patient.patient_uid
WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp;
', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_patient_key
         JOIN rdb_modern.dbo.nrt_patient
              ON rdb_modern.dbo.nrt_patient_key.d_patient_key = rdb_modern.dbo.nrt_patient.patient_uid
WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp;', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_patient_key.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_patient_key
             JOIN rdb_modern.dbo.nrt_patient
                  ON rdb_modern.dbo.nrt_patient_key.d_patient_key = rdb_modern.dbo.nrt_patient.patient_uid
    WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;

INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('nrt_provider_key', 'RDB_MODERN', 'SELECT rdb_modern.dbo.nrt_provider_key.*
FROM rdb_modern.dbo.nrt_provider_key
         JOIN rdb_modern.dbo.nrt_provider
              ON rdb_modern.dbo.nrt_provider_key.d_provider_key = rdb_modern.dbo.nrt_provider.provider_uid
WHERE rdb_modern.dbo.nrt_provider.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.refresh_datetime :operator :timestamp;
', NULL, 'SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_provider_key
         JOIN rdb_modern.dbo.nrt_provider
              ON rdb_modern.dbo.nrt_provider_key.d_provider_key = rdb_modern.dbo.nrt_provider.provider_uid
WHERE rdb_modern.dbo.nrt_provider.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_provider.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_provider.refresh_datetime :operator :timestamp;

', 'WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_provider_key.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_provider_key
             JOIN rdb_modern.dbo.nrt_provider
                  ON rdb_modern.dbo.nrt_provider_key.d_provider_key = rdb_modern.dbo.nrt_provider.provider_uid
    WHERE rdb_modern.dbo.nrt_provider.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;')
;
