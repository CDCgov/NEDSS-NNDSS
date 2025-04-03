DELETE FROM [dbo].[data_sync_config] WHERE table_name IN ('ENTITY', 'ROLE', 'PERSON', 'ACT', 'OBSERVATION', 'PARTICIPATION', 'ACT_RELATIONSHIP');

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
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ROLE')
    BEGIN

        INSERT INTO [dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                              query_with_pagination)
        VALUES
            ('ROLE',
             'NBS_ODSE',
             'SELECT * FROM ROLE  WHERE last_chg_time :operator :timestamp;',

             NULL,
             'SELECT COUNT(*) FROM ROLE  WHERE last_chg_time :operator :timestamp;',
             'WITH PaginatedResults AS (SELECT ROLE.*, ROW_NUMBER() OVER (ORDER BY ROLE.subject_entity_uid, ROLE.role_seq, ROLE.cd) AS RowNum FROM ROLE  WHERE last_chg_time :operator :timestamp ) SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;'
            );

    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PERSON')
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
                   'PERSON',
                   'NBS_ODSE',

                   'SELECT * FROM PERSON WHERE (
                       (
                           SELECT MAX(val) FROM (VALUES
                               (last_chg_time),
                               (add_time),
                               (record_status_time),
                               (status_time)
                           ) AS AllTimes(val)
                       ) :operator :timestamp
                       OR (
                           last_chg_time IS NULL AND
                           add_time IS NULL AND
                           record_status_time IS NULL AND
                           status_time IS NULL
                       )
                   );',

                   NULL,

                   'SELECT COUNT(*) FROM PERSON WHERE (
                       (
                           SELECT MAX(val) FROM (VALUES
                               (last_chg_time),
                               (add_time),
                               (record_status_time),
                               (status_time)
                           ) AS AllTimes(val)
                       ) :operator :timestamp
                       OR (
                           last_chg_time IS NULL AND
                           add_time IS NULL AND
                           record_status_time IS NULL AND
                           status_time IS NULL
                       )
                   );',

                   'WITH PaginatedResults AS (
                       SELECT PERSON.*,
                              ROW_NUMBER() OVER (ORDER BY PERSON.person_parent_uid, PERSON.person_uid) AS RowNum
                       FROM PERSON
                       WHERE (
                           (
                               SELECT MAX(val) FROM (VALUES
                                   (last_chg_time),
                                   (add_time),
                                   (record_status_time),
                                   (status_time)
                               ) AS AllTimes(val)
                           ) :operator :timestamp
                           OR (
                               last_chg_time IS NULL AND
                               add_time IS NULL AND
                               record_status_time IS NULL AND
                               status_time IS NULL
                           )
                       )
                   )
                   SELECT * FROM PaginatedResults
                   WHERE RowNum BETWEEN :startRow AND :endRow;'
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


IF NOT EXISTS (
    SELECT 1
    FROM [dbo].[data_sync_config]
    WHERE table_name = 'OBSERVATION'
)
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
                   'OBSERVATION',
                   'NBS_ODSE',

                   'SELECT * FROM OBSERVATION WHERE (
                       (
                           SELECT MAX(val) FROM (VALUES
                               (last_chg_time),
                               (add_time),
                               (status_time),
                               (activity_from_time),
                               (rpt_to_state_time),
                               (record_status_time)
                           ) AS AllTimes(val)
                       ) :operator :timestamp
                       OR (
                           last_chg_time IS NULL AND
                           add_time IS NULL AND
                           status_time IS NULL AND
                           activity_from_time IS NULL AND
                           rpt_to_state_time IS NULL AND
                           record_status_time IS NULL
                       )
                   );',

                   NULL,

                   'SELECT COUNT(*) FROM OBSERVATION WHERE (
                       (
                           SELECT MAX(val) FROM (VALUES
                               (last_chg_time),
                               (add_time),
                               (status_time),
                               (activity_from_time),
                               (rpt_to_state_time),
                               (record_status_time)
                           ) AS AllTimes(val)
                       ) :operator :timestamp
                       OR (
                           last_chg_time IS NULL AND
                           add_time IS NULL AND
                           status_time IS NULL AND
                           activity_from_time IS NULL AND
                           rpt_to_state_time IS NULL AND
                           record_status_time IS NULL
                       )
                   );',

                   'WITH PaginatedResults AS (
                       SELECT OBSERVATION.*,
                              ROW_NUMBER() OVER (ORDER BY observation_uid) AS RowNum
                       FROM OBSERVATION
                       WHERE (
                           (
                               SELECT MAX(val) FROM (VALUES
                                   (last_chg_time),
                                   (add_time),
                                   (status_time),
                                   (activity_from_time),
                                   (rpt_to_state_time),
                                   (record_status_time)
                               ) AS AllTimes(val)
                           ) :operator :timestamp
                           OR (
                               last_chg_time IS NULL AND
                               add_time IS NULL AND
                               status_time IS NULL AND
                               activity_from_time IS NULL AND
                               rpt_to_state_time IS NULL AND
                               record_status_time IS NULL
                           )
                       )
                   )
                   SELECT * FROM PaginatedResults
                   WHERE RowNum BETWEEN :startRow AND :endRow;'
               );
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'PARTICIPATION')
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
                   'PARTICIPATION',
                   'NBS_ODSE',

                   'SELECT * FROM PARTICIPATION WHERE (
                       (
                           SELECT MAX(val) FROM (VALUES
                               (last_chg_time),
                               (add_time),
                               (record_status_time),
                               (status_time)
                           ) AS AllTimes(val)
                       ) :operator :timestamp
                       OR (
                           last_chg_time IS NULL AND
                           add_time IS NULL AND
                           record_status_time IS NULL AND
                           status_time IS NULL
                       )
                   );',

                   NULL,

                   'SELECT COUNT(*) FROM PARTICIPATION WHERE (
                       (
                           SELECT MAX(val) FROM (VALUES
                               (last_chg_time),
                               (add_time),
                               (record_status_time),
                               (status_time)
                           ) AS AllTimes(val)
                       ) :operator :timestamp
                       OR (
                           last_chg_time IS NULL AND
                           add_time IS NULL AND
                           record_status_time IS NULL AND
                           status_time IS NULL
                       )
                   );',

                   'WITH PaginatedResults AS (
                       SELECT PARTICIPATION.*,
                              ROW_NUMBER() OVER (ORDER BY PARTICIPATION.subject_entity_uid, PARTICIPATION.act_uid, PARTICIPATION.type_cd) AS RowNum
                       FROM PARTICIPATION
                       WHERE (
                           (
                               SELECT MAX(val) FROM (VALUES
                                   (last_chg_time),
                                   (add_time),
                                   (record_status_time),
                                   (status_time)
                               ) AS AllTimes(val)
                           ) :operator :timestamp
                           OR (
                               last_chg_time IS NULL AND
                               add_time IS NULL AND
                               record_status_time IS NULL AND
                               status_time IS NULL
                           )
                       )
                   )
                   SELECT * FROM PaginatedResults
                   WHERE RowNum BETWEEN :startRow AND :endRow;'
               );
    END;


IF
    NOT EXISTS (SELECT 1 FROM [dbo].[data_sync_config] WHERE table_name = 'ACT_RELATIONSHIP')
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
                   'ACT_RELATIONSHIP',
                   'NBS_ODSE',

                   'SELECT * FROM ACT_RELATIONSHIP WHERE (
                       last_chg_time :operator :timestamp
                   );',

                   NULL,

                   'SELECT COUNT(*) FROM ACT_RELATIONSHIP WHERE (
                       last_chg_time :operator :timestamp
                   );',

                   'WITH PaginatedResults AS (
                       SELECT ACT_RELATIONSHIP.*,
                              ROW_NUMBER() OVER (ORDER BY source_act_uid, target_act_uid, type_cd) AS RowNum
                       FROM ACT_RELATIONSHIP
                       WHERE (
                           last_chg_time :operator :timestamp
                       )
                   )
                   SELECT * FROM PaginatedResults
                   WHERE RowNum BETWEEN :startRow AND :endRow;'
               );
    END;
