ALTER TABLE data_view_config
    ADD cross_db_applied BIT NOT NULL CONSTRAINT DF_data_view_config_cross_db_applied DEFAULT 0;
