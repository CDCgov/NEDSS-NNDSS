IF NOT EXISTS(SELECT *
              FROM sys.databases
              WHERE name = 'RDB')
BEGIN
    CREATE DATABASE RDB;
END
GO