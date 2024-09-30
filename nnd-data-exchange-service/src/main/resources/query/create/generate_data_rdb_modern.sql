INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('NRT_OBSERVATION', 'RDB_MODERN',
    'SELECT * FROM nrt_observation WHERE last_chg_time >= :timestamp OR refresh_datetime >= :timestamp;',
    NULL,
    'SELECT COUNT(*) FROM nrt_observation WHERE last_chg_time >= :timestamp OR refresh_datetime >= :timestamp;',
    'WITH PaginatedResults AS (
        SELECT *, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
        FROM nrt_observation
        WHERE last_chg_time >= :timestamp OR refresh_datetime >= :timestamp
    )
    SELECT * FROM PaginatedResults
    WHERE RowNum BETWEEN :startRow AND :endRow;')
;
