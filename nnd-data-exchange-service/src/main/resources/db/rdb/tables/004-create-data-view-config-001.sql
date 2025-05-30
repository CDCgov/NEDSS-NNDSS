IF NOT EXISTS(
    SELECT 'X'
    FROM INFORMATION_SCHEMA.TABLES
    WHERE TABLE_NAME = 'data_view_config')
    BEGIN
        CREATE TABLE data_view_config
        (
            id                         BIGINT PRIMARY KEY IDENTITY(1,1),
            query_name                 NVARCHAR(255) NOT NULL UNIQUE,
            source_db                  NVARCHAR(255) NOT NULL,
            query                      NVARCHAR(MAX) NOT NULL,
            meta_data                  NVARCHAR(MAX) NULL,
            custom_param_applied       BIT NOT NULL DEFAULT 0,
            created_at                 DATETIME2 DEFAULT GETDATE(),
            updated_at                 DATETIME2 DEFAULT GETDATE()
        );
    END