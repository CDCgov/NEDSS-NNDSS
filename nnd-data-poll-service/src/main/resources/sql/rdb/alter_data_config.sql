ALTER TABLE poll_data_sync_config
    ADD key_list NVARCHAR(250);


ALTER TABLE poll_data_sync_config
    ADD recreate_applied bit DEFAULT 0;


