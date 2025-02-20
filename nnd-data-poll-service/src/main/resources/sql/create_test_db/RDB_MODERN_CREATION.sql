-- rdb_modern.dbo.nrt_observation definition

-- Drop table

-- DROP TABLE rdb_modern.dbo.nrt_observation;

CREATE TABLE nrt_observation (
                                 observation_uid bigint NOT NULL,
                                 class_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 mood_cd varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 act_uid bigint NULL,
                                 cd_desc_txt varchar(1000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 record_status_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 jurisdiction_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 program_jurisdiction_oid bigint NULL,
                                 prog_area_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 pregnant_ind_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 local_id varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 activity_to_time datetime NULL,
                                 effective_from_time datetime NULL,
                                 rpt_to_state_time datetime NULL,
                                 electronic_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 version_ctrl_nbr smallint NOT NULL,
                                 ordering_person_id bigint NULL,
                                 patient_id bigint NULL,
                                 result_observation_uid nvarchar(MAX) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 author_organization_id bigint NULL,
                                 ordering_organization_id bigint NULL,
                                 performing_organization_id bigint NULL,
                                 material_id bigint NULL,
                                 add_user_id bigint NULL,
                                 add_user_name varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 add_time datetime NULL,
                                 last_chg_user_id bigint NULL,
                                 last_chg_user_name varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 last_chg_time datetime NULL,
                                 refresh_datetime datetime2 NOT NULL,
                                 cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 shared_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 ctrl_cd_display_form varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 obs_domain_cd_st_1 varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 processing_decision_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 cd_system_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 cd_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 ctrl_cd_user_defined_1 varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 alt_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 alt_cd_desc_txt varchar(1000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 alt_cd_system_cd varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 alt_cd_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 method_cd varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 method_desc_txt varchar(2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 target_site_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 target_site_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 txt varchar(1000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 interpretation_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 interpretation_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 report_observation_uid bigint NULL,
                                 followup_observation_uid nvarchar(MAX) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 report_refr_uid bigint NULL,
                                 report_sprt_uid bigint NULL,
                                 morb_physician_id bigint NULL,
                                 morb_reporter_id bigint NULL,
                                 transcriptionist_id bigint NULL,
                                 transcriptionist_val varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 transcriptionist_first_nm varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 transcriptionist_last_nm varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 assistant_interpreter_id bigint NULL,
                                 assistant_interpreter_val varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 assistant_interpreter_first_nm varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 assistant_interpreter_last_nm varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 result_interpreter_id bigint NULL,
                                 specimen_collector_id bigint NULL,
                                 copy_to_provider_id bigint NULL,
                                 lab_test_technician_id bigint NULL,
                                 health_care_id bigint NULL,
                                 morb_hosp_reporter_id bigint NULL,
                                 accession_number varchar(199) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 morb_hosp_id bigint NULL,
                                 transcriptionist_auth_type varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 assistant_interpreter_auth_type varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 priority_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 transcriptionist_id_assign_auth varchar(199) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 assistant_interpreter_id_assign_auth varchar(199) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                 record_status_time datetime NULL,
                                 status_time datetime NULL,
                                 CONSTRAINT PK__nrt_obse__FA4A1A9B0DBF251C PRIMARY KEY (observation_uid)
);



-- rdb_modern.dbo.nrt_observation_coded definition

-- Drop table

-- DROP TABLE rdb_modern.dbo.nrt_observation_coded;

CREATE TABLE nrt_observation_coded (
                                       observation_uid bigint NOT NULL,
                                       ovc_code varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
                                       ovc_code_system_cd varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                       ovc_code_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                       ovc_display_name varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                       ovc_alt_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                       ovc_alt_cd_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                       ovc_alt_cd_system_cd varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                       ovc_alt_cd_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
                                       refresh_datetime datetime2 NOT NULL
);
CREATE NONCLUSTERED INDEX idx_nrt_obs_coded_obs_uid ON dbo.nrt_observation_coded (  observation_uid ASC  )
    WITH (  PAD_INDEX = OFF ,FILLFACTOR = 100  ,SORT_IN_TEMPDB = OFF , IGNORE_DUP_KEY = OFF , STATISTICS_NORECOMPUTE = OFF , ONLINE = OFF , ALLOW_ROW_LOCKS = ON , ALLOW_PAGE_LOCKS = ON  )
    ON [PRIMARY ] ;
CREATE NONCLUSTERED INDEX idx_nrt_obs_coded_obs_uid_code ON dbo.nrt_observation_coded (  observation_uid ASC  , ovc_code ASC  )
    WITH (  PAD_INDEX = OFF ,FILLFACTOR = 100  ,SORT_IN_TEMPDB = OFF , IGNORE_DUP_KEY = OFF , STATISTICS_NORECOMPUTE = OFF , ONLINE = OFF , ALLOW_ROW_LOCKS = ON , ALLOW_PAGE_LOCKS = ON  )
    ON [PRIMARY ] ;