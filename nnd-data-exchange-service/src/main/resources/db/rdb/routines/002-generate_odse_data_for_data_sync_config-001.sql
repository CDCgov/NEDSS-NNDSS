delete from data_sync_config where table_name in ('ACT',
    'Act_relationship',
    'EDX_ACTIVITY_DETAIL_LOG',
    'EDX_ACTIVITY_LOG',
    'ENTITY',
    'Entity_id',
    'OBS_VALUE_CODED',
    'OBS_VALUE_NUMERIC',
    'OBS_VALUE_TXT',
    'OBSERVATION',
    'ORGANIZATION',
    'ORGANIZATION_NAME',
    'PARTICIPATION',
    'PERSON',
    'PUBLIC_HEALTH_CASE',
    'PublicHealthCaseFact',
    'ROLE',
     'MidisInvestigation',
     'MidisInvestigation_ObsValueCoded',
     'MidisInvestigationReminders');

IF
NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'MidisInvestigation')
BEGIN
INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, datasync_applied, meta_data, query_count, query_with_pagination)
VALUES
    (
    'MidisInvestigation',
    'NBS_ODSE',
    'select o.jurisdiction_cd as jurisdictionCd, o.cd as code, o.cd_desc_txt as codeDescTxt, o.rpt_to_state_time as rptToStateTime, o.local_id as localId, o1.observation_uid as observationUid
      from Observation o
      join Participation p     on o.observation_uid = p.act_uid
      join Act_relationship ar on o.observation_uid = ar.target_act_uid
      join Observation o1      on ar.source_act_uid = o1.observation_uid
    where o.record_status_cd     = ''UNPROCESSED''
       and o.obs_domain_cd_st_1   = ''Order''
       and o.ctrl_cd_display_form = ''LabReport''
       and o.jurisdiction_cd is not null
       and o.prog_area_cd is not null
       and p.type_cd = ''AUT''
       and ar.type_cd = ''COMP''
       and o1.obs_domain_cd_st_1 = ''Result''
    order by o.local_id',
    '0',
    'jurisdictionCd, code, codeDescTxt, rptToStateTime, localId, observationUid','','');
END;


IF
NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'MidisInvestigation_ObsValueCoded')
BEGIN
INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, datasync_applied, meta_data, query_count, query_with_pagination)
VALUES
    (
    'MidisInvestigation_ObsValueCoded',
    'NBS_ODSE',
    'select * from OBS_VALUE_CODED
    where observation_uid IN :param;',
    '0',
    'observation_uid, code, code_system_cd, code_system_desc_txt, code_version, display_name, original_txt, alt_cd, alt_cd_desc_txt, alt_cd_system_cd, alt_cd_system_desc_txt, code_derived_ind','','');
END;


IF
NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'MidisInvestigationReminders')
BEGIN
INSERT INTO [RDB].[dbo].[data_sync_config]
(table_name, source_db, query, datasync_applied, meta_data, query_count, query_with_pagination)
VALUES
    (
    'MidisInvestigationReminders', 'NBS_ODSE', 'SELECT
        phc.Mmwr_Year as YearNumber,
        phc.Mmwr_Week as WeekNumber,
        phc.Cd_Desc_Txt as ConditionName,
        per.Local_Id as PatientId,
        phc.Jurisdiction_Cd as JurisdictionName,
        phc.Add_Time as PublicHealthCaseAddTime,
        phc.Prog_Area_Cd as ProgramAreaCode,
        phc.Local_Id as InvestigationId,
        phc.Add_Time as AddedDate

        FROM
        dbo.Public_Health_Case phc
        JOIN dbo.Participation part on phc.Public_Health_Case_Uid = part.Act_Uid
        JOIN dbo.Person per on part.Subject_Entity_Uid = per.Person_Uid
        JOIN dbo.PublicHealthCaseFact fact on per.Person_Uid = fact.Person_Uid
        WHERE
        phc.Case_Class_Cd IN (''P'',''C'',''U'')
        and phc.Add_Time > '':param''
        and fact.FirstNotificationdate is null
        and phc.Jurisdiction_Cd != ''''
        and part.Type_Cd = ''SubjOfPHC'';', '0', 'YearNumber, WeekNumber, ConditionName, PatientId, JurisdictionName, PublicHealthCaseAddTime, ProgramAreaCode, InvestigationId, AddedDate','',''
    )
END;