package gov.cdc.nnddatapollservice.share;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JdbcTemplateUtil {
    private final JdbcTemplate rdbJdbcTemplate;

    public JdbcTemplateUtil(@Qualifier("rdbJdbcTemplate") JdbcTemplate rdbJdbcTemplate) {
        this.rdbJdbcTemplate = rdbJdbcTemplate;
    }

    public void upsert(String tableName, Map<String, Object> data) throws SQLException {
        if (data.isEmpty()) {
            return;
        }
        Set<String> columnNames = getColumnNames(tableName);
        data.keySet().retainAll(columnNames);
        if (data.isEmpty()) {
            throw new IllegalArgumentException("No valid columns found for table: " + tableName);
        }

        String columns = String.join(", ", data.keySet());
        String values = String.join(", ", Collections.nCopies(data.size(), "?"));
        String updates = String.join(", ", data.keySet().stream().map(col -> col + " = EXCLUDED." + col).toList());

        // Construct the dynamic MERGE statement
        String sql = " MERGE INTO "
                + tableName
                + " AS target USING (SELECT "
                + values
                + " ) AS source("
                + columns
                + ") ON (target.id = source.id) WHEN MATCHED THEN UPDATE SET "
                + updates
                + " WHEN NOT MATCHED THEN INSERT ("
                + columns
                + ") VALUES ("
                + values
                + "); ";

        rdbJdbcTemplate.update(sql, data.values().toArray());
    }

    public void upsertBatch(String tableName, List<Map<String, Object>> dataList) throws SQLException {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        // Extract all possible columns from the first record
        Set<String> columnNames = getColumnNames(tableName);
        Set<String> validColumns = new HashSet<>(columnNames);

        // Retain only valid columns in each data entry
        List<Map<String, Object>> validDataList = new ArrayList<>();
        for (Map<String, Object> data : dataList) {
            Map<String, Object> validData = new LinkedHashMap<>(data);
            validData.keySet().retainAll(validColumns);
            if (!validData.isEmpty()) {
                validDataList.add(validData);
            }
        }

        if (validDataList.isEmpty()) {
            throw new IllegalArgumentException("No valid columns found for table: " + tableName);
        }

        // Extract column names and generate query parts
        List<String> columnList = new ArrayList<>(validDataList.get(0).keySet());
        String columns = String.join(", ", columnList);
        String placeholders = String.join(", ", Collections.nCopies(columnList.size(), "?"));
        String updates = columnList.stream()
                .map(col -> "target." + col + " = source." + col)
                .collect(Collectors.joining(", "));

        // Construct the dynamic MERGE statement
        String sql = "MERGE INTO " + tableName + " AS target " +
                "USING (VALUES " +
                validDataList.stream()
                        .map(d -> "(" + placeholders + ")")
                        .collect(Collectors.joining(", ")) +
                ") AS source(" + columns + ") " +
                "ON target.id = source.id " +
                "WHEN MATCHED THEN UPDATE SET " + updates + " " +
                "WHEN NOT MATCHED THEN INSERT (" + columns + ") VALUES (" + placeholders + ");";

        // Flatten values for batch execution
        List<Object> batchValues = new ArrayList<>();
        for (Map<String, Object> data : validDataList) {
            batchValues.addAll(data.values());
        }

        // Execute batch update
        rdbJdbcTemplate.update(sql, batchValues.toArray());
    }


    private Set<String> getColumnNames(String tableName) throws SQLException {
        Set<String> columnNames = new HashSet<>();
        DatabaseMetaData metaData = rdbJdbcTemplate.getDataSource().getConnection().getMetaData();
        try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
            while (columns.next()) {
                columnNames.add(columns.getString("COLUMN_NAME"));
            }
        }
        return columnNames;
    }
}
