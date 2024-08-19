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
    WHERE TABLE_NAME = 'data_exchange_config')
BEGIN
CREATE TABLE data_exchange_config
(
    table_name      NVARCHAR(255) NOT NULL PRIMARY KEY,
    source_db       NVARCHAR(255) NOT NULL,
    query NVARCHAR(MAX) NOT NULL,
    query_with_limit NVARCHAR(MAX) NULL,
    created_at      DATETIME2 DEFAULT GETDATE(),
    updated_at      DATETIME2 DEFAULT GETDATE()
);
END
GO
