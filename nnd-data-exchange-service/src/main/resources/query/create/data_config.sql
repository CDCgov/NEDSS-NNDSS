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
    WHERE TABLE_NAME = 'data_sync_config')
BEGIN
CREATE TABLE data_sync_config
(
    table_name      NVARCHAR(255) NOT NULL PRIMARY KEY,
    source_db       NVARCHAR(255) NOT NULL,
    query NVARCHAR(MAX) NOT NULL,
    query_count                NVARCHAR( MAX) NOT NULL,
    query_with_pagination      NVARCHAR( MAX) NOT NULL,
    query_with_null_timestamp NVARCHAR(MAX) NULL,
    created_at      DATETIME2 DEFAULT GETDATE(),
    last_executed_timestamp DATETIME2 NULL DEFAULT NULL,
    last_executed_run_time NVARCHAR(255) NULL DEFAULT NULL,
    last_executed_result_count INTEGER NULL DEFAULT NULL
);
END
GO
