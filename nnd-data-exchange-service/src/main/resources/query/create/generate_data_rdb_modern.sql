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
