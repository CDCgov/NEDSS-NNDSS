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


CREATE TABLE Entity_id (
                           entity_uid bigint NOT NULL,
                           entity_id_seq smallint NOT NULL,
                           add_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           add_time datetime NULL,
                           add_user_id bigint NULL,
                           assigning_authority_cd varchar(199) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           assigning_authority_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           duration_amt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           duration_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           effective_from_time datetime NULL,
                           effective_to_time datetime NULL,
                           last_chg_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           last_chg_time datetime NULL,
                           last_chg_user_id bigint NULL,
                           record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           record_status_time datetime NULL,
                           root_extension_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           status_time datetime NULL,
                           type_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           type_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           user_affiliation_txt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           valid_from_time datetime NULL,
                           valid_to_time datetime NULL,
                           as_of_date datetime NULL,
                           assigning_authority_id_type varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                           CONSTRAINT PK__Entity_id__2E1BDC42 PRIMARY KEY (entity_uid,entity_id_seq)
);

ALTER TABLE NBS_ODSE.dbo.Entity_id ADD CONSTRAINT FK__Entity_id__entit__6CD828CA FOREIGN KEY (entity_uid) REFERENCES Entity(entity_uid);


CREATE TABLE Obs_value_coded (
                                 observation_uid bigint NOT NULL,
                                 code varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                                 code_system_cd varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 code_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 code_version varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 display_name varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 original_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 alt_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 alt_cd_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 alt_cd_system_cd varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 alt_cd_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 code_derived_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 CONSTRAINT PK__Obs_value_coded__534D60F1 PRIMARY KEY (observation_uid,code)
);


-- NBS_ODSE.dbo.Obs_value_coded foreign keys

ALTER TABLE NBS_ODSE.dbo.Obs_value_coded ADD CONSTRAINT FK__Obs_value__obser__7B264821 FOREIGN KEY (observation_uid) REFERENCES Observation(observation_uid);

CREATE TABLE Obs_value_numeric (
                                   observation_uid bigint NOT NULL,
                                   obs_value_numeric_seq smallint NOT NULL,
                                   high_range varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                   low_range varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                   comparator_cd_1 varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                   numeric_value_1 numeric(15,5) NULL,
                                   numeric_value_2 numeric(15,5) NULL,
                                   numeric_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                   separator_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                   numeric_scale_1 smallint NULL,
                                   numeric_scale_2 smallint NULL,
                                   CONSTRAINT PK__Obs_value_numeri__5EBF139D PRIMARY KEY (observation_uid,obs_value_numeric_seq)
);


-- NBS_ODSE.dbo.Obs_value_numeric foreign keys

ALTER TABLE NBS_ODSE.dbo.Obs_value_numeric ADD CONSTRAINT FK__Obs_value__obser__7E02B4CC FOREIGN KEY (observation_uid) REFERENCES Observation(observation_uid);


CREATE TABLE Obs_value_txt (
                               observation_uid bigint NOT NULL,
                               obs_value_txt_seq smallint NOT NULL,
                               data_subtype_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               encoding_type_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               txt_type_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               value_image_txt image NULL,
                               value_txt varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                               CONSTRAINT PK__Obs_value_txt__628FA481 PRIMARY KEY (observation_uid,obs_value_txt_seq)
);


-- NBS_ODSE.dbo.Obs_value_txt foreign keys

ALTER TABLE NBS_ODSE.dbo.Obs_value_txt ADD CONSTRAINT FK__Obs_value__obser__7FEAFD3E FOREIGN KEY (observation_uid) REFERENCES Observation(observation_uid);



CREATE TABLE Organization (
                              organization_uid bigint NOT NULL,
                              add_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              add_time datetime NULL,
                              add_user_id bigint NULL,
                              cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              cd_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              description varchar(1000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              duration_amt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              duration_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              from_time datetime NULL,
                              last_chg_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              last_chg_time datetime NULL,
                              last_chg_user_id bigint NULL,
                              local_id varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              record_status_time datetime NULL,
                              standard_industry_class_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              standard_industry_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              status_time datetime NULL,
                              to_time datetime NULL,
                              user_affiliation_txt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              display_nm varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              street_addr1 varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              street_addr2 varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              city_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              city_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              state_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              cnty_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              cntry_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              zip_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              phone_nbr varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              phone_cntry_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              version_ctrl_nbr smallint NOT NULL,
                              electronic_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              edx_ind varchar(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                              CONSTRAINT PK__Organization__71D1E811 PRIMARY KEY (organization_uid)
);
ALTER TABLE NBS_ODSE.dbo.Organization ADD CONSTRAINT FK__Organizat__organ__0697FACD FOREIGN KEY (organization_uid) REFERENCES Entity(entity_uid);


CREATE TABLE Organization_name (
                                   organization_uid bigint NOT NULL,
                                   organization_name_seq smallint NOT NULL,
                                   nm_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                   nm_use_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                   record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                   default_nm_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                   CONSTRAINT PK__Organization_nam__75A278F5 PRIMARY KEY (organization_uid,organization_name_seq)
);


-- NBS_ODSE.dbo.Organization_name foreign keys

ALTER TABLE NBS_ODSE.dbo.Organization_name ADD CONSTRAINT FK__Organizat__organ__0880433F FOREIGN KEY (organization_uid) REFERENCES Organization(organization_uid);



CREATE TABLE Public_health_case (
                                    public_health_case_uid bigint NOT NULL,
                                    activity_duration_amt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    activity_duration_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    activity_from_time datetime NULL,
                                    activity_to_time datetime NULL,
                                    add_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    add_time datetime NULL,
                                    add_user_id bigint NULL,
                                    case_class_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    case_type_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    cd_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    cd_system_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    cd_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    confidentiality_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    confidentiality_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    detection_method_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    detection_method_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    diagnosis_time datetime NULL,
                                    disease_imported_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    disease_imported_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    effective_duration_amt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    effective_duration_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    effective_from_time datetime NULL,
                                    effective_to_time datetime NULL,
                                    group_case_cnt smallint NULL,
                                    investigation_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    jurisdiction_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    last_chg_reason_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    last_chg_time datetime NULL,
                                    last_chg_user_id bigint NULL,
                                    local_id varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    mmwr_week varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    mmwr_year varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    outbreak_ind varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    outbreak_from_time datetime NULL,
                                    outbreak_to_time datetime NULL,
                                    outbreak_name varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    outcome_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    pat_age_at_onset varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    pat_age_at_onset_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    patient_group_id bigint NULL,
                                    prog_area_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    record_status_time datetime NULL,
                                    repeat_nbr smallint NULL,
                                    rpt_cnty_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    rpt_form_cmplt_time datetime NULL,
                                    rpt_source_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    rpt_source_cd_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    rpt_to_county_time datetime NULL,
                                    rpt_to_state_time datetime NULL,
                                    status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    status_time datetime NULL,
                                    transmission_mode_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    transmission_mode_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    txt varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    user_affiliation_txt varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    program_jurisdiction_oid bigint NULL,
                                    shared_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                                    version_ctrl_nbr smallint NOT NULL,
                                    investigator_assigned_time datetime NULL,
                                    hospitalized_ind_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    hospitalized_admin_time datetime NULL,
                                    hospitalized_discharge_time datetime NULL,
                                    hospitalized_duration_amt numeric(18,0) NULL,
                                    pregnant_ind_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    day_care_ind_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    food_handler_ind_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    imported_country_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    imported_state_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    imported_city_desc_txt varchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    imported_county_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    deceased_time datetime NULL,
                                    count_interval_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    priority_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    contact_inv_txt varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    infectious_from_date datetime NULL,
                                    infectious_to_date datetime NULL,
                                    contact_inv_status_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    referral_basis_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    curr_process_state_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    inv_priority_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    coinfection_id varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                    CONSTRAINT PK__Public_health_ca__2180FB33 PRIMARY KEY (public_health_case_uid)
);


ALTER TABLE NBS_ODSE.dbo.Public_health_case ADD CONSTRAINT FK__Public_he__publi__1D7B6025 FOREIGN KEY (public_health_case_uid) REFERENCES Act(act_uid);



CREATE TABLE PublicHealthCaseFact (
                                      public_health_case_uid bigint NOT NULL,
                                      adults_in_house_nbr smallint NULL,
                                      ageInMonths smallint NULL,
                                      ageInYears smallint NULL,
                                      age_category_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      age_reported_time datetime NULL,
                                      age_reported_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      age_reported numeric(8,0) NULL,
                                      awareness_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      awareness_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      birth_gender_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      birth_order_nbr smallint NULL,
                                      birth_time datetime NULL,
                                      birth_time_calc datetime NULL,
                                      birth_time_std datetime NULL,
                                      case_class_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                                      case_type_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      cd_system_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      cd_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      census_block_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      census_minor_civil_division_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      census_track_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      cnty_code_desc_txt varchar(200) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      children_in_house_nbr smallint NULL,
                                      city_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      city_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      confidentiality_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      confidentiality_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      confirmation_method_cd varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      confirmation_method_time datetime NULL,
                                      county varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      cntry_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      cnty_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      curr_sex_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      deceased_ind_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      deceased_time datetime NULL,
                                      detection_method_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      detection_method_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      diagnosis_date datetime NULL,
                                      disease_imported_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      disease_imported_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      education_level_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      ELP_class_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      ELP_from_time datetime NULL,
                                      ELP_to_time datetime NULL,
                                      ethnic_group_ind varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      ethnic_group_ind_desc varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      event_date datetime NULL,
                                      event_type varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      education_level_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      firstNotificationSenddate datetime NULL,
                                      firstNotificationdate datetime NULL,
                                      firstNotificationStatus varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      firstNotificationSubmittedBy bigint NULL,
                                      geoLatitude real NULL,
                                      geoLongitude real NULL,
                                      group_case_cnt numeric(11,5) NULL,
                                      investigation_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      investigatorAssigneddate datetime NULL,
                                      investigatorName varchar(102) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      investigatorPhone varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      jurisdiction_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      lastNotificationdate datetime NULL,
                                      lastNotificationSenddate datetime NULL,
                                      lastNotificationSubmittedBy bigint NULL,
                                      marital_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      marital_status_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      mart_record_creation_date datetime NULL,
                                      mart_record_creation_time datetime NULL,
                                      mmwr_week numeric(8,0) NULL,
                                      mmwr_year numeric(8,0) NULL,
                                      MSA_congress_district_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      multiple_birth_ind varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      notifCreatedCount int NULL,
                                      notificationdate datetime NULL,
                                      notifSentCount int NULL,
                                      occupation_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      onSetDate datetime NULL,
                                      organizationName varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      outcome_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      outbreak_from_time datetime NULL,
                                      outbreak_ind varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      outbreak_name varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      outbreak_to_time datetime NULL,
                                      PAR_type_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      pat_age_at_onset numeric(8,0) NULL,
                                      pat_age_at_onset_unit_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      postal_locator_uid bigint NULL,
                                      person_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      person_code_desc varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      person_uid bigint NULL,
                                      PHC_add_time datetime NULL,
                                      PHC_code varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                                      PHC_code_desc varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                                      PHC_code_short_desc varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      prim_lang_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      prim_lang_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      prog_area_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                                      providerPhone varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      providerName varchar(102) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      PST_record_status_time datetime NULL,
                                      PST_record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      race_concatenated_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      race_concatenated_desc_txt varchar(500) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      region_district_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      reporterName varchar(102) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      reporterPhone varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      rpt_cnty_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      rpt_form_cmplt_time datetime NULL,
                                      rpt_source_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      rpt_source_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      rpt_to_county_time datetime NULL,
                                      rpt_to_state_time datetime NULL,
                                      shared_ind varchar(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      state varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      state_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      state_code_short_desc_txt varchar(200) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      street_addr1 varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      street_addr2 varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      ELP_use_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      zip_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      patientName varchar(102) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      jurisdiction varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      investigationstartdate datetime NULL,
                                      program_jurisdiction_oid bigint NULL,
                                      report_date datetime NULL,
                                      person_parent_uid bigint NULL,
                                      person_local_id varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      sub_addr_as_of_date datetime NULL,
                                      state_case_id varchar(199) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      LOCAL_ID varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      NOTIFCURRENTSTATE varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      age_reported_unit_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      birth_gender_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      case_class_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      cntry_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      curr_sex_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      investigation_status_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      occupation_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      outcome_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      pat_age_at_onset_unit_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      prog_area_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      rpt_cnty_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      outbreak_name_desc varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      confirmation_method_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      LASTUPDATE datetime NULL,
                                      PHCTXT varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      NOTITXT varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      NOTIFICATION_LOCAL_ID varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      HSPTL_ADMISSION_DT datetime NULL,
                                      HSPTL_DISCHARGE_DT datetime NULL,
                                      hospitalized_ind varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                      CONSTRAINT PK__PublicHealthCase__25518C17 PRIMARY KEY (public_health_case_uid)
);