MERGE INTO my_table AS target
    USING (SELECT ? AS id, ? AS name, ? AS value) AS source
    ON target.id = source.id
    WHEN MATCHED THEN
        UPDATE SET target.name = source.name, target.value = source.value
    WHEN NOT MATCHED THEN
        INSERT (id, name, value) VALUES (source.id, source.name, source.value);