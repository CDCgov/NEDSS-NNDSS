IF NOT EXISTS(SELECT *
              FROM sys.databases
              WHERE name = 'NBS_SRTE')
BEGIN
    CREATE DATABASE RDB_MODERN;
END
GO