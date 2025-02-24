CREATE TABLE EDX_activity_log (
                                  edx_activity_log_uid bigint IDENTITY(1001,1) NOT NULL,
                                  source_uid bigint NULL,
                                  target_uid bigint NULL,
                                  doc_type varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  record_status_time datetime NULL,
                                  exception_txt text COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  imp_exp_ind_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  source_type_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  target_type_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  business_obj_localId varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  doc_nm varchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  source_nm varchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  algorithm_action varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  algorithm_name varchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  Message_id varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  Entity_nm varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  Accession_nbr varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  CONSTRAINT PK_EDX_Activity_Log PRIMARY KEY (edx_activity_log_uid)
);


CREATE TABLE EDX_activity_detail_log (
                                         edx_activity_detail_log_uid bigint IDENTITY(1001,1) NOT NULL,
                                         edx_activity_log_uid bigint NOT NULL,
                                         record_id varchar(256) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                         record_type varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                         record_nm varchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                         log_type varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                         log_comment varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                         CONSTRAINT PK_EDX_activity_detail_log PRIMARY KEY (edx_activity_detail_log_uid)
);

ALTER TABLE EDX_activity_detail_log ADD CONSTRAINT FK_EDX_activity_detail_log_4021 FOREIGN KEY (edx_activity_log_uid) REFERENCES EDX_activity_log(edx_activity_log_uid);


CREATE TABLE Entity (
                        entity_uid bigint NOT NULL,
                        class_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                        CONSTRAINT PK__Entity__286302EC PRIMARY KEY (entity_uid)
);


CREATE TABLE [Role] (
                        subject_entity_uid bigint NOT NULL,
                        cd varchar(40) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                        role_seq bigint NOT NULL,
                        add_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        add_time datetime NULL,
                        add_user_id bigint NULL,
                        cd_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        effective_duration_amt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        effective_duration_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        effective_from_time datetime NULL,
                        effective_to_time datetime NULL,
                        last_chg_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        last_chg_time datetime NULL,
                        last_chg_user_id bigint NULL,
                        record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        record_status_time datetime NULL,
                        scoping_class_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        scoping_entity_uid bigint NULL,
                        scoping_role_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        scoping_role_seq smallint NULL,
                        status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                        status_time datetime NULL,
                        subject_class_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        user_affiliation_txt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        CONSTRAINT PK__Role__2EDAF651 PRIMARY KEY (subject_entity_uid,role_seq,cd)
);

-- [Role] foreign keys

ALTER TABLE [Role] ADD CONSTRAINT FK__Role__subject_en__251C81ED FOREIGN KEY (subject_entity_uid) REFERENCES Entity(entity_uid);


-- Person definition

-- Drop table

-- DROP TABLE Person;

CREATE TABLE Person (
                        person_uid bigint NOT NULL,
                        add_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        add_time datetime NULL,
                        add_user_id bigint NULL,
                        administrative_gender_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        age_calc smallint NULL,
                        age_calc_time datetime NULL,
                        age_calc_unit_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        age_category_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        age_reported varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        age_reported_time datetime NULL,
                        age_reported_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        birth_gender_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        birth_order_nbr smallint NULL,
                        birth_time datetime NULL,
                        birth_time_calc datetime NULL,
                        cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        cd_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        curr_sex_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        deceased_ind_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        deceased_time datetime NULL,
                        description varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        education_level_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        education_level_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        ethnic_group_ind varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        last_chg_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        last_chg_time datetime NULL,
                        last_chg_user_id bigint NULL,
                        local_id varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        marital_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        marital_status_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        mothers_maiden_nm varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        multiple_birth_ind varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        occupation_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        preferred_gender_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        prim_lang_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        prim_lang_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        record_status_time datetime NULL,
                        status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        status_time datetime NULL,
                        survived_ind_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        user_affiliation_txt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        first_nm varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        last_nm varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        middle_nm varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        nm_prefix varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        nm_suffix varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        preferred_nm varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        hm_street_addr1 varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        hm_street_addr2 varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        hm_city_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        hm_city_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        hm_state_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        hm_zip_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        hm_cnty_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        hm_cntry_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        hm_phone_nbr varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        hm_phone_cntry_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        hm_email_addr varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        cell_phone_nbr varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        wk_street_addr1 varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        wk_street_addr2 varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        wk_city_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        wk_city_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        wk_state_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        wk_zip_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        wk_cnty_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        wk_cntry_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        wk_phone_nbr varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        wk_phone_cntry_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        wk_email_addr varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        SSN varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        medicaid_num varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        dl_num varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        dl_state_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        race_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        race_seq_nbr smallint NULL,
                        race_category_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        ethnicity_group_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        ethnic_group_seq_nbr smallint NULL,
                        adults_in_house_nbr smallint NULL,
                        children_in_house_nbr smallint NULL,
                        birth_city_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        birth_city_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        birth_cntry_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        birth_state_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        race_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        ethnic_group_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        version_ctrl_nbr smallint NOT NULL,
                        as_of_date_admin datetime NULL,
                        as_of_date_ethnicity datetime NULL,
                        as_of_date_general datetime NULL,
                        as_of_date_morbidity datetime NULL,
                        as_of_date_sex datetime NULL,
                        electronic_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        person_parent_uid bigint NULL,
                        dedup_match_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        group_nbr int NULL,
                        group_time datetime NULL,
                        edx_ind varchar(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        speaks_english_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        additional_gender_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        ehars_id varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        ethnic_unk_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        sex_unk_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                        CONSTRAINT PK__Person__01142BA1 PRIMARY KEY (person_uid)
);
-- Person foreign keys

ALTER TABLE Person ADD CONSTRAINT FK__Person__person_p__0F2D40CE FOREIGN KEY (person_parent_uid) REFERENCES Person(person_uid);
ALTER TABLE Person ADD CONSTRAINT FK__Person__person_u__10216507 FOREIGN KEY (person_uid) REFERENCES Entity(entity_uid);




-- Act definition

-- Drop table

-- DROP TABLE Act;

CREATE TABLE Act (
                     act_uid bigint NOT NULL,
                     class_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                     mood_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                     CONSTRAINT PK__Act__76CBA758 PRIMARY KEY (act_uid)
);


-- Observation definition

-- Drop table

-- DROP TABLE Observation;

CREATE TABLE Observation (
                             observation_uid bigint NOT NULL,
                             activity_duration_amt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             activity_duration_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             activity_from_time datetime NULL,
                             activity_to_time datetime NULL,
                             add_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             add_time datetime NULL,
                             add_user_id bigint NULL,
                             cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             cd_desc_txt varchar(1000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             cd_system_cd varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             cd_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             confidentiality_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             confidentiality_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             ctrl_cd_display_form varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             ctrl_cd_user_defined_1 varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             ctrl_cd_user_defined_2 varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             ctrl_cd_user_defined_3 varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             ctrl_cd_user_defined_4 varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             derivation_exp smallint NULL,
                             effective_duration_amt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             effective_duration_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             effective_from_time datetime NULL,
                             effective_to_time datetime NULL,
                             electronic_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             group_level_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             jurisdiction_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             lab_condition_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             last_chg_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             last_chg_time datetime NULL,
                             last_chg_user_id bigint NULL,
                             local_id varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             method_cd varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             method_desc_txt varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             obs_domain_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             obs_domain_cd_st_1 varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             pnu_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             priority_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             priority_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             prog_area_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             record_status_time datetime NULL,
                             repeat_nbr smallint NULL,
                             status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             status_time datetime NULL,
                             subject_person_uid bigint NULL,
                             target_site_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             target_site_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             txt varchar(1000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             user_affiliation_txt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             value_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             ynu_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             program_jurisdiction_oid bigint NULL,
                             shared_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                             version_ctrl_nbr smallint NOT NULL,
                             alt_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             alt_cd_desc_txt varchar(1000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             alt_cd_system_cd varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             alt_cd_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             cd_derived_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             rpt_to_state_time datetime NULL,
                             cd_version varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             processing_decision_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             pregnant_ind_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             pregnant_week smallint NULL,
                             processing_decision_txt varchar(1000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                             CONSTRAINT PK__Observation__66603565 PRIMARY KEY (observation_uid)
);


-- Observation foreign keys

ALTER TABLE Observation ADD CONSTRAINT FK__Observati__obser__01D345B0 FOREIGN KEY (observation_uid) REFERENCES Act(act_uid);
ALTER TABLE Observation ADD CONSTRAINT FK__Observati__subje__02C769E9 FOREIGN KEY (subject_person_uid) REFERENCES Person(person_uid);


-- Participation definition

-- Drop table

-- DROP TABLE Participation;

CREATE TABLE Participation (
                               subject_entity_uid bigint NOT NULL,
                               act_uid bigint NOT NULL,
                               type_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                               act_class_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               add_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               add_time datetime NULL,
                               add_user_id bigint NULL,
                               awareness_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               awareness_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               cd varchar(40) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               duration_amt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               duration_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               from_time datetime NULL,
                               last_chg_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               last_chg_time datetime NULL,
                               last_chg_user_id bigint NULL,
                               record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               record_status_time datetime NULL,
                               role_seq bigint NULL,
                               status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               status_time datetime NULL,
                               subject_class_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               to_time datetime NULL,
                               type_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               user_affiliation_txt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               CONSTRAINT PK__Participation__797309D9 PRIMARY KEY (subject_entity_uid,act_uid,type_cd)
);


-- Participation foreign keys

ALTER TABLE Participation ADD CONSTRAINT FK__Participa__act_u__0A688BB1 FOREIGN KEY (act_uid) REFERENCES Act(act_uid);
ALTER TABLE Participation ADD CONSTRAINT FK__Participa__subje__0C50D423 FOREIGN KEY (subject_entity_uid) REFERENCES Entity(entity_uid);
ALTER TABLE Participation ADD CONSTRAINT FK__Participation__0 FOREIGN KEY (subject_entity_uid,role_seq,cd) REFERENCES [Role](subject_entity_uid,role_seq,cd);


-- Act_relationship definition

-- Drop table

-- DROP TABLE Act_relationship;

CREATE TABLE Act_relationship (
                                  target_act_uid bigint NOT NULL,
                                  source_act_uid bigint NOT NULL,
                                  type_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                                  add_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  add_time datetime NULL,
                                  add_user_id bigint NULL,
                                  duration_amt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  duration_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  from_time datetime NULL,
                                  last_chg_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  last_chg_time datetime NULL,
                                  last_chg_user_id bigint NULL,
                                  record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  record_status_time datetime NULL,
                                  sequence_nbr smallint NULL,
                                  source_class_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  status_time datetime NULL,
                                  target_class_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  to_time datetime NULL,
                                  type_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  user_affiliation_txt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                  CONSTRAINT PK__Act_relationship__00551192 PRIMARY KEY (source_act_uid,target_act_uid,type_cd)
);


-- Act_relationship foreign keys

ALTER TABLE Act_relationship ADD CONSTRAINT FK__Act_relat__sourc__5E8A0973 FOREIGN KEY (source_act_uid) REFERENCES Act(act_uid);
ALTER TABLE Act_relationship ADD CONSTRAINT FK__Act_relat__targe__5D95E53A FOREIGN KEY (target_act_uid) REFERENCES Act(act_uid);