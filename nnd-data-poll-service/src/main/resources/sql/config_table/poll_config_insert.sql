
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('D_ORGANIZATION', 'RDB',1, 'ORGANIZATION_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('BMIRD_MULTI_VALUE_FIELD_GROUP', 'RDB',1, 'BMIRD_MULTI_VAL_GRP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('D_PCR_SOURCE_GROUP', 'RDB',1, 'D_PCR_SOURCE_GROUP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('D_RASH_LOC_GEN_GROUP', 'RDB',1, 'D_RASH_LOC_GEN_GROUP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('D_VAR_PAM', 'RDB',1, 'D_VAR_PAM_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('D_PROVIDER', 'RDB',1, 'PROVIDER_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_CASE_MANAGEMENT', 'RDB',2);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('D_INTERVIEW', 'RDB',1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, use_key_pagination)
VALUES('D_INV_ADMINISTRATIVE', 'RDB',4, 'D_INV_ADMINISTRATIVE_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, use_key_pagination)
VALUES('D_INV_EPIDEMIOLOGY', 'RDB',4, 'D_INV_EPIDEMIOLOGY_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, use_key_pagination)
VALUES('D_INV_HIV', 'RDB',4, 'D_INV_HIV_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, use_key_pagination)
VALUES('D_INV_LAB_FINDING', 'RDB',4, 'D_INV_LAB_FINDING_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, use_key_pagination)
VALUES('D_INV_MEDICAL_HISTORY', 'RDB',4, 'D_INV_MEDICAL_HISTORY_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, use_key_pagination)
VALUES('D_INV_RISK_FACTOR', 'RDB',4, 'D_INV_RISK_FACTOR_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, use_key_pagination)
VALUES('D_INV_TREATMENT', 'RDB',4, 'D_INV_TREATMENT_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, use_key_pagination)
VALUES('D_INV_VACCINATION', 'RDB',4, 'D_INV_VACCINATION_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('D_PATIENT', 'RDB',1, 'PATIENT_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('F_INTERVIEW_CASE', 'RDB',1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('F_PAGE_CASE', 'RDB',1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('F_STD_PAGE_CASE', 'RDB',1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, recreate_applied)
VALUES('F_VAR_PAM', 'RDB',2, 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('CONDITION', 'RDB',1, 'CONDITION_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('INVESTIGATION', 'RDB',1, 'INVESTIGATION_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('RDB_DATE', 'RDB',1, 'DATE_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, no_pagination, recreate_applied)
VALUES('CONFIRMATION_METHOD', 'RDB',2, 1, 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('LDF_GROUP', 'RDB',1, 'LDF_GROUP', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('HEP_MULTI_VALUE_FIELD_GROUP', 'RDB',1, 'HEP_MULTI_VAL_GRP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('NOTIFICATION', 'RDB',1, 'NOTIFICATION_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('PERTUSSIS_SUSPECTED_SOURCE_GRP', 'RDB',1, 'PERTUSSIS_SUSPECT_SRC_GRP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('PERTUSSIS_TREATMENT_GROUP', 'RDB',1, 'PERTUSSIS_TREATMENT_GROUP', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('PERTUSSIS_SUSPECTED_SOURCE_FLD', 'RDB',2, 'PERTUSSIS_SUSPECT_SRC_FLD_KEY, PERTUSSIS_SUSPECT_SRC_GRP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('PERTUSSIS_TREATMENT_FIELD', 'RDB',2, 'PERTUSSIS_TREATMENT_FLD_KEY, PERTUSSIS_TREATMENT_GRP_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('BMIRD_CASE', 'RDB',2,
       'ADT_HSPTL_KEY, ANTIMICROBIAL_GRP_KEY, BMIRD_MULTI_VAL_GRP_KEY, CONDITION_KEY, DAYCARE_FACILITY_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, NURSING_HOME_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY'
      , 1
      );
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('CASE_COUNT', 'RDB',2,
       'CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY',
       1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('CONFIRMATION_METHOD_GROUP', 'RDB',3, 'CONFIRMATION_METHOD_KEY, INVESTIGATION_KEY');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('GENERIC_CASE', 'RDB',2,
       'ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY'
      , 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('HEPATITIS_CASE', 'RDB',2,
       'ADT_HSPTL_KEY, CONDITION_KEY, HEP_MULTI_VAL_GRP_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY',
       1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('HEP_MULTI_VALUE_FIELD', 'RDB',2, 'HEP_MULTI_VAL_DATA_KEY, HEP_MULTI_VAL_GRP_KEY', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order)
VALUES('HEPATITIS_DATAMART', 'RDB',1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('LDF_DATA', 'RDB',1, 'LDF_DATA_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, recreate_applied)
VALUES('LDF_FOODBORNE', 'RDB',1, 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('MEASLES_CASE', 'RDB',2,
       'ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INV_RPT_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY'
      , 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('NOTIFICATION_EVENT', 'RDB',2,
       'CONDITION_KEY, INVESTIGATION_KEY, NOTIFICATION_KEY, NOTIFICATION_SENT_DT_KEY, NOTIFICATION_SUBMIT_DT_KEY, PATIENT_KEY',
       1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('PERTUSSIS_CASE', 'RDB',2,
       'ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PERTUSSIS_SUSPECT_SRC_GRP_KEY, PERTUSSIS_TREATMENT_GRP_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY'
      , 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('RUBELLA_CASE', 'RDB',2,
       'ADT_HSPTL_KEY, CONDITION_KEY, INV_ASSIGNED_DT_KEY, INVESTIGATION_KEY, INVESTIGATOR_KEY, LDF_GROUP_KEY, PATIENT_KEY, PHYSICIAN_KEY, REPORTER_KEY, RPT_SRC_ORG_KEY',
       1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('TREATMENT', 'RDB',1, 'TREATMENT_KEY', 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('TREATMENT_EVENT', 'RDB',1,
       'CONDITION_KEY, INVESTIGATION_KEY, LDF_GROUP_KEY, MORB_RPT_KEY, PATIENT_KEY, TREATMENT_DT_KEY, TREATMENT_KEY, TREATMENT_PHYSICIAN_KEY, TREATMENT_PROVIDING_ORG_KEY',
       1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, recreate_applied)
VALUES('VAR_PAM_LDF', 'RDB',1, 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('ANTIMICROBIAL_GROUP', 'RDB',1, 'ANTIMICROBIAL_GRP_KEY', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('ANTIMICROBIAL', 'RDB',2, 'ANTIMICROBIAL_GRP_KEY, ANTIMICROBIAL_KEY', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('D_PCR_SOURCE', 'RDB',2, 'D_PCR_SOURCE_KEY, VAR_PAM_UID', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('D_RASH_LOC_GEN', 'RDB',2, 'D_RASH_LOC_GEN_KEY, VAR_PAM_UID', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list, recreate_applied)
VALUES('BMIRD_MULTI_VALUE_FIELD', 'RDB',2, 'BMIRD_MULTI_VAL_FIELD_KEY, BMIRD_MULTI_VAL_GRP_KEY', 1);

INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, recreate_applied)
VALUES('STD_HIV_DATAMART', 'RDB',2, 1);



INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('PROGRAM_AREA_CODE', 'SRTE',1, 'prog_area_cd');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('CONDITION_CODE', 'SRTE',2, 'condition_cd');
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, no_pagination)
VALUES('CODE_TO_CONDITION', 'SRTE',1, 1);
INSERT INTO poll_data_sync_config
(table_name, source_db,table_order, key_list)
VALUES('JURISDICTION_CODE', 'SRTE',1, 'code');
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
VALUES ('COVID_LAB_DATAMART', 'COVID_DATAMART', 1, 'COVID_LAB_DATAMART_KEY');

INSERT INTO poll_data_sync_config (table_name, source_db, table_order, recreate_applied, no_pagination)
VALUES ('COVID_LAB_CELR_DATAMART', 'COVID_DATAMART', 1, 1, 1);


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