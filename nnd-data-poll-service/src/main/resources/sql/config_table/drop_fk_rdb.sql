DECLARE @sql NVARCHAR(MAX);
DECLARE @tableName SYSNAME;

-- Declare a table variable to store table names in the specified order
DECLARE @tables TABLE (TableOrder INT IDENTITY(1,1), TableName SYSNAME);

-- Insert tables in the given order
INSERT INTO @tables (TableName) VALUES
                                    ('D_INV_ADMINISTRATIVE'),
                                    ('D_INV_EPIDEMIOLOGY'),
                                    ('D_INV_HIV'),
                                    ('D_INV_LAB_FINDING'),
                                    ('D_INV_MEDICAL_HISTORY'),
                                    ('D_INV_RISK_FACTOR'),
                                    ('D_INV_TREATMENT'),
                                    ('D_INV_VACCINATION'),
                                    ('CONFIRMATION_METHOD_GROUP'),
                                    ('D_CASE_MANAGEMENT'),
                                    ('CASE_COUNT'),
                                    ('ANTIMICROBIAL'),
                                    ('BMIRD_CASE'),
                                    ('BMIRD_MULTI_VALUE_FIELD'),
                                    ('D_RASH_LOC_GEN'),
                                    ('F_VAR_PAM'),
                                    ('GENERIC_CASE'),
                                    ('HEP_MULTI_VALUE_FIELD'),
                                    ('HEPATITIS_CASE'),
                                    ('D_PCR_SOURCE'),
                                    ('MEASLES_CASE'),
                                    ('NOTIFICATION_EVENT'),
                                    ('PERTUSSIS_CASE'),
                                    ('PERTUSSIS_SUSPECTED_SOURCE_FLD'),
                                    ('RUBELLA_CASE'),
                                    ('STD_HIV_DATAMART'),
                                    ('PERTUSSIS_TREATMENT_FIELD'),
                                    ('CONFIRMATION_METHOD'),
                                    ('PERTUSSIS_TREATMENT_GROUP'),
                                    ('RDB_DATE'),
                                    ('TREATMENT'),
                                    ('TREATMENT_EVENT'),
                                    ('VAR_PAM_LDF'),
                                    ('PERTUSSIS_SUSPECTED_SOURCE_GRP'),
                                    ('NOTIFICATION'),
                                    ('D_PCR_SOURCE_GROUP'),
                                    ('D_PROVIDER'),
                                    ('HEPATITIS_DATAMART'),
                                    ('INVESTIGATION'),
                                    ('LDF_DATA'),
                                    ('LDF_FOODBORNE'),
                                    ('LDF_GROUP'),
                                    ('HEP_MULTI_VALUE_FIELD_GROUP'),
                                    ('D_RASH_LOC_GEN_GROUP'),
                                    ('D_VAR_PAM'),
                                    ('F_INTERVIEW_CASE'),
                                    ('F_PAGE_CASE'),
                                    ('F_STD_PAGE_CASE'),
                                    ('BMIRD_MULTI_VALUE_FIELD_GROUP'),
                                    ('ANTIMICROBIAL_GROUP'),
                                    ('CONDITION'),
                                    ('D_INTERVIEW'),
                                    ('D_ORGANIZATION'),
                                    ('D_PATIENT');

-- Cursor to process each table in order
DECLARE table_cursor CURSOR FOR
SELECT TableName FROM @tables ORDER BY TableOrder;

OPEN table_cursor;
FETCH NEXT FROM table_cursor INTO @tableName;

WHILE @@FETCH_STATUS = 0
BEGIN
    SET @sql = N'';

    -- Generate DROP CONSTRAINT statements for foreign keys only
SELECT @sql += 'ALTER TABLE ' + QUOTENAME(OBJECT_SCHEMA_NAME(parent_object_id))
                   + '.' + QUOTENAME(OBJECT_NAME(parent_object_id))
                   + ' DROP CONSTRAINT ' + QUOTENAME(name) + ';' + CHAR(13)
FROM sys.foreign_keys
WHERE parent_object_id = OBJECT_ID(@tableName);

-- Print and execute the generated SQL
PRINT @sql;
EXEC sp_executesql @sql;

FETCH NEXT FROM table_cursor INTO @tableName;
END;

CLOSE table_cursor;
DEALLOCATE table_cursor;
