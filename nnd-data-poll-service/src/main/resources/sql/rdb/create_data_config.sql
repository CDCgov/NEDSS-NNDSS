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
    last_update_time      DATETIME2,
    table_order int NULL,
    query nvarchar(MAX) NOT NULL
);
END
GO