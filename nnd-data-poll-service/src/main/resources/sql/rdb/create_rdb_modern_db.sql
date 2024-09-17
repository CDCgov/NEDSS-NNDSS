IF NOT EXISTS(SELECT *
              FROM sys.databases
              WHERE name = 'RDB_MODERN')
BEGIN
    CREATE DATABASE RDB_MODERN;
END
GO