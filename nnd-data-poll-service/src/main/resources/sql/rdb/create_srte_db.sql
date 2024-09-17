IF NOT EXISTS(SELECT *
              FROM sys.databases
              WHERE name = 'SRTE')
BEGIN
    CREATE DATABASE SRTE;
END
GO