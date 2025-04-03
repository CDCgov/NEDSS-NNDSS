INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list, use_key_pagination)
VALUES ('ENTITY', 'ODSE_OBS', 1, 'entity_uid', 1);
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('ROLE', 'ODSE_OBS', 2, 'subject_entity_uid, role_seq, cd');
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('PERSON', 'ODSE_OBS', 3, 'person_uid');
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list, use_key_pagination)
VALUES ('ACT', 'ODSE_OBS', 4, 'act_uid', 1);
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('OBSERVATION', 'ODSE_OBS', 5, 'observation_uid');
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('PARTICIPATION', 'ODSE_OBS', 6, 'subject_entity_uid, act_uid, type_cd');
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('ACT_RELATIONSHIP', 'ODSE_OBS', 7, 'source_act_uid,target_act_uid,type_cd');