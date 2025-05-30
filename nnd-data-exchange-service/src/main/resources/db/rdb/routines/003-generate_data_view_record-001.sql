
IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_view_config] WHERE query_name = 'MidisInvestigationReminders')
    BEGIN
        INSERT INTO [dbo].[data_view_config]
        (query_name, source_db, query, meta_data)
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
        and phc.Add_Time > ? and phc.Jurisdiction_Cd IN (?)
        and fact.FirstNotificationdate is null
        and phc.Jurisdiction_Cd != ''''
        and part.Type_Cd = ''SubjOfPHC'';',
        'YearNumber, WeekNumber, ConditionName, PatientId, JurisdictionName, PublicHealthCaseAddTime, ProgramAreaCode, InvestigationId, AddedDate'
            )
    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_view_config] WHERE query_name = 'MidisQueue')
    BEGIN
        INSERT INTO [dbo].[data_view_config]
        (query_name, source_db, query, meta_data)
        VALUES
            (
                'MidisQueue',
                'NBS_ODSE',
                'SELECT
                o.jurisdiction_cd AS JurisdictionName,
                o.cd AS OrderedTestCode,
                o.cd_desc_txt AS OrderedTest,
                o1.cd as SourceOrderedTestCode,
                o1.cd_desc_txt as SourceOrderedTest,
                o.rpt_to_state_time AS ReportToStateTime,
                o.local_id AS ObsNumber,
                o1.observation_uid AS ObservationUid,
                vc.display_name as Result,
                vc.code as Result_Code
            FROM Observation o
            JOIN Participation p       ON o.observation_uid = p.act_uid
            JOIN Act_relationship ar   ON o.observation_uid = ar.target_act_uid
            JOIN Observation o1        ON ar.source_act_uid = o1.observation_uid
            LEFT JOIN OBS_VALUE_CODED vc    ON vc.observation_uid = o1.observation_uid
            WHERE o.record_status_cd     = ''UNPROCESSED''
              AND o.obs_domain_cd_st_1   = ''Order''
              AND o.ctrl_cd_display_form = ''LabReport''
              AND o.jurisdiction_cd IS NOT NULL
              AND o.prog_area_cd IS NOT NULL
              AND p.type_cd              = ''AUT''
              AND ar.type_cd             = ''COMP''
              AND o1.obs_domain_cd_st_1  = ''Result''
            ORDER BY SourceOrderedTest;',
                'JurisdictionName, OrderedTestCode, OrderedTest, SourceOrderedTestCode, SourceOrderedTest, ReportToStateTime, ObsNumber, ObservationUid, Result, Result_Code'
            )
    END;



IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_view_config] WHERE query_name = 'MidisQueue_CustomParam')
    BEGIN
        INSERT INTO [dbo].[data_view_config]
        (query_name, source_db, query, meta_data, custom_param_applied)
        VALUES
            (
                'MidisQueue_CustomParam',
                'NBS_ODSE',
                'SELECT
                o.jurisdiction_cd AS JurisdictionName,
                o.cd AS OrderedTestCode,
                o.cd_desc_txt AS OrderedTest,
                o1.cd as SourceOrderedTestCode,
                o1.cd_desc_txt as SourceOrderedTest,
                o.rpt_to_state_time AS ReportToStateTime,
                o.local_id AS ObsNumber,
                o1.observation_uid AS ObservationUid,
                vc.display_name as Result,
                vc.code as Result_Code
            FROM Observation o
            JOIN Participation p       ON o.observation_uid = p.act_uid
            JOIN Act_relationship ar   ON o.observation_uid = ar.target_act_uid
            JOIN Observation o1        ON ar.source_act_uid = o1.observation_uid
            LEFT JOIN OBS_VALUE_CODED vc    ON vc.observation_uid = o1.observation_uid
            WHERE_STATEMENT
            ORDER BY SourceOrderedTest;',
                'JurisdictionName, OrderedTestCode, OrderedTest, SourceOrderedTestCode, SourceOrderedTest, ReportToStateTime, ObsNumber, ObservationUid, Result, Result_Code',
             1)
    END;