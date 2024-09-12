IF NOT EXISTS(SELECT *
              FROM sys.databases
              WHERE name = 'RDB')
BEGIN
    CREATE DATABASE RDB;
END
GO

USE RDB;
GO

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
    last_update_time  DATETIME2 NULL,
    last_update_time_s3  DATETIME2 NULL,
    last_executed_log NVARCHAR(MAX) NULL,
    query nvarchar(MAX) NULL
);
END
GO
