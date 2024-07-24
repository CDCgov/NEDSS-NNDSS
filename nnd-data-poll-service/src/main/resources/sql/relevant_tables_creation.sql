IF NOT EXISTS(SELECT *
              FROM sys.databases
              WHERE name = 'MSGOUTE')
BEGIN
        CREATE DATABASE MSGOUTE
END
GO
USE MSGOUTE;
GO

CREATE LOGIN nbs_ods WITH PASSWORD = 'ods'
GO

use MSGOUTE
GO
CREATE USER nbs_ods FOR LOGIN nbs_ods
GO
ALTER USER nbs_ods WITH DEFAULT_SCHEMA=dbo
GO
ALTER ROLE db_owner ADD MEMBER nbs_ods
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TransportQ_out](
    [recordId] [bigint] IDENTITY(1,1) NOT NULL,
    [messageId] [varchar](255) NULL,
    [payloadFile] [varchar](255) NULL,
    [payloadContent] [image] NULL,
    [destinationFilename] [varchar](255) NULL,
    [routeInfo] [varchar](255) NOT NULL,
    [service] [varchar](255) NOT NULL,
    [action] [varchar](255) NOT NULL,
    [arguments] [varchar](255) NULL,
    [messageRecipient] [varchar](255) NULL,
    [messageCreationTime] [varchar](255) NULL,
    [encryption] [char](10) NOT NULL,
    [signature] [char](10) NOT NULL,
    [publicKeyLdapAddress] [varchar](255) NULL,
    [publicKeyLdapBaseDN] [varchar](255) NULL,
    [publicKeyLdapDN] [varchar](255) NULL,
    [certificateURL] [varchar](255) NULL,
    [processingStatus] [varchar](255) NULL,
    [transportStatus] [varchar](255) NULL,
    [transportErrorCode] [varchar](255) NULL,
    [applicationStatus] [varchar](255) NULL,
    [applicationErrorCode] [varchar](255) NULL,
    [applicationResponse] [varchar](255) NULL,
    [messageSentTime] [varchar](255) NULL,
    [messageReceivedTime] [varchar](255) NULL,
    [responseMessageId] [varchar](255) NULL,
    [responseArguments] [varchar](255) NULL,
    [responseLocalFile] [varchar](255) NULL,
    [responseFilename] [varchar](255) NULL,
    [responseContent] [image] NULL,
    [responseMessageOrigin] [varchar](255) NULL,
    [responseMessageSignature] [varchar](255) NULL,
    [priority] [int] NULL,
    PRIMARY KEY CLUSTERED
(
[recordId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
    ) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
    GO
    SET ANSI_PADDING ON
    GO
CREATE NONCLUSTERED INDEX [IDX_TransportQ_MSG_PID] ON [dbo].[TransportQ_out]
(
	[messageId] ASC,
	[processingStatus] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

----------------------------------------

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NETSS_TransportQ_out](
    [NETSS_TransportQ_out_uid] [bigint] IDENTITY(1000,1) NOT NULL,
    [record_type_cd] [varchar](200) NOT NULL,
    [mmwr_year] [smallint] NOT NULL,
    [mmwr_week] [smallint] NOT NULL,
    [netss_case_id] [varchar](50) NOT NULL,
    [phc_local_id] [varchar](50) NOT NULL,
    [notification_local_id] [varchar](50) NOT NULL,
    [add_time] [datetime] NULL,
    [payload] [varchar](250) NOT NULL,
    [record_status_cd] [varchar](20) NULL
    ) ON [PRIMARY]
    GO
ALTER TABLE [dbo].[NETSS_TransportQ_out] ADD  DEFAULT (getdate()) FOR [add_time]
    GO

----------------------------------------

    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[CN_transportq_out](
    [cn_transportq_out_uid] [bigint] IDENTITY(1000,1) NOT NULL,
    [add_reason_cd] [varchar](20) NULL,
    [add_time] [datetime] NULL,
    [add_user_id] [bigint] NULL,
    [last_chg_reason_cd] [varchar](20) NULL,
    [last_chg_time] [datetime] NULL,
    [last_chg_user_id] [bigint] NULL,
    [message_payload] [text] NULL,
    [notification_uid] [bigint] NOT NULL,
    [notification_local_id] [varchar](50) NULL,
    [public_health_case_local_id] [varchar](50) NULL,
    [report_status_cd] [varchar](20) NULL,
    [record_status_cd] [varchar](20) NULL,
    [record_status_time] [datetime] NULL,
    [version_ctrl_nbr] [smallint] NULL,
    CONSTRAINT [PK_CNtransQOut_cnTransOutUid] PRIMARY KEY NONCLUSTERED
(
[cn_transportq_out_uid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
    ) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
    GO
    SET ANSI_PADDING ON
    GO
CREATE NONCLUSTERED INDEX [PERF_08302021_01] ON [dbo].[CN_transportq_out]
(
	[record_status_cd] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO