delete from data_sync_config where table_name in ('Entity', 'ACT', 'PublicHealthCaseFact', 'Entity_id', 'PARTICIPATION', 'ORGANIZATION',
                                                  'ORGANIZATION_NAME', 'PUBLIC_HEALTH_CASE', 'Act_relationship', 'PARTICIPATION',
                                                 'OBSERVATION', 'OBS_VALUE_CODED', 'OBS_VALUE_NUMERIC', 'OBS_VALUE_TXT');


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ENTITY')
    BEGIN

        INSERT INTO [dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                              query_with_pagination)
        VALUES
            ('ENTITY',
             'NBS_ODSE',
             'SELECT * FROM ENTITY  WHERE entity_uid > :timestamp;',

             NULL,
             'SELECT COUNT(*) FROM ENTITY  WHERE entity_uid > :timestamp;',
             'WITH PaginatedResults AS (SELECT ENTITY.*, ROW_NUMBER() OVER (ORDER BY ENTITY.entity_uid) AS RowNum FROM ENTITY  WHERE entity_uid > :timestamp ) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'
            );

    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ACT')
    BEGIN

        INSERT INTO [dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                              query_with_pagination)
        VALUES
            ('ACT',
             'NBS_ODSE',
             'SELECT * FROM ACT  WHERE act_uid > :timestamp;',

             NULL,
             'SELECT COUNT(*) FROM ACT  WHERE act_uid > :timestamp;',
             'WITH PaginatedResults AS (SELECT ACT.*, ROW_NUMBER() OVER (ORDER BY ACT.act_uid) AS RowNum FROM ACT  WHERE act_uid > :timestamp ) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'
            );

    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PublicHealthCaseFact')
    BEGIN
        INSERT INTO [dbo].[data_sync_config] (
            table_name,
            source_db,
            query,
            query_with_null_timestamp,
            query_count,
            query_with_pagination
        )
        VALUES (
                   'PublicHealthCaseFact',
                   'NBS_ODSE',

                   'SELECT * FROM PublicHealthCaseFact WHERE (
                       (
                           SELECT MAX(val) FROM (VALUES
                               (notificationdate),
                               (PHC_add_time),
                               (report_date),
                               (LASTUPDATE)
                           ) AS AllTimes(val)
                       ) :operator :timestamp
                   );',

                   NULL,

                   'SELECT COUNT(*) FROM PublicHealthCaseFact WHERE (
                       (
                           SELECT MAX(val) FROM (VALUES
                               (notificationdate),
                               (PHC_add_time),
                               (report_date),
                               (LASTUPDATE)
                           ) AS AllTimes(val)
                       ) :operator :timestamp
                   );',

                   'WITH PaginatedResults AS (
                       SELECT PublicHealthCaseFact.*,
                              ROW_NUMBER() OVER (ORDER BY PublicHealthCaseFact.public_health_case_uid) AS RowNum
                       FROM PublicHealthCaseFact
                       WHERE (
                           (
                               SELECT MAX(val) FROM (VALUES
                                   (notificationdate),
                                   (PHC_add_time),
                                   (report_date),
                                   (LASTUPDATE)
                               ) AS AllTimes(val)
                           ) :operator :timestamp
                       )
                   )
                   SELECT * FROM PaginatedResults
                   WHERE RowNum BETWEEN :startRow AND :endRow;'
               );
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'Entity_id')
    BEGIN

        INSERT INTO [dbo].[data_sync_config] (
            table_name,
            source_db,
            query,
            query_with_null_timestamp,
            query_count,
            query_with_pagination
        )
        VALUES
            (
                'Entity_id',
                'NBS_ODSE',
                'SELECT * FROM Entity_id WHERE (
                    (
                        SELECT MAX(val) FROM (VALUES
                            (add_time),
                            (record_status_time),
                            (status_time)
                        ) AS AllTimes(val)
                    ) :operator :timestamp
                );',

                NULL,

                'SELECT COUNT(*) FROM Entity_id WHERE (
                    (
                        SELECT MAX(val) FROM (VALUES
                            (add_time),
                            (record_status_time),
                            (status_time)
                        ) AS AllTimes(val)
                    ) :operator :timestamp
                );',

                'WITH PaginatedResults AS (
                    SELECT Entity_id.*,
                           ROW_NUMBER() OVER (
                               ORDER BY entity_uid
                           ) AS RowNum
                    FROM Entity_id
                    WHERE (
                        (
                            SELECT MAX(val) FROM (VALUES
                                (add_time),
                                (record_status_time),
                                (status_time)
                            ) AS AllTimes(val)
                        ) :operator :timestamp
                    )
                )
                SELECT * FROM PaginatedResults
                WHERE RowNum BETWEEN :startRow AND :endRow;'
            );

    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ROLE')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('ROLE', 'NBS_ODSE',
             'SELECT ROLE.*
                FROM ROLE WHERE (
                    (
                        SELECT MAX(val) FROM (VALUES
                            (add_time),
                            (last_chg_time),
                            (record_status_time),
							(status_time)
                        ) AS AllTimes(val)
                    ) :operator :timestamp
                );',
             NULL,
             'SELECT COUNT(*) FROM ROLE WHERE (
                    (
                        SELECT MAX(val) FROM (VALUES
                            (add_time),
                            (last_chg_time),
                            (record_status_time),
							(status_time)
                        ) AS AllTimes(val)
                    ) :operator :timestamp
                );',

             'WITH PaginatedResults AS (
             SELECT ROLE.*,
                    ROW_NUMBER() OVER (
                        ORDER BY
                            COALESCE(add_time, last_chg_time, record_status_time, status_time) ASC,
                            subject_entity_uid, role_seq, cd
                    ) AS RowNum
             FROM ROLE
             WHERE (
                 (
                     SELECT MAX(val) FROM (VALUES
                         (add_time),
                         (last_chg_time),
                         (record_status_time),
                         (status_time)
                     ) AS AllTimes(val)
                 ) :operator :timestamp
             )
         )
         SELECT * FROM PaginatedResults
         WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PARTICIPATION')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('PARTICIPATION', 'NBS_ODSE',
             'SELECT DISTINCT PARTICIPATION.*
         FROM PARTICIPATION
         INNER JOIN ACT
          ON ACT.act_uid = PARTICIPATION.act_uid
         INNER JOIN OBSERVATION
          ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
         INNER JOIN ENTITY
          ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
         LEFT JOIN ROLE
          ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
          AND ROLE.role_seq = PARTICIPATION.role_seq
          AND ROLE.cd = PARTICIPATION.cd
         WHERE (OBSERVATION.add_time :operator :timestamp
             OR OBSERVATION.last_chg_time :operator :timestamp);',
             NULL,
             'SELECT COUNT(*) FROM (
         SELECT DISTINCT PARTICIPATION.subject_entity_uid, PARTICIPATION.act_uid, PARTICIPATION.type_cd
         FROM PARTICIPATION
         INNER JOIN ACT
             ON ACT.act_uid = PARTICIPATION.act_uid
         INNER JOIN OBSERVATION
             ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
         INNER JOIN ENTITY
             ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
         LEFT JOIN ROLE
             ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
             AND ROLE.role_seq = PARTICIPATION.role_seq
             AND ROLE.cd = PARTICIPATION.cd
         WHERE (OBSERVATION.add_time :operator :timestamp
                OR OBSERVATION.last_chg_time :operator :timestamp)
         ) AS PARTICIPATION_COUNT;',
             'WITH PaginatedResults AS (
                 SELECT DISTINCT PARTICIPATION.*,
                        COALESCE(OBSERVATION.add_time, OBSERVATION.last_chg_time) AS latest_timestamp
                 FROM PARTICIPATION
                 INNER JOIN ACT
                     ON ACT.act_uid = PARTICIPATION.act_uid
                 INNER JOIN OBSERVATION
                     ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                 INNER JOIN ENTITY
                     ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
                 LEFT JOIN ROLE
                     ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
                     AND ROLE.role_seq = PARTICIPATION.role_seq
                     AND ROLE.cd = PARTICIPATION.cd
                 WHERE (OBSERVATION.add_time :operator :timestamp
                        OR OBSERVATION.last_chg_time :operator :timestamp)
             ),
             NumberedResults AS (
                 SELECT *,
                        ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, subject_entity_uid, act_uid, type_cd) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ORGANIZATION')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config] (
            table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination
        )
        VALUES (
                   'ORGANIZATION',
                   'NBS_ODSE',
                   'WITH EntityResults AS (
                       SELECT DISTINCT ENTITY.entity_uid
                       FROM ENTITY
                       INNER JOIN PARTICIPATION
                           ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
                       INNER JOIN OBSERVATION
                           ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                   )
                   SELECT ORGANIZATION.*
                   FROM ORGANIZATION
                   WHERE ORGANIZATION.organization_uid IN (SELECT entity_uid FROM EntityResults);',
                   NULL,
                   'SELECT COUNT(*)
                    FROM ORGANIZATION
                    WHERE organization_uid IN (
                        SELECT DISTINCT ENTITY.entity_uid
                        FROM ENTITY
                        INNER JOIN PARTICIPATION
                            ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
                        INNER JOIN OBSERVATION
                            ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                    );',
                   'WITH PaginatedResults AS (
                       SELECT DISTINCT ENTITY.entity_uid,
                           MAX(CASE
                               WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
                               ELSE OBSERVATION.last_chg_time
                           END) AS latest_timestamp
                       FROM ENTITY
                       INNER JOIN PARTICIPATION
                           ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
                       INNER JOIN OBSERVATION
                           ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                       GROUP BY ENTITY.entity_uid
                   ),
                   NumberedResults AS (
                       SELECT entity_uid, ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, entity_uid) AS RowNum
                       FROM PaginatedResults
                   )
                   SELECT ORGANIZATION.*
                   FROM ORGANIZATION
                   WHERE ORGANIZATION.organization_uid IN (
                       SELECT entity_uid
                       FROM NumberedResults
                       WHERE RowNum BETWEEN :startRow AND :endRow
                   );'
               );
    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ORGANIZATION_NAME')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config] (
            table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination
        )
        VALUES (
                   'ORGANIZATION_NAME',
                   'NBS_ODSE',
                   'WITH EntityResults AS (
                       SELECT DISTINCT ENTITY.entity_uid
                       FROM ENTITY
                       INNER JOIN PARTICIPATION
                           ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
                       INNER JOIN OBSERVATION
                           ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                   )
                   SELECT ORGANIZATION_NAME.*
                   FROM ORGANIZATION_NAME
                   WHERE ORGANIZATION_NAME.organization_uid IN (SELECT entity_uid FROM EntityResults);',
                   NULL,
                   'SELECT COUNT(*)
                    FROM ORGANIZATION_NAME
                    WHERE organization_uid IN (
                        SELECT DISTINCT ENTITY.entity_uid
                        FROM ENTITY
                        INNER JOIN PARTICIPATION
                            ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
                        INNER JOIN OBSERVATION
                            ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                    );',
                   'WITH PaginatedResults AS (
                       SELECT DISTINCT ENTITY.entity_uid,
                           MAX(CASE
                               WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
                               ELSE OBSERVATION.last_chg_time
                           END) AS latest_timestamp
                       FROM ENTITY
                       INNER JOIN PARTICIPATION
                           ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
                       INNER JOIN OBSERVATION
                           ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                       GROUP BY ENTITY.entity_uid
                   ),
                   NumberedResults AS (
                       SELECT entity_uid, ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, entity_uid) AS RowNum
                       FROM PaginatedResults
                   )
                   SELECT ORGANIZATION_NAME.*
                   FROM ORGANIZATION_NAME
                   WHERE ORGANIZATION_NAME.organization_uid IN (
                       SELECT entity_uid
                       FROM NumberedResults
                       WHERE RowNum BETWEEN :startRow AND :endRow
                   );'
               );
    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PUBLIC_HEALTH_CASE')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config] (
            table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination
        )
        VALUES (
                   'PUBLIC_HEALTH_CASE',
                   'NBS_ODSE',
                   'WITH ActResults AS (
                       SELECT DISTINCT ACT.act_uid
                       FROM ACT
                       INNER JOIN Act_relationship
                           ON ACT.act_uid = Act_relationship.target_act_uid
                       INNER JOIN OBSERVATION
                           ON Act_relationship.source_act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                   )
                   SELECT PUBLIC_HEALTH_CASE.*
                   FROM PUBLIC_HEALTH_CASE
                   WHERE PUBLIC_HEALTH_CASE.public_health_case_uid IN (SELECT act_uid FROM ActResults);',
                   NULL,
                   'SELECT COUNT(*)
                    FROM PUBLIC_HEALTH_CASE
                    WHERE public_health_case_uid IN (
                        SELECT DISTINCT ACT.act_uid
                        FROM ACT
                        INNER JOIN Act_relationship
                            ON ACT.act_uid = Act_relationship.target_act_uid
                        INNER JOIN OBSERVATION
                            ON Act_relationship.source_act_uid = OBSERVATION.observation_uid
                        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                    );',
                   'WITH ActResults AS (
                       SELECT DISTINCT ACT.act_uid,
                           MAX(CASE
                               WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
                               ELSE OBSERVATION.last_chg_time
                           END) AS latest_timestamp
                       FROM ACT
                       INNER JOIN Act_relationship
                           ON ACT.act_uid = Act_relationship.target_act_uid
                       INNER JOIN OBSERVATION
                           ON Act_relationship.source_act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                       GROUP BY ACT.act_uid
                   ),
                   NumberedResults AS (
                       SELECT act_uid, ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, act_uid) AS RowNum
                       FROM ActResults
                   )
                   SELECT PUBLIC_HEALTH_CASE.*
                   FROM PUBLIC_HEALTH_CASE
                   WHERE PUBLIC_HEALTH_CASE.public_health_case_uid IN (
                       SELECT act_uid FROM NumberedResults
                       WHERE RowNum BETWEEN :startRow AND :endRow
                   );'
               );
    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'Act_relationship')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('Act_relationship', 'NBS_ODSE', 'SELECT DISTINCT Act_relationship.*
     FROM Act_relationship
     INNER JOIN OBSERVATION
         ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
     INNER JOIN ACT AS ACT_SOURCE
         ON ACT_SOURCE.act_uid = Act_relationship.source_act_uid
     INNER JOIN ACT AS ACT_TARGET
         ON ACT_TARGET.act_uid = Act_relationship.target_act_uid
     WHERE (OBSERVATION.add_time :operator :timestamp
            OR OBSERVATION.last_chg_time :operator :timestamp);', NULL, 'SELECT COUNT(*) FROM (
        SELECT DISTINCT Act_relationship.source_act_uid, Act_relationship.target_act_uid, Act_relationship.type_cd
        FROM Act_relationship
        INNER JOIN OBSERVATION
            ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
        INNER JOIN ACT AS ACT_SOURCE
            ON ACT_SOURCE.act_uid = Act_relationship.source_act_uid
        INNER JOIN ACT AS ACT_TARGET
            ON ACT_TARGET.act_uid = Act_relationship.target_act_uid
        WHERE (OBSERVATION.add_time :operator :timestamp
               OR OBSERVATION.last_chg_time :operator :timestamp)
    ) AS ACT_RELATIONSHIP_COUNT;', 'WITH PaginatedResults AS (
        SELECT DISTINCT Act_relationship.*,
               COALESCE(OBSERVATION.add_time, OBSERVATION.last_chg_time) AS latest_timestamp
        FROM Act_relationship
        INNER JOIN OBSERVATION
            ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
        INNER JOIN ACT AS ACT_SOURCE
            ON ACT_SOURCE.act_uid = Act_relationship.source_act_uid
        INNER JOIN ACT AS ACT_TARGET
            ON ACT_TARGET.act_uid = Act_relationship.target_act_uid
        WHERE (OBSERVATION.add_time :operator :timestamp
               OR OBSERVATION.last_chg_time :operator :timestamp)
    ),
    NumberedResults AS (
        SELECT *,
               ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, source_act_uid, target_act_uid, type_cd) AS RowNum
        FROM PaginatedResults
    )
    SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PARTICIPATION')
    BEGIN

        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('PARTICIPATION', 'NBS_ODSE',
             'SELECT DISTINCT PARTICIPATION.*
         FROM PARTICIPATION
         INNER JOIN ACT
          ON ACT.act_uid = PARTICIPATION.act_uid
         INNER JOIN OBSERVATION
          ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
         INNER JOIN ENTITY
          ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
         LEFT JOIN ROLE
          ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
          AND ROLE.role_seq = PARTICIPATION.role_seq
          AND ROLE.cd = PARTICIPATION.cd
         WHERE (OBSERVATION.add_time :operator :timestamp
             OR OBSERVATION.last_chg_time :operator :timestamp);',
             NULL,
             'SELECT COUNT(*) FROM (
         SELECT DISTINCT PARTICIPATION.subject_entity_uid, PARTICIPATION.act_uid, PARTICIPATION.type_cd
         FROM PARTICIPATION
         INNER JOIN ACT
             ON ACT.act_uid = PARTICIPATION.act_uid
         INNER JOIN OBSERVATION
             ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
         INNER JOIN ENTITY
             ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
         LEFT JOIN ROLE
             ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
             AND ROLE.role_seq = PARTICIPATION.role_seq
             AND ROLE.cd = PARTICIPATION.cd
         WHERE (OBSERVATION.add_time :operator :timestamp
                OR OBSERVATION.last_chg_time :operator :timestamp)
         ) AS PARTICIPATION_COUNT;',
             'WITH PaginatedResults AS (
                 SELECT DISTINCT PARTICIPATION.*,
                        COALESCE(OBSERVATION.add_time, OBSERVATION.last_chg_time) AS latest_timestamp
                 FROM PARTICIPATION
                 INNER JOIN ACT
                     ON ACT.act_uid = PARTICIPATION.act_uid
                 INNER JOIN OBSERVATION
                     ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                 INNER JOIN ENTITY
                     ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
                 LEFT JOIN ROLE
                     ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
                     AND ROLE.role_seq = PARTICIPATION.role_seq
                     AND ROLE.cd = PARTICIPATION.cd
                 WHERE (OBSERVATION.add_time :operator :timestamp
                        OR OBSERVATION.last_chg_time :operator :timestamp)
             ),
             NumberedResults AS (
                 SELECT *,
                        ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, subject_entity_uid, act_uid, type_cd) AS RowNum
                 FROM PaginatedResults
             )
             SELECT * FROM NumberedResults WHERE RowNum BETWEEN :startRow AND :endRow;');

    END;

IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'OBSERVATION')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                                    query_with_pagination)
        VALUES
            ('OBSERVATION',
             'NBS_ODSE',
             'WITH ObservationResults AS (
SELECT DISTINCT OBS_MAIN.observation_uid
FROM OBSERVATION as OBS_MAIN JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND obs_domain_cd_st_1 = ''Order'' AND record_status_cd = ''UNPROCESSED'' AND ctrl_cd_display_form = ''LabReport'' AND jurisdiction_cd is NOT NULL AND prog_area_cd is NOT NULL
)
SELECT OBSERVATION.*
FROM OBSERVATION
INNER JOIN ObservationResults
ON OBSERVATION.observation_uid = ObservationResults.observation_uid;',
             NULL,
             'SELECT COUNT(*) FROM (
SELECT DISTINCT OBS_MAIN.observation_uid
FROM OBSERVATION as OBS_MAIN JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND obs_domain_cd_st_1 = ''Order'' AND record_status_cd = ''UNPROCESSED'' AND ctrl_cd_display_form = ''LabReport'' AND jurisdiction_cd is NOT NULL AND prog_area_cd is NOT NULL
) AS OBSERVATION_COUNT;
',
             'WITH ObservationResults AS (
SELECT DISTINCT OBS_MAIN.observation_uid,
   MAX(CASE
       WHEN OBS_MAIN.add_time >= OBS_MAIN.last_chg_time THEN OBS_MAIN.add_time
       ELSE OBS_MAIN.last_chg_time
   END) AS latest_timestamp
FROM OBSERVATION as OBS_MAIN  JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND obs_domain_cd_st_1 = ''Order'' AND record_status_cd = ''UNPROCESSED'' AND ctrl_cd_display_form = ''LabReport'' AND jurisdiction_cd is NOT NULL AND prog_area_cd is NOT NULL
GROUP BY OBS_MAIN.observation_uid
),
NumberedResults AS (
SELECT observation_uid, ROW_NUMBER() OVER (ORDER BY observation_uid, latest_timestamp ASC) AS RowNum
FROM ObservationResults
)
SELECT OBSERVATION.*
FROM OBSERVATION
INNER JOIN NumberedResults
ON OBSERVATION.observation_uid = NumberedResults.observation_uid
WHERE NumberedResults.RowNum BETWEEN :startRow AND :endRow;');
    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'OBS_VALUE_CODED')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config] (
            table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination
        )
        VALUES (
                   'OBS_VALUE_CODED',
                   'NBS_ODSE',
                   'WITH ObservationResults AS (
                       SELECT DISTINCT OBS_MAIN.observation_uid
                       FROM OBSERVATION AS OBS_MAIN  JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND OBS_MAIN.obs_domain_cd_st_1 = ''Order'' AND OBS_MAIN.record_status_cd = ''UNPROCESSED'' AND OBS_MAIN.ctrl_cd_display_form = ''LabReport'' AND OBS_MAIN.jurisdiction_cd is NOT NULL AND OBS_MAIN.prog_area_cd is NOT NULL
                   )
                   SELECT OBS_VALUE_CODED.*
                   FROM OBS_VALUE_CODED
                   WHERE OBS_VALUE_CODED.observation_uid IN (SELECT observation_uid FROM ObservationResults);',
                   NULL,
                   'SELECT COUNT(*)
                    FROM OBS_VALUE_CODED
                    WHERE observation_uid IN (
                        SELECT DISTINCT OBS_MAIN.observation_uid
                        FROM OBSERVATION AS OBS_MAIN  JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
                        WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND OBS_MAIN.obs_domain_cd_st_1 = ''Order'' AND OBS_MAIN.record_status_cd = ''UNPROCESSED'' AND OBS_MAIN.ctrl_cd_display_form = ''LabReport'' AND OBS_MAIN.jurisdiction_cd is NOT NULL AND OBS_MAIN.prog_area_cd is NOT NULL
                    );',
                   'WITH ObservationResults AS (
                       SELECT DISTINCT OBS_MAIN.observation_uid,
                           MAX(CASE
                               WHEN OBS_MAIN.add_time >= OBS_MAIN.last_chg_time THEN OBS_MAIN.add_time
                               ELSE OBS_MAIN.last_chg_time
                           END) AS latest_timestamp
                       FROM OBSERVATION AS OBS_MAIN  JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND OBS_MAIN.obs_domain_cd_st_1 = ''Order'' AND OBS_MAIN.record_status_cd = ''UNPROCESSED'' AND OBS_MAIN.ctrl_cd_display_form = ''LabReport'' AND OBS_MAIN.jurisdiction_cd is NOT NULL AND OBS_MAIN.prog_area_cd is NOT NULL
                       GROUP BY OBS_MAIN.observation_uid
                   ),
                   NumberedResults AS (
                       SELECT observation_uid, ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, observation_uid) AS RowNum
                       FROM ObservationResults
                   )
                   SELECT OBS_VALUE_CODED.*
                   FROM OBS_VALUE_CODED
                   INNER JOIN NumberedResults
                       ON OBS_VALUE_CODED.observation_uid = NumberedResults.observation_uid
                   WHERE NumberedResults.RowNum BETWEEN :startRow AND :endRow;'
               );
    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'OBS_VALUE_NUMERIC')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config] (
            table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination
        )
        VALUES (
                   'OBS_VALUE_NUMERIC',
                   'NBS_ODSE',
                   'WITH ObservationResults AS (
                       SELECT DISTINCT OBS_MAIN.observation_uid
                       FROM OBSERVATION AS OBS_MAIN  JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND OBS_MAIN.obs_domain_cd_st_1 = ''Order'' AND OBS_MAIN.record_status_cd = ''UNPROCESSED'' AND OBS_MAIN.ctrl_cd_display_form = ''LabReport'' AND OBS_MAIN.jurisdiction_cd is NOT NULL AND OBS_MAIN.prog_area_cd is NOT NULL
                   )
                   SELECT OBS_VALUE_NUMERIC.*
                   FROM OBS_VALUE_NUMERIC
                   WHERE OBS_VALUE_NUMERIC.observation_uid IN (SELECT observation_uid FROM ObservationResults);',
                   NULL,
                   'SELECT COUNT(*)
                    FROM OBS_VALUE_NUMERIC
                    WHERE observation_uid IN (
                        SELECT DISTINCT OBS_MAIN.observation_uid
                        FROM OBSERVATION AS OBS_MAIN  JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
                        WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND OBS_MAIN.obs_domain_cd_st_1 = ''Order'' AND OBS_MAIN.record_status_cd = ''UNPROCESSED'' AND OBS_MAIN.ctrl_cd_display_form = ''LabReport'' AND OBS_MAIN.jurisdiction_cd is NOT NULL AND OBS_MAIN.prog_area_cd is NOT NULL
                    );',
                   'WITH ObservationResults AS (
                       SELECT DISTINCT OBS_MAIN.observation_uid,
                           MAX(CASE
                               WHEN OBS_MAIN.add_time >= OBS_MAIN.last_chg_time THEN OBS_MAIN.add_time
                               ELSE OBS_MAIN.last_chg_time
                           END) AS latest_timestamp
                       FROM OBSERVATION AS OBS_MAIN  JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND OBS_MAIN.obs_domain_cd_st_1 = ''Order'' AND OBS_MAIN.record_status_cd = ''UNPROCESSED'' AND OBS_MAIN.ctrl_cd_display_form = ''LabReport'' AND OBS_MAIN.jurisdiction_cd is NOT NULL AND OBS_MAIN.prog_area_cd is NOT NULL
                       GROUP BY OBS_MAIN.observation_uid
                   ),
                   NumberedResults AS (
                       SELECT observation_uid, ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, observation_uid) AS RowNum
                       FROM ObservationResults
                   )
                   SELECT OBS_VALUE_NUMERIC.*
                   FROM OBS_VALUE_NUMERIC
                   INNER JOIN NumberedResults
                       ON OBS_VALUE_NUMERIC.observation_uid = NumberedResults.observation_uid
                   WHERE NumberedResults.RowNum BETWEEN :startRow AND :endRow;'
               );
    END;

IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'OBS_VALUE_TXT')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config] (
            table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination
        )
        VALUES (
                   'OBS_VALUE_TXT',
                   'NBS_ODSE',
                   'WITH ObservationResults AS (
                       SELECT DISTINCT OBS_MAIN.observation_uid
                       FROM OBSERVATION AS OBS_MAIN  JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND OBS_MAIN.obs_domain_cd_st_1 = ''Order'' AND OBS_MAIN.record_status_cd = ''UNPROCESSED'' AND OBS_MAIN.ctrl_cd_display_form = ''LabReport'' AND OBS_MAIN.jurisdiction_cd is NOT NULL AND OBS_MAIN.prog_area_cd is NOT NULL
                   )
                   SELECT OBS_VALUE_TXT.*
                   FROM OBS_VALUE_TXT
                   WHERE OBS_VALUE_TXT.observation_uid IN (SELECT observation_uid FROM ObservationResults);',
                   NULL,
                   'SELECT COUNT(*)
                    FROM OBS_VALUE_TXT
                    WHERE observation_uid IN (
                        SELECT DISTINCT OBS_MAIN.observation_uid
                        FROM OBSERVATION AS OBS_MAIN  JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
                        WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND OBS_MAIN.obs_domain_cd_st_1 = ''Order'' AND OBS_MAIN.record_status_cd = ''UNPROCESSED'' AND OBS_MAIN.ctrl_cd_display_form = ''LabReport'' AND OBS_MAIN.jurisdiction_cd is NOT NULL AND OBS_MAIN.prog_area_cd is NOT NULL
                    );',
                   'WITH ObservationResults AS (
                       SELECT DISTINCT OBS_MAIN.observation_uid,
                           MAX(CASE
                               WHEN OBS_MAIN.add_time >= OBS_MAIN.last_chg_time THEN OBS_MAIN.add_time
                               ELSE OBS_MAIN.last_chg_time
                           END) AS latest_timestamp
                       FROM OBSERVATION AS OBS_MAIN  JOIN Participation p ON OBS_MAIN.observation_uid = p.act_uid AND p.type_cd = ''AUT'' JOIN Act_relationship ar ON OBS_MAIN.observation_uid = ar.target_act_uid AND ar.type_cd = ''COMP'' JOIN Observation o1 ON ar.source_act_uid = o1.observation_uid AND o1.obs_domain_cd_st_1 = ''Result''
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp) AND OBS_MAIN.obs_domain_cd_st_1 = ''Order'' AND OBS_MAIN.record_status_cd = ''UNPROCESSED'' AND OBS_MAIN.ctrl_cd_display_form = ''LabReport'' AND OBS_MAIN.jurisdiction_cd is NOT NULL AND OBS_MAIN.prog_area_cd is NOT NULL
                       GROUP BY OBS_MAIN.observation_uid
                   ),
                   NumberedResults AS (
                       SELECT observation_uid, ROW_NUMBER() OVER (ORDER BY latest_timestamp ASC, observation_uid) AS RowNum
                       FROM ObservationResults
                   )
                   SELECT OBS_VALUE_TXT.*
                   FROM OBS_VALUE_TXT
                   INNER JOIN NumberedResults
                       ON OBS_VALUE_TXT.observation_uid = NumberedResults.observation_uid
                   WHERE NumberedResults.RowNum BETWEEN :startRow AND :endRow;'
               );
    END;