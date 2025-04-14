ALTER TABLE data_sync_config
    ADD
        meta_data NVARCHAR(255) NULL,
        datasync_applied BIT NOT NULL DEFAULT 1;