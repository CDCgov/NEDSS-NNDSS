

CREATE TABLE NBS_SRTE.dbo.Condition_code (
	condition_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	condition_codeset_nm varchar(256) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	condition_seq_num smallint NULL,
	assigning_authority_cd varchar(199) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	assigning_authority_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code_system_cd varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	condition_desc_txt varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	condition_short_nm varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	effective_from_time datetime NULL,
	effective_to_time datetime NULL,
	indent_level_nbr smallint NULL,
	investigation_form_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	is_modifiable_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	nbs_uid bigint NULL,
	nnd_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	parent_is_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	prog_area_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	reportable_morbidity_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	reportable_summary_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	status_time datetime NULL,
	nnd_entity_identifier varchar(200) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	nnd_summary_entity_identifier varchar(200) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	summary_investigation_form_cd varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	contact_tracing_enable_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	vaccine_enable_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	treatment_enable_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	lab_report_enable_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	morb_report_enable_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	port_req_ind_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	family_cd varchar(256) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	coinfection_grp_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	CONSTRAINT PK_Condition_code182 PRIMARY KEY (condition_cd)
);
 CREATE  UNIQUE NONCLUSTERED INDEX UQ__Condition_code__276EDEB3 ON dbo.Condition_code (  nbs_uid ASC  )
	 WITH (  PAD_INDEX = OFF ,FILLFACTOR = 90   ,SORT_IN_TEMPDB = OFF , IGNORE_DUP_KEY = OFF , STATISTICS_NORECOMPUTE = OFF , ONLINE = OFF , ALLOW_ROW_LOCKS = ON , ALLOW_PAGE_LOCKS = ON  )
	 ON [PRIMARY ] ;


-- NBS_SRTE.dbo.Condition_code foreign keys

ALTER TABLE NBS_SRTE.dbo.Condition_code ADD CONSTRAINT FK_Condition_code336 FOREIGN KEY (prog_area_cd) REFERENCES NBS_SRTE.dbo.Program_area_code(prog_area_cd);




CREATE TABLE NBS_SRTE.dbo.Code_to_condition (
	code_system_cd varchar(256) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code_desc_txt varchar(256) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code_system_version_id varchar(256) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	condition_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	disease_nm varchar(200) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	status_time datetime NULL,
	nbs_uid int NULL,
	effective_from_time datetime NULL,
	effective_to_time datetime NULL
);

CREATE TABLE NBS_SRTE.dbo.Jurisdiction_code (
	code varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	type_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	assigning_authority_cd varchar(199) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	assigning_authority_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code_desc_txt varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code_short_desc_txt varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	effective_from_time datetime NULL,
	effective_to_time datetime NULL,
	indent_level_nbr smallint NULL,
	is_modifiable_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	parent_is_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	state_domain_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	status_cd varchar(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	status_time datetime NULL,
	code_set_nm varchar(256) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code_seq_num smallint NULL,
	nbs_uid int NULL,
	source_concept_id varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code_system_cd varchar(300) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code_system_desc_txt varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	export_ind char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	CONSTRAINT PK_Jurisdiction_code PRIMARY KEY (code)
);


CREATE TABLE NBS_SRTE.dbo.Program_area_code (
	prog_area_cd varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	prog_area_desc_txt varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	nbs_uid int NULL,
	status_cd char(1) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	status_time datetime NULL,
	code_set_nm varchar(256) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	code_seq smallint NULL,
	CONSTRAINT PK_Program_area_code196 PRIMARY KEY (prog_area_cd)
);
 CREATE  UNIQUE NONCLUSTERED INDEX UQ__Program_area_cod__5441852A ON dbo.Program_area_code (  nbs_uid ASC  )
	 WITH (  PAD_INDEX = OFF ,FILLFACTOR = 90   ,SORT_IN_TEMPDB = OFF , IGNORE_DUP_KEY = OFF , STATISTICS_NORECOMPUTE = OFF , ONLINE = OFF , ALLOW_ROW_LOCKS = ON , ALLOW_PAGE_LOCKS = ON  )
	 ON [PRIMARY ] ;
