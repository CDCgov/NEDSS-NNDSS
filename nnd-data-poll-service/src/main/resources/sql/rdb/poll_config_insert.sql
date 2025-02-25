
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('D_ORGANIZATION', 'RDB',1, 'ORGANIZATION_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('BMIRD_MULTI_VALUE_FIELD_GROUP', 'RDB',2, 'BMIRD_MULTI_VAL_GRP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('D_PCR_SOURCE_GROUP', 'RDB',3, 'D_PCR_SOURCE_GROUP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('D_RASH_LOC_GEN_GROUP', 'RDB',4, 'D_RASH_LOC_GEN_GROUP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('D_VAR_PAM', 'RDB',5, 'D_VAR_PAM_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('D_PROVIDER', 'RDB',6, 'PROVIDER_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_CASE_MANAGEMENT', 'RDB',7);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_INTERVIEW', 'RDB',8);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_INV_ADMINISTRATIVE', 'RDB',9);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_INV_EPIDEMIOLOGY', 'RDB',10);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_INV_HIV', 'RDB',11);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_INV_LAB_FINDING', 'RDB',12);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_INV_MEDICAL_HISTORY', 'RDB',13);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_INV_RISK_FACTOR', 'RDB',14);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_INV_TREATMENT', 'RDB',15);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_INV_VACCINATION', 'RDB',16);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('D_PATIENT', 'RDB',17, 'PATIENT_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('F_INTERVIEW_CASE', 'RDB',18);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('F_PAGE_CASE', 'RDB',19);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('F_STD_PAGE_CASE', 'RDB',20);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, recreate_applied)
VALUES('F_VAR_PAM', 'RDB',21, 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('CONDITION', 'RDB',22, 'CONDITION_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('INVESTIGATION', 'RDB',23, 'INVESTIGATION_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('RDB_DATE', 'RDB',24, 'DATE_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('CONFIRMATION_METHOD', 'RDB',25, 'CONFIRMATION_METHOD_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('LDF_GROUP', 'RDB',26, 'LDF_GROUP', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('HEP_MULTI_VALUE_FIELD_GROUP', 'RDB',27, 'HEP_MULTI_VAL_GRP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('NOTIFICATION', 'RDB',28, 'NOTIFICATION_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('PERTUSSIS_SUSPECTED_SOURCE_GRP', 'RDB',29, 'PERTUSSIS_SUSPECT_SRC_GRP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('PERTUSSIS_TREATMENT_GROUP', 'RDB',30, 'PERTUSSIS_TREATMENT_GROUP', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('PERTUSSIS_SUSPECTED_SOURCE_FLD', 'RDB',31, 'PERTUSSIS_SUSPECT_SRC_FLD_KEY, PERTUSSIS_SUSPECT_SRC_GRP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('PERTUSSIS_TREATMENT_FIELD', 'RDB',32, 'PERTUSSIS_TREATMENT_FLD_KEY, PERTUSSIS_TREATMENT_GRP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('BMIRD_CASE', 'RDB',33,
       'ADT_HSPTL_KEY, ANTIMICROBIAL_GRP_KEY, BMIRD_MULTI_VAL_GRP_KEY, CONDITION_KEY, DAYCARE_FACILITY_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, NURSING_HOME_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY'
      , 1
      );
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('CASE_COUNT', 'RDB',34,
       'CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY',
       1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('CONFIRMATION_METHOD_GROUP', 'RDB',35, 'CONFIRMATION_METHOD_KEY, INVESTIGATION_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('GENERIC_CASE', 'RDB',36,
       'ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY'
      , 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('HEPATITIS_CASE', 'RDB',37,
       'ADT_HSPTL_KEY, CONDITION_KEY, HEP_MULTI_VAL_GRP_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY',
       1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('HEP_MULTI_VALUE_FIELD_GROUP', 'RDB',38, 'HEP_MULTI_VAL_GRP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('HEP_MULTI_VALUE_FIELD', 'RDB',39, 'HEP_MULTI_VAL_DATA_KEY, HEP_MULTI_VAL_GRP_KEY', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('HEPATITIS_DATAMART', 'RDB',40);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('LDF_DATA', 'RDB',41, 'LDF_DATA_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, recreate_applied, key_list)
VALUES('LDF_FOODBORNE', 'RDB',42, 1, 'INVESTIGATION_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('MEASLES_CASE', 'RDB',43,
       'ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INV_RPT_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY'
      , 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('NOTIFICATION_EVENT', 'RDB',44,
       'CONDITION_KEY, INVESTIGATION_KEY, NOTIFICATION_KEY, NOTIFICATION_SENT_DT_KEY, NOTIFICATION_SUBMIT_DT_KEY, PATIENT_KEY',
       1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('PERTUSSIS_CASE', 'RDB',45,
       'ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PERTUSSIS_SUSPECT_SRC_GRP_KEY, PERTUSSIS_TREATMENT_GRP_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY'
      , 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('RUBELLA_CASE', 'RDB',46,
       'ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY',
       1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('TREATMENT', 'RDB',47, 'TREATMENT_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('TREATMENT_EVENT', 'RDB',48,
       'CONDITION_KEY, INVESTIGATION_KEY, LDF_GROUP_KEY, MORB_RPT_KEY, PATIENT_KEY, TREATMENT_DT_KEY, TREATMENT_KEY, TREATMENT_PHYSICIAN_KEY, TREATMENT_PROVIDING_ORG_KEY',
       1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, recreate_applied, key_list)
VALUES('VAR_PAM_LDF', 'RDB',49, 1, 'INVESTIGATION_KEY');

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('ANTIMICROBIAL_GROUP', 'RDB',50, 'ANTIMICROBIAL_GRP_KEY', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('ANTIMICROBIAL', 'RDB',51, 'ANTIMICROBIAL_GRP_KEY, ANTIMICROBIAL_KEY', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('D_PCR_SOURCE', 'RDB',52, 'D_PCR_SOURCE_KEY, VAR_PAM_UID', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('D_RASH_LOC_GEN', 'RDB',53, 'D_RASH_LOC_GEN_KEY, VAR_PAM_UID', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('BMIRD_MULTI_VALUE_FIELD', 'RDB',54, 'BMIRD_MULTI_VAL_FIELD_KEY, BMIRD_MULTI_VAL_GRP_KEY', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('STD_HIV_DATAMART', 'RDB',55, 'INVESTIGATION_KEY', 1);



INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('PROGRAM_AREA_CODE', 'SRTE',1, 'prog_area_cd');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('CONDITION_CODE', 'SRTE',2, 'condition_cd');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, no_pagination)
VALUES('CODE_TO_CONDITION', 'SRTE',3, 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('JURISDICTION_CODE', 'SRTE',4, 'code');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('EDX_ACTIVITY_LOG', 'ODSE_EDX',1, 'edx_activity_log_uid');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('EDX_ACTIVITY_DETAIL_LOG', 'ODSE_EDX',2, 'edx_activity_detail_log_uid');


INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('NRT_OBSERVATION', 'RDB_MODERN', 1, 'observation_uid');

INSERT INTO poll_data_sync_config (table_name, source_db, table_order)
VALUES ('NRT_OBSERVATION_CODED', 'RDB_MODERN', 2);


INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('COVID_CASE_DATAMART', 'COVID_DATAMART', 1, 'COVID_CASE_DATAMART_KEY');

INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('COVID_LAB_DATAMART', 'COVID_DATAMART', 2, 'COVID_LAB_DATAMART_KEY');

INSERT INTO poll_data_sync_config (table_name, source_db, table_order, recreate_applied, no_pagination)
VALUES ('COVID_LAB_CELR_DATAMART', 'COVID_DATAMART', 3, 1, 1);


INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('ENTITY', 'ODSE_OBS', 1, 'entity_uid');
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('ROLE', 'ODSE_OBS', 2, 'subject_entity_uid, role_seq, cd');
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('PERSON', 'ODSE_OBS', 3, 'person_uid');
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('ACT', 'ODSE_OBS', 4, 'act_uid');
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('OBSERVATION', 'ODSE_OBS', 5, 'observation_uid');
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('PARTICIPATION', 'ODSE_OBS', 6, 'subject_entity_uid, act_uid, type_cd');
INSERT INTO poll_data_sync_config (table_name, source_db, table_order, key_list)
VALUES ('ACT_RELATIONSHIP', 'ODSE_OBS', 7, 'source_act_uid,target_act_uid,type_cd');