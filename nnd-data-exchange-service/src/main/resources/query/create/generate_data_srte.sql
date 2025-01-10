INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination) VALUES
    ('CONDITION_CODE', 'SRTE',
    'SELECT * FROM CONDITION_CODE',
    NULL,
    'SELECT COUNT(*) FROM CONDITION_CODE;',
    'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum FROM CONDITION_CODE) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'),
    ('CODE_TO_CONDITION', 'SRTE',
    'SELECT * FROM CODE_TO_CONDITION',
    NULL,
    'SELECT COUNT(*) FROM CODE_TO_CONDITION;',
    'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum FROM CODE_TO_CONDITION) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');


INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination) VALUES
    ('Program_area_code', 'SRTE',
    'SELECT * FROM Program_area_code;',
    NULL,
    'SELECT COUNT(*) FROM Program_area_code;',
    'WITH PaginatedResults AS (SELECT *, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum FROM Program_area_code) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;')
;