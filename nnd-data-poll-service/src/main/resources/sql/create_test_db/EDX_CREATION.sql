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

