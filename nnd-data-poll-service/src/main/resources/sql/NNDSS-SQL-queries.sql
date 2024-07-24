-- Initial load Full load
	-- CN_transportq_out
	-- NETSS_TransportQ_out
	-- TransportQ_out


-- This is the query logic for gathering the NBS_MSGOUTE.dbo.TransportQ_out tables

SELECT recordId
      ,messageId
      ,payloadFile
      ,payloadContent
      ,destinationFilename
      ,routeInfo
      ,service
      ,action
      ,arguments
      ,messageRecipient
      ,messageCreationTime
      ,encryption
      ,signature
      ,publicKeyLdapAddress
      ,publicKeyLdapBaseDN
      ,publicKeyLdapDN
      ,certificateURL
      ,processingStatus
      ,transportStatus
      ,transportErrorCode
      ,applicationStatus
      ,applicationErrorCode
      ,applicationResponse
      ,messageSentTime
      ,messageReceivedTime
      ,responseMessageId
      ,responseArguments
      ,responseLocalFile
      ,responseFilename
      ,responseContent
      ,responseMessageOrigin
      ,responseMessageSignature
      ,priority
  FROM NBS_MSGOUTE.dbo.TransportQ_out
  where messageCreationTime > (select max(messageCreationTime) from @database_on-prem);


 -- This is the query logic for gathering data from NBS_ODSE..CN_transportq_out tables
select 
	cn_transportq_out_uid,
	notification_uid,
	add_reason_cd,
	add_time,
	add_user_id,
	last_chg_reason_cd,
	last_chg_time,
	last_chg_user_id,
	report_status_cd,
	record_status_cd,
	record_status_time,
	message_payload,
	public_health_case_local_id,
	notification_local_id,
	version_ctrl_nbr
from NBS_ODSE..CN_transportq_out 
	where record_status_cd = 'UNPROCESSED' and add_time > (select max(add_time) from @database_on-prem);

