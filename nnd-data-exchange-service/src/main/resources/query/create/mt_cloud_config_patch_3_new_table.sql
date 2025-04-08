delete from data_sync_config where table_name in ('ENTITY_ID', 'Obs_value_coded', 'Obs_value_numeric', 'Obs_value_txt', 'Organization', 'Organization_name', 'Public_health_case', 'PublicHealthCaseFact');


IF NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ENTITY_ID')
    BEGIN
        INSERT INTO [RDB].[dbo].[data_sync_config] (
            table_name, source_db, query, query_with_null_timestamp, query_count, query_with_pagination
        )
        VALUES (
                   'ENTITY_ID',
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
                   SELECT ENTITY_ID.*
                   FROM ENTITY_ID
                   WHERE ENTITY_ID.entity_uid IN (SELECT entity_uid FROM EntityResults);',
                   NULL,
                   'SELECT COUNT(*)
                    FROM ENTITY_ID
                    WHERE entity_uid IN (
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
                              MAX(CASE WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time
                                       THEN OBSERVATION.add_time
                                       ELSE OBSERVATION.last_chg_time END) AS latest_timestamp
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
                   SELECT ENTITY_ID.*
                   FROM ENTITY_ID
                   WHERE ENTITY_ID.entity_uid IN (
                       SELECT entity_uid FROM NumberedResults
                       WHERE RowNum BETWEEN :startRow AND :endRow
                   );'
               );
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
                       SELECT DISTINCT OBS_DOMAIN.observation_uid
                       FROM OBSERVATION AS OBS_DOMAIN
                       INNER JOIN OBSERVATION AS OBS_MAIN
                           ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
                   )
                   SELECT OBS_VALUE_CODED.*
                   FROM OBS_VALUE_CODED
                   WHERE OBS_VALUE_CODED.observation_uid IN (SELECT observation_uid FROM ObservationResults);',
                   NULL,
                   'SELECT COUNT(*)
                    FROM OBS_VALUE_CODED
                    WHERE observation_uid IN (
                        SELECT DISTINCT OBS_DOMAIN.observation_uid
                        FROM OBSERVATION AS OBS_DOMAIN
                        INNER JOIN OBSERVATION AS OBS_MAIN
                            ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
                        WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
                    );',
                   'WITH ObservationResults AS (
                       SELECT DISTINCT OBS_DOMAIN.observation_uid,
                           MAX(CASE
                               WHEN OBS_MAIN.add_time >= OBS_MAIN.last_chg_time THEN OBS_MAIN.add_time
                               ELSE OBS_MAIN.last_chg_time
                           END) AS latest_timestamp
                       FROM OBSERVATION AS OBS_DOMAIN
                       INNER JOIN OBSERVATION AS OBS_MAIN
                           ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
                       GROUP BY OBS_DOMAIN.observation_uid
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
                       SELECT DISTINCT OBS_DOMAIN.observation_uid
                       FROM OBSERVATION AS OBS_DOMAIN
                       INNER JOIN OBSERVATION AS OBS_MAIN
                           ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
                   )
                   SELECT OBS_VALUE_NUMERIC.*
                   FROM OBS_VALUE_NUMERIC
                   WHERE OBS_VALUE_NUMERIC.observation_uid IN (SELECT observation_uid FROM ObservationResults);',
                   NULL,
                   'SELECT COUNT(*)
                    FROM OBS_VALUE_NUMERIC
                    WHERE observation_uid IN (
                        SELECT DISTINCT OBS_DOMAIN.observation_uid
                        FROM OBSERVATION AS OBS_DOMAIN
                        INNER JOIN OBSERVATION AS OBS_MAIN
                            ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
                        WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
                    );',
                   'WITH ObservationResults AS (
                       SELECT DISTINCT OBS_DOMAIN.observation_uid,
                           MAX(CASE
                               WHEN OBS_MAIN.add_time >= OBS_MAIN.last_chg_time THEN OBS_MAIN.add_time
                               ELSE OBS_MAIN.last_chg_time
                           END) AS latest_timestamp
                       FROM OBSERVATION AS OBS_DOMAIN
                       INNER JOIN OBSERVATION AS OBS_MAIN
                           ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
                       GROUP BY OBS_DOMAIN.observation_uid
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
                       SELECT DISTINCT OBS_DOMAIN.observation_uid
                       FROM OBSERVATION AS OBS_DOMAIN
                       INNER JOIN OBSERVATION AS OBS_MAIN
                           ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
                   )
                   SELECT OBS_VALUE_TXT.*
                   FROM OBS_VALUE_TXT
                   WHERE OBS_VALUE_TXT.observation_uid IN (SELECT observation_uid FROM ObservationResults);',
                   NULL,
                   'SELECT COUNT(*)
                    FROM OBS_VALUE_TXT
                    WHERE observation_uid IN (
                        SELECT DISTINCT OBS_DOMAIN.observation_uid
                        FROM OBSERVATION AS OBS_DOMAIN
                        INNER JOIN OBSERVATION AS OBS_MAIN
                            ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
                        WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
                    );',
                   'WITH ObservationResults AS (
                       SELECT DISTINCT OBS_DOMAIN.observation_uid,
                           MAX(CASE
                               WHEN OBS_MAIN.add_time >= OBS_MAIN.last_chg_time THEN OBS_MAIN.add_time
                               ELSE OBS_MAIN.last_chg_time
                           END) AS latest_timestamp
                       FROM OBSERVATION AS OBS_DOMAIN
                       INNER JOIN OBSERVATION AS OBS_MAIN
                           ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
                       WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
                       GROUP BY OBS_DOMAIN.observation_uid
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
                       INNER JOIN OBSERVATION
                           ON ACT.act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

                       UNION

                       SELECT DISTINCT ACT.act_uid
                       FROM ACT
                       INNER JOIN PARTICIPATION
                           ON ACT.act_uid = PARTICIPATION.act_uid
                       INNER JOIN OBSERVATION
                           ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

                       UNION

                       SELECT DISTINCT ACT.act_uid
                       FROM ACT
                       INNER JOIN Act_relationship
                           ON ACT.act_uid = Act_relationship.source_act_uid
                       INNER JOIN OBSERVATION
                           ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

                       UNION

                       SELECT DISTINCT ACT.act_uid
                       FROM ACT
                       INNER JOIN Act_relationship
                           ON ACT.act_uid = Act_relationship.target_act_uid
                       INNER JOIN OBSERVATION
                           ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
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
                        INNER JOIN OBSERVATION
                            ON ACT.act_uid = OBSERVATION.observation_uid
                        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

                        UNION

                        SELECT DISTINCT ACT.act_uid
                        FROM ACT
                        INNER JOIN PARTICIPATION
                            ON ACT.act_uid = PARTICIPATION.act_uid
                        INNER JOIN OBSERVATION
                            ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

                        UNION

                        SELECT DISTINCT ACT.act_uid
                        FROM ACT
                        INNER JOIN Act_relationship
                            ON ACT.act_uid = Act_relationship.source_act_uid
                        INNER JOIN OBSERVATION
                            ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
                        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

                        UNION

                        SELECT DISTINCT ACT.act_uid
                        FROM ACT
                        INNER JOIN Act_relationship
                            ON ACT.act_uid = Act_relationship.target_act_uid
                        INNER JOIN OBSERVATION
                            ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
                        WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                    );',
                   'WITH ActResults AS (
                       SELECT DISTINCT ACT.act_uid,
                           MAX(CASE
                               WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
                               ELSE OBSERVATION.last_chg_time
                           END) AS latest_timestamp
                       FROM ACT
                       INNER JOIN OBSERVATION
                           ON ACT.act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                       GROUP BY ACT.act_uid

                       UNION

                       SELECT DISTINCT ACT.act_uid,
                           MAX(CASE
                               WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
                               ELSE OBSERVATION.last_chg_time
                           END) AS latest_timestamp
                       FROM ACT
                       INNER JOIN PARTICIPATION
                           ON ACT.act_uid = PARTICIPATION.act_uid
                       INNER JOIN OBSERVATION
                           ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                       GROUP BY ACT.act_uid

                       UNION

                       SELECT DISTINCT ACT.act_uid,
                           MAX(CASE
                               WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
                               ELSE OBSERVATION.last_chg_time
                           END) AS latest_timestamp
                       FROM ACT
                       INNER JOIN Act_relationship
                           ON ACT.act_uid = Act_relationship.source_act_uid
                       INNER JOIN OBSERVATION
                           ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
                       WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
                       GROUP BY ACT.act_uid

                       UNION

                       SELECT DISTINCT ACT.act_uid,
                           MAX(CASE
                               WHEN OBSERVATION.add_time >= OBSERVATION.last_chg_time THEN OBSERVATION.add_time
                               ELSE OBSERVATION.last_chg_time
                           END) AS latest_timestamp
                       FROM ACT
                       INNER JOIN Act_relationship
                           ON ACT.act_uid = Act_relationship.target_act_uid
                       INNER JOIN OBSERVATION
                           ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
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
