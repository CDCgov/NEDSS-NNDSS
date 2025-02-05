IF
NOT EXISTS(SELECT *
              FROM sys.databases
              WHERE name = 'RDB')
BEGIN
    CREATE
DATABASE RDB;
END
GO

USE RDB;
GO

IF NOT EXISTS(
    SELECT 'X'
    FROM INFORMATION_SCHEMA.TABLES
    WHERE TABLE_NAME = 'data_sync_config')
BEGIN
CREATE TABLE data_sync_config
(
    table_name                 NVARCHAR(255) NOT NULL PRIMARY KEY,
    source_db                  NVARCHAR(255) NOT NULL,
    query                      NVARCHAR(MAX) NOT NULL,
    query_count                NVARCHAR( MAX) NOT NULL,
    query_with_pagination      NVARCHAR( MAX) NOT NULL,
    query_with_null_timestamp  NVARCHAR(MAX) NULL,
    created_at                 DATETIME2 DEFAULT GETDATE(),
    last_executed_timestamp    DATETIME2 NULL DEFAULT NULL,
    last_executed_run_time     NVARCHAR(255) NULL DEFAULT NULL,
    last_executed_result_count INTEGER NULL DEFAULT NULL,
    log_start_row              NVARCHAR(255) NULL,
    log_end_row                NVARCHAR(255) NULL
);
END
GO

IF EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'data_sync_config'
    AND COLUMN_NAME IN (
        'last_executed_timestamp',
        'last_executed_run_time',
        'last_executed_result_count',
        'log_start_row',
        'log_end_row'
    )
)
BEGIN
UPDATE data_sync_config
set last_executed_timestamp = null,
    last_executed_run_time = null,
    last_executed_result_count = null,
    log_start_row = null,
    log_end_row = null;
END
GO

IF EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'data_sync_config'
)
BEGIN
ALTER TABLE data_sync_config
DROP COLUMN
        last_executed_timestamp,
        last_executed_run_time,
        last_executed_result_count,
        log_start_row,
        log_end_row;
END
GO

IF NOT EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.TABLES
    WHERE TABLE_NAME = 'data_sync_log'
)
BEGIN
CREATE TABLE data_sync_log
(
    log_id      INT IDENTITY(1,1) PRIMARY KEY,
    table_name  NVARCHAR(255) NOT NULL,
    status_sync NVARCHAR(20) NOT NULL,
    error_desc  NVARCHAR(MAX) NULL,
    start_time  DATETIME NOT NULL DEFAULT GETDATE(),
    end_time    DATETIME NULL,
    last_executed_timestamp    DATETIME2 NULL DEFAULT NULL,
    last_executed_run_time     NVARCHAR(255) NULL DEFAULT NULL,
    last_executed_result_count INTEGER NULL DEFAULT NULL,
    log_start_row              NVARCHAR(255) NULL,
    log_end_row                NVARCHAR(255) NULL

    CONSTRAINT FK_data_sync_log_table
        FOREIGN KEY (table_name)
            REFERENCES data_sync_config (table_name)
            ON DELETE CASCADE
);
END
GO

