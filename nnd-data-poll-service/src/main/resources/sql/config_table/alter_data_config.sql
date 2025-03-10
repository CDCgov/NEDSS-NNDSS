ALTER TABLE poll_data_sync_config
    ADD key_list NVARCHAR(250);


ALTER TABLE poll_data_sync_config
    ADD recreate_applied bit DEFAULT 0;

ALTER TABLE poll_data_sync_config
    ADD no_pagination bit DEFAULT 0;


ALTER TABLE poll_data_sync_config
    ADD use_key_pagination bit DEFAULT 0;

-- ALTER TABLE poll_data_sync_config
--     ADD last_max_key NVARCHAR(255) NULL;


IF EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'poll_data_sync_config'
)
BEGIN
ALTER TABLE poll_data_sync_config
DROP COLUMN
        last_executed_log;
END
GO
