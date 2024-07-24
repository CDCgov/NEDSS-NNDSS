SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CN_transportq_out](
    [cn_transportq_out_uid] [bigint] NOT NULL,
    [add_reason_cd] [varchar](20) NULL,
    [add_time] [datetime] NULL,
    [add_user_id] [bigint] NULL,
    [last_chg_reason_cd] [varchar](20) NULL,
    [last_chg_time] [datetime] NULL,
    [last_chg_user_id] [bigint] NULL,
    [message_payload] [text] NULL,
    [notification_uid] [bigint] NULL,
    [notification_local_id] [varchar](50) NULL,
    [public_health_case_local_id] [varchar](50) NULL,
    [report_status_cd] [varchar](20) NULL,
    [record_status_cd] [varchar](20) NULL,
    [record_status_time] [datetime] NULL,
    [version_ctrl_nbr] [smallint] NULL
    ) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
    GO
ALTER TABLE [dbo].[CN_transportq_out] ADD  CONSTRAINT [PK_CNtransQOut_cnTransOutUid] PRIMARY KEY NONCLUSTERED
    (
    [cn_transportq_out_uid] ASC
    )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
    GO
