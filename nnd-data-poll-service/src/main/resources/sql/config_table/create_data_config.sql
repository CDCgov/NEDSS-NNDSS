IF NOT EXISTS(
    SELECT 'X'
    FROM INFORMATION_SCHEMA.TABLES
    WHERE TABLE_NAME = 'poll_data_sync_config')
BEGIN
CREATE TABLE poll_data_sync_config
(
    table_name      NVARCHAR(255) NOT NULL PRIMARY KEY,
    source_db       NVARCHAR(255) NOT NULL,
    table_order int NULL,
    last_update_time  DATETIME2,
    last_update_time_s3 DATETIME2,
    last_update_time_local_dir DATETIME2,
    key_list NVARCHAR(250),
    recreate_applied bit DEFAULT 0,
    no_pagination bit DEFAULT 0,
    use_key_pagination bit DEFAULT 0
);
END

IF NOT EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.TABLES
    WHERE TABLE_NAME = 'poll_data_log'
)
BEGIN
CREATE TABLE poll_data_log
(
    log_id      INT IDENTITY(1,1) PRIMARY KEY,
    table_name  NVARCHAR(255) NOT NULL,
    status_sync NVARCHAR(20) NOT NULL,
    start_time  DATETIME NOT NULL DEFAULT GETDATE(),
    end_time    DATETIME NULL,
    executed_log NVARCHAR(MAX) NULL,
    stack_trace NVARCHAR(MAX) NULL,

    CONSTRAINT FK_poll_data_log_table
        FOREIGN KEY (table_name)
            REFERENCES poll_data_sync_config (table_name)
            ON DELETE CASCADE
);
END
