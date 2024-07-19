SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TransportQ_out](
    [recordId] [bigint] NOT NULL,
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
    [priority] [int] NULL
    ) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
    GO
ALTER TABLE [dbo].[TransportQ_out] ADD PRIMARY KEY CLUSTERED
    (
    [recordId] ASC
    )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
    GO
    SET ANSI_PADDING ON
    GO
CREATE NONCLUSTERED INDEX [IDX_TransportQ_MSG_PID] ON [dbo].[TransportQ_out]
(
	[messageId] ASC,
	[processingStatus] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
