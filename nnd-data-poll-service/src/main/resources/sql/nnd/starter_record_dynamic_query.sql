DECLARE @startDTM VARCHAR(20)

SET @startDTM = '2024-12-10'

INSERT INTO TransportQ_out 
	(messageId, 
	payloadFile, 
	routeInfo, 
	service, 
	action, 
	messageCreationTime, 
	encryption, signature, processingStatus, priority)
VALUES 
	('STRT_REC', 
	'Starter_file',
	'CDC', 
	'Starter', 
	'DONOTSEND', 
	CONVERT(VARCHAR(20), @startDTM) + 'T00:00:00', 
	'NA', 'NA', 'queued', 1);

INSERT INTO CN_transportq_out
	(add_time, 
	add_reason_cd, 
	notification_uid,
	notification_local_id,
	record_status_time)
VALUES
	(CONVERT(VARCHAR, @startDTM, 120), 
	'Starter', 
	0,
	'STRT_REC_01',
	CONVERT(VARCHAR, @startDTM, 120));

INSERT INTO NETSS_TransportQ_out
	(record_type_cd, 
	mmwr_year, 
	mmwr_week, 
	netss_case_id, phc_local_id, notification_local_id, 
	add_time, 
	payload)
VALUES
	('Starter_Record', 
	RIGHT(YEAR(@startDTM), 2), 
	DATEPART(week, @startDTM), 
	0, 0, 0, 
	CONVERT(VARCHAR, @startDTM, 120), 
	'Starter_Record');