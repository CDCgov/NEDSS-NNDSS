ALTER TABLE poll_data_sync_config
    ADD key_list NVARCHAR(250);


ALTER TABLE poll_data_sync_config
    ADD etl_recreate_applied bit DEFAULT 0;


