-- nrt_investigation
SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_investigation
WHERE rdb_modern.dbo.nrt_investigation.addtime :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp;


SELECT rdb_modern.dbo.nrt_investigation.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_investigation
WHERE rdb_modern.dbo.nrt_investigation.addtime :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp
    )

WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_investigation.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_investigation
    WHERE rdb_modern.dbo.nrt_investigation.addtime :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

-- nrt_investigation_notification
SELECT rdb_modern.dbo.nrt_investigation_notification.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_investigation_notification
WHERE rdb_modern.dbo.nrt_investigation_notification.notif_add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation_notification.notif_last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation_notification.refresh_datetime :operator :timestamp
    )

WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_investigation_notification.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_investigation_notification
    WHERE rdb_modern.dbo.nrt_investigation_notification.notif_add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation_notification.notif_last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation_notification.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;


SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_investigation_notification
WHERE rdb_modern.dbo.nrt_investigation_notification.notif_add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation_notification.notif_last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation_notification.refresh_datetime :operator :timestamp;

-- nrt_investigation_observation
WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_investigation_observation.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_investigation_observation
    WHERE rdb_modern.dbo.nrt_investigation_observation.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.nrt_investigation_observation.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_investigation_observation
WHERE rdb_modern.dbo.nrt_investigation_observation.refresh_datetime :operator :timestamp
    );


SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_investigation_observation
WHERE rdb_modern.dbo.nrt_investigation_observation.refresh_datetime :operator :timestamp;


-- nrt_observation_date
WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_observation_date.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_observation_date
    WHERE rdb_modern.dbo.nrt_observation_date.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.nrt_observation_date.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_observation_date
WHERE rdb_modern.dbo.nrt_observation_date.refresh_datetime :operator :timestamp
    );

SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_observation_date
WHERE rdb_modern.dbo.nrt_observation_date.refresh_datetime :operator :timestamp;


-- nrt_observation_numeric

WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_observation_numeric.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_observation_numeric
    WHERE rdb_modern.dbo.nrt_observation_numeric.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.nrt_observation_numeric.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_observation_numeric
WHERE rdb_modern.dbo.nrt_observation_numeric.refresh_datetime :operator :timestamp
    );


SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_observation_numeric
WHERE rdb_modern.dbo.nrt_observation_numeric.refresh_datetime :operator :timestamp;


-- nrt_observation_txt
WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_observation_txt.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_observation_txt
    WHERE rdb_modern.dbo.nrt_observation_txt.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.nrt_observation_txt.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_observation_txt
WHERE rdb_modern.dbo.nrt_observation_txt.refresh_datetime :operator :timestamp;


SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_observation_txt
WHERE rdb_modern.dbo.nrt_observation_txt.refresh_datetime :operator :timestamp;

-- nrt_organization
WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_organization.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_organization
    WHERE rdb_modern.dbo.nrt_organization.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_organization.last_chg_time :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.nrt_organization.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_organization
WHERE rdb_modern.dbo.nrt_organization.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_organization.last_chg_time :operator :timestamp;

SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_organization
WHERE rdb_modern.dbo.nrt_organization.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_organization.last_chg_time :operator :timestamp;

-- nrt_patient
WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_patient.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_patient
    WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.nrt_patient.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_patient
WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp;

SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_patient
WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp;


-- nrt_provider

WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_provider.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_provider
    WHERE rdb_modern.dbo.nrt_provider.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.nrt_provider.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_provider
WHERE rdb_modern.dbo.nrt_provider.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.refresh_datetime :operator :timestamp;

SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_provider
WHERE rdb_modern.dbo.nrt_provider.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_provider.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_provider.refresh_datetime :operator :timestamp;



-- nrt_investigation_key
WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_investigation_key.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_investigation_key
             JOIN rdb_modern.dbo.nrt_investigation
                  ON rdb_modern.dbo.nrt_investigation_key.d_investigation_key = rdb_modern.dbo.nrt_investigation.public_health_case_uid
    WHERE rdb_modern.dbo.nrt_investigation.addtime :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.nrt_investigation_key.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_investigation_key
         JOIN rdb_modern.dbo.nrt_investigation
              ON rdb_modern.dbo.nrt_investigation_key.d_investigation_key = rdb_modern.dbo.nrt_investigation.public_health_case_uid
WHERE rdb_modern.dbo.nrt_investigation.addtime :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp;

SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_investigation_key
         JOIN rdb_modern.dbo.nrt_investigation
              ON rdb_modern.dbo.nrt_investigation_key.d_investigation_key = rdb_modern.dbo.nrt_investigation.public_health_case_uid
WHERE rdb_modern.dbo.nrt_investigation.addtime :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_investigation.refresh_datetime :operator :timestamp;


-- nrt_organization_key
WITH PaginatedResults AS (
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


SELECT rdb_modern.dbo.nrt_organization_key.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_organization_key
         JOIN rdb_modern.dbo.nrt_organization
              ON rdb_modern.dbo.nrt_organization_key.d_organization_key = rdb_modern.dbo.nrt_organization.organization_uid
WHERE rdb_modern.dbo.nrt_organization.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_organization.last_chg_time :operator :timestamp;

SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_organization_key
         JOIN rdb_modern.dbo.nrt_organization
              ON rdb_modern.dbo.nrt_organization_key.d_organization_key = rdb_modern.dbo.nrt_organization.organization_uid
WHERE rdb_modern.dbo.nrt_organization.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_organization.last_chg_time :operator :timestamp;

-- nrt_patient_key

WITH PaginatedResults AS (
    SELECT rdb_modern.dbo.nrt_patient_key.*,
           ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM rdb_modern.dbo.nrt_patient_key
             JOIN rdb_modern.dbo.nrt_patient
                  ON rdb_modern.dbo.nrt_patient_key.nrt_patient_key = rdb_modern.dbo.nrt_patient.patient_uid
    WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp
    )
SELECT * FROM PaginatedResults
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.nrt_patient_key.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_patient_key
         JOIN rdb_modern.dbo.nrt_patient
              ON rdb_modern.dbo.nrt_patient_key.nrt_patient_key = rdb_modern.dbo.nrt_patient.patient_uid
WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp;


SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_patient_key
         JOIN rdb_modern.dbo.nrt_patient
              ON rdb_modern.dbo.nrt_patient_key.nrt_patient_key = rdb_modern.dbo.nrt_patient.patient_uid
WHERE rdb_modern.dbo.nrt_patient.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_patient.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_patient.refresh_datetime :operator :timestamp;



-- nrt_provider_key
WITH PaginatedResults AS (
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
WHERE RowNum BETWEEN :startRow AND :endRow;

SELECT rdb_modern.dbo.nrt_provider_key.*,
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
FROM rdb_modern.dbo.nrt_provider_key
         JOIN rdb_modern.dbo.nrt_provider
              ON rdb_modern.dbo.nrt_provider_key.d_provider_key = rdb_modern.dbo.nrt_provider.provider_uid
WHERE rdb_modern.dbo.nrt_provider.add_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.last_chg_time :operator :timestamp
    OR rdb_modern.dbo.nrt_provider.refresh_datetime :operator :timestamp;

SELECT COUNT(*)
FROM rdb_modern.dbo.nrt_provider_key
         JOIN rdb_modern.dbo.nrt_provider
              ON rdb_modern.dbo.nrt_provider_key.d_provider_key = rdb_modern.dbo.nrt_provider.provider_uid
WHERE rdb_modern.dbo.nrt_provider.add_time :operator :timestamp
   OR rdb_modern.dbo.nrt_provider.last_chg_time :operator :timestamp
   OR rdb_modern.dbo.nrt_provider.refresh_datetime :operator :timestamp;

