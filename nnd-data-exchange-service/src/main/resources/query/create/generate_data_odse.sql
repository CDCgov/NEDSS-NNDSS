INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                            query_with_pagination)
VALUES
    ('ENTITY', 'NBS_ODSE', 'WITH EntityResults AS (
    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PARTICIPATION
    ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
    INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
)
SELECT ENTITY.*
FROM ENTITY
INNER JOIN EntityResults
ON ENTITY.entity_uid = EntityResults.entity_uid;', NULL, 'SELECT COUNT(*) FROM (
    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PARTICIPATION
    ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
    INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ENTITY.entity_uid
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
) AS ENTITY_COUNT;
', 'WITH PaginatedResults AS (
    SELECT DISTINCT ENTITY.entity_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ENTITY.entity_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM ENTITY
    INNER JOIN PARTICIPATION
    ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
    INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ENTITY.entity_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM ENTITY
    INNER JOIN PERSON
    ON ENTITY.entity_uid = PERSON.person_uid
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
)
SELECT ENTITY.*
FROM ENTITY
INNER JOIN PaginatedResults
ON ENTITY.entity_uid = PaginatedResults.entity_uid
WHERE PaginatedResults.RowNum BETWEEN :startRow AND :endRow;');

INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                            query_with_pagination)
VALUES
    ('ROLE', 'NBS_ODSE', 'SELECT DISTINCT ROLE.* FROM ROLE
INNER JOIN PARTICIPATION
ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
AND ROLE.role_seq = PARTICIPATION.role_seq
AND ROLE.cd = PARTICIPATION.cd
INNER JOIN OBSERVATION
ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp);', NULL, 'SELECT COUNT(*) FROM (
    SELECT DISTINCT ROLE.* FROM ROLE
    INNER JOIN PARTICIPATION
    ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
    AND ROLE.role_seq = PARTICIPATION.role_seq
    AND ROLE.cd = PARTICIPATION.cd
    INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
) AS ROLE_COUNT;', 'WITH PaginatedResults AS (
    SELECT DISTINCT ROLE.*, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM ROLE
    INNER JOIN PARTICIPATION
    ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
    AND ROLE.role_seq = PARTICIPATION.role_seq
    AND ROLE.cd = PARTICIPATION.cd
    INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
)
SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');


INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                            query_with_pagination)
VALUES
    ('PERSON', 'NBS_ODSE', 'WITH PersonResults AS (
    SELECT DISTINCT PERSON.person_uid
    FROM PERSON
    INNER JOIN OBSERVATION
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT PERSON.person_uid
    FROM PERSON
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
)
SELECT PERSON.*
FROM PERSON
INNER JOIN PersonResults
ON PERSON.person_uid = PersonResults.person_uid;', NULL, 'SELECT COUNT(*) FROM (
    SELECT DISTINCT PERSON.person_uid
    FROM PERSON
    INNER JOIN OBSERVATION
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT PERSON.person_uid
    FROM PERSON
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.observation_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
) AS PERSON_COUNT;', 'WITH PersonResults AS (
    SELECT DISTINCT PERSON.person_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM PERSON
    INNER JOIN OBSERVATION
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT PERSON.person_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM PERSON
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON PERSON.person_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
)
SELECT PERSON.*
FROM PERSON
INNER JOIN PersonResults
ON PERSON.person_uid = PersonResults.person_uid
WHERE PersonResults.RowNum BETWEEN :startRow AND :endRow;');


INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                            query_with_pagination)
VALUES
    ('ACT', 'NBS_ODSE', 'WITH ActResults AS (
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
    ON Act.act_uid = Act_relationship.source_act_uid
    INNER JOIN OBSERVATION
    ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ACT.act_uid
    FROM ACT
    INNER JOIN Act_relationship
    ON Act.act_uid = Act_relationship.target_act_uid
    INNER JOIN OBSERVATION
    ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ACT.act_uid
    FROM ACT
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON ACT.act_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
)
SELECT ACT.*
FROM ACT
INNER JOIN ActResults
ON ACT.act_uid = ActResults.act_uid;', NULL, 'SELECT COUNT(*) FROM (
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
    ON Act.act_uid = Act_relationship.source_act_uid
    INNER JOIN OBSERVATION
    ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ACT.act_uid
    FROM ACT
    INNER JOIN Act_relationship
    ON Act.act_uid = Act_relationship.target_act_uid
    INNER JOIN OBSERVATION
    ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ACT.act_uid
    FROM ACT
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON ACT.act_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
) AS ACT_COUNT;', 'WITH ActResults AS (
    SELECT DISTINCT ACT.act_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM ACT
    INNER JOIN OBSERVATION
    ON ACT.act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ACT.act_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM ACT
    INNER JOIN PARTICIPATION
    ON ACT.act_uid = PARTICIPATION.act_uid
    INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ACT.act_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM ACT
    INNER JOIN Act_relationship
    ON Act.act_uid = Act_relationship.source_act_uid
    INNER JOIN OBSERVATION
    ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ACT.act_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM ACT
    INNER JOIN Act_relationship
    ON Act.act_uid = Act_relationship.target_act_uid
    INNER JOIN OBSERVATION
    ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT ACT.act_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM ACT
    INNER JOIN OBSERVATION as OBS_DOMAIN
    ON ACT.act_uid = OBS_DOMAIN.subject_person_uid
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_DOMAIN.observation_uid = OBS_MAIN.subject_person_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
)
SELECT ACT.*
FROM ACT
INNER JOIN ActResults
ON ACT.act_uid = ActResults.act_uid
WHERE ActResults.RowNum BETWEEN :startRow AND :endRow;');

INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                            query_with_pagination)
VALUES
    ('OBSERVATION', 'NBS_ODSE', 'WITH ObservationResults AS (
    SELECT DISTINCT OBSERVATION.observation_uid
    FROM OBSERVATION
    INNER JOIN ACT
    ON ACT.act_uid = OBSERVATION.observation_uid
    INNER JOIN PERSON
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT OBS_DOMAIN.observation_uid
    FROM OBSERVATION as OBS_DOMAIN
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
)
SELECT OBSERVATION.*
FROM OBSERVATION
INNER JOIN ObservationResults
ON OBSERVATION.observation_uid = ObservationResults.observation_uid;', NULL, 'SELECT COUNT(*) FROM (
    SELECT DISTINCT OBSERVATION.observation_uid
    FROM OBSERVATION
    INNER JOIN ACT
    ON ACT.act_uid = OBSERVATION.observation_uid
    INNER JOIN PERSON
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT OBS_DOMAIN.observation_uid
    FROM OBSERVATION as OBS_DOMAIN
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
) AS OBSERVATION_COUNT;
', 'WITH ObservationResults AS (
    SELECT DISTINCT OBSERVATION.observation_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM OBSERVATION
    INNER JOIN ACT
    ON ACT.act_uid = OBSERVATION.observation_uid
    INNER JOIN PERSON
    ON PERSON.person_uid = OBSERVATION.subject_person_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)

    UNION

    SELECT DISTINCT OBS_DOMAIN.observation_uid, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM OBSERVATION as OBS_DOMAIN
    INNER JOIN OBSERVATION as OBS_MAIN
    ON OBS_MAIN.observation_uid = OBS_DOMAIN.observation_uid
    WHERE (OBS_MAIN.add_time :operator :timestamp OR OBS_MAIN.last_chg_time :operator :timestamp)
)
SELECT OBSERVATION.*
FROM OBSERVATION
INNER JOIN ObservationResults
ON OBSERVATION.observation_uid = ObservationResults.observation_uid
WHERE ObservationResults.RowNum BETWEEN :startRow AND :endRow;');

INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                            query_with_pagination)
VALUES
    ('PARTICIPATION', 'NBS_ODSE', 'SELECT DISTINCT PARTICIPATION.* FROM PARTICIPATION
INNER JOIN ACT
ON ACT.act_uid = PARTICIPATION.act_uid
INNER JOIN OBSERVATION
ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
INNER JOIN ENTITY
ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
INNER JOIN ROLE
ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
AND ROLE.role_seq = PARTICIPATION.role_seq
AND ROLE.cd = PARTICIPATION.cd
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp);', NULL, 'SELECT COUNT(*) FROM (
    SELECT DISTINCT PARTICIPATION.* FROM PARTICIPATION
    INNER JOIN ACT
    ON ACT.act_uid = PARTICIPATION.act_uid
    INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
    INNER JOIN ENTITY
    ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
    INNER JOIN ROLE
    ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
    AND ROLE.role_seq = PARTICIPATION.role_seq
    AND ROLE.cd = PARTICIPATION.cd
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
) AS PARTICIPATION_COUNT;', 'WITH PaginatedResults AS (
    SELECT DISTINCT PARTICIPATION.*, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM PARTICIPATION
    INNER JOIN ACT
    ON ACT.act_uid = PARTICIPATION.act_uid
    INNER JOIN OBSERVATION
    ON PARTICIPATION.act_uid = OBSERVATION.observation_uid
    INNER JOIN ENTITY
    ON ENTITY.entity_uid = PARTICIPATION.subject_entity_uid
    INNER JOIN ROLE
    ON ROLE.subject_entity_uid = PARTICIPATION.subject_entity_uid
    AND ROLE.role_seq = PARTICIPATION.role_seq
    AND ROLE.cd = PARTICIPATION.cd
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
)
SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;');


INSERT INTO [RDB].[dbo].[data_sync_config] (table_name, source_db, query, query_with_null_timestamp, query_count,
                                            query_with_pagination)
VALUES
    ('Act_relationship', 'NBS_ODSE', 'SELECT DISTINCT Act_relationship.* FROM Act_relationship
INNER JOIN OBSERVATION
ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
INNER JOIN ACT as ACT_SOURCE
ON ACT_SOURCE.act_uid = Act_relationship.source_act_uid
INNER JOIN ACT as ACT_TARGET
ON ACT_TARGET.act_uid = Act_relationship.target_act_uid
WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp);', NULL, 'SELECT COUNT(*) FROM (
    SELECT DISTINCT Act_relationship.* FROM Act_relationship
    INNER JOIN OBSERVATION
    ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
    INNER JOIN ACT as ACT_SOURCE
    ON ACT_SOURCE.act_uid = Act_relationship.source_act_uid
    INNER JOIN ACT as ACT_TARGET
    ON ACT_TARGET.act_uid = Act_relationship.target_act_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
) AS ACT_RELATIONSHIP_COUNT;', 'WITH PaginatedResults AS (
    SELECT DISTINCT Act_relationship.*, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum
    FROM Act_relationship
    INNER JOIN OBSERVATION
    ON Act_relationship.target_act_uid = OBSERVATION.observation_uid
    INNER JOIN ACT as ACT_SOURCE
    ON ACT_SOURCE.act_uid = Act_relationship.source_act_uid
    INNER JOIN ACT as ACT_TARGET
    ON ACT_TARGET.act_uid = Act_relationship.target_act_uid
    WHERE (OBSERVATION.add_time :operator :timestamp OR OBSERVATION.last_chg_time :operator :timestamp)
)
SELECT * FROM PaginatedResults WHERE RowNum BETWEEN :startRow AND :endRow;
');










