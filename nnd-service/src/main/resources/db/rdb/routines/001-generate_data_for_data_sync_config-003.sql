IF
NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'DM_INV_MEASLES_DATAMART')
BEGIN
INSERT INTO [dbo].[data_sync_config]
(table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination)
VALUES
    ('DM_INV_MEASLES_DATAMART', 'RDB', 'SELECT MD.* FROM DM_INV_MEASLES_DATAMART MD', NULL,
    'SELECT COUNT(*) FROM DM_INV_MEASLES_DATAMART MD',
    '');
END;
