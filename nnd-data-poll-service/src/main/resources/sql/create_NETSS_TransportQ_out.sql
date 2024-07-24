USE NBS_MSGOUTE;

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

