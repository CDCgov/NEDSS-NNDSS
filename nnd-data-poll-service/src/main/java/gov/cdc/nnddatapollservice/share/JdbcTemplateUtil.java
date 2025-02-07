package gov.cdc.nnddatapollservice.share;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JdbcTemplateUtil {
    private final JdbcTemplate rdbJdbcTemplate;

    public JdbcTemplateUtil(@Qualifier("rdbJdbcTemplate") JdbcTemplate rdbJdbcTemplate) {
        this.rdbJdbcTemplate = rdbJdbcTemplate;
    }

    public void upsertSingle(String tableName, Map<String, Object> data) throws SQLException {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("No data provided for table: " + tableName);
        }

        data.entrySet().removeIf(entry -> entry.getKey().equalsIgnoreCase("RowNum"));


        // Extract valid column names from the database
        Set<String> columnNames = getColumnNames(tableName); // Assume this retrieves the column list
        Map<String, Object> validData = new LinkedHashMap<>(data);
        validData.keySet().retainAll(columnNames); // Keep only valid columns

        if (validData.isEmpty()) {
            throw new IllegalArgumentException("No valid columns found for table: " + tableName);
        }

        // Extract column names and generate query parts
        List<String> columnList = new ArrayList<>(validData.keySet());
        String columns = String.join(", ", columnList);
        String placeholders = "(" + String.join(", ", Collections.nCopies(columnList.size(), "?")) + ")";
        String updates = columnList.stream()
                .map(col -> "target." + col + " = source." + col)
                .collect(Collectors.joining(", "));

//        var valuesForQuery = String.join(", ", Collections.nCopies(columnList.size(), "source." + columns));
        var valuesForQuery = columnList.stream()
                .map(col -> "source." + col)
                .collect(Collectors.joining(", "));
        // Construct the dynamic MERGE statement for a single record
        String sql = "MERGE INTO " + tableName + " AS target " +
                "USING (VALUES " + placeholders + ") AS source(" + columns + ") " +
                "ON target. "+ "observation_uid" + " = source."+ "observation_uid" + " " +
                "WHEN MATCHED THEN UPDATE SET " + updates + " " +
                "WHEN NOT MATCHED THEN INSERT (" + columns + ") VALUES (" +
                valuesForQuery + ");";

        var values = validData.values().toArray();
        // Execute the statement
        rdbJdbcTemplate.update(sql, values);
    }

    public void upsertBatch(String tableName, List<Map<String, Object>> dataList) throws SQLException {

        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        dataList.forEach(data -> data.remove("RowNum")); // Remove "RowNum"

        // Extract valid column names from the data list directly
        Set<String> validColumns = dataList.stream()
                .flatMap(map -> map.keySet().stream())
                .collect(Collectors.toSet());

        // Filter data and retain only valid columns
        List<Map<String, Object>> validDataList = dataList.stream()
                .map(data -> {
                    Map<String, Object> filteredData = new LinkedHashMap<>(data);
                    filteredData.keySet().retainAll(validColumns); // Retain only valid columns
                    return filteredData.isEmpty() ? null : filteredData;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (validDataList.isEmpty()) {
            throw new IllegalArgumentException("No valid columns found for table: " + tableName);
        }

        // Extract column names (AFTER filtering)
        List<String> columnList = new ArrayList<>(validDataList.get(0).keySet());
        String columns = String.join(", ", columnList);

        // Prepare query parts
        String placeholdersPerRow = "(" + String.join(", ", Collections.nCopies(columnList.size(), "?")) + ")";
        String updates = columnList.stream()
                .map(col -> "target." + col + " = source." + col)
                .collect(Collectors.joining(", "));
        String valuesForQuery = columnList.stream()
                .map(col -> "source." + col)
                .collect(Collectors.joining(", "));

        // **Calculate Maximum Rows Per Batch**
        int maxParamsPerRow = columnList.size();
        int maxRows = 2100 / maxParamsPerRow; // Ensure within SQL Server limit
        int totalRows = validDataList.size();

        try {
            for (int i = 0; i < totalRows; i += maxRows) {
                List<Map<String, Object>> batch = validDataList.subList(i, Math.min(i + maxRows, totalRows));

                // Combine all batch values into a single list
                List<Object> combinedValues = new ArrayList<>();
                for (Map<String, Object> data : batch) {
                    for (String column : columnList) {
                        combinedValues.add(data.getOrDefault(column, null)); // Add each column value
                    }
                }

                // Generate row placeholders dynamically
                String rowPlaceholders = String.join(", ", Collections.nCopies(batch.size(), placeholdersPerRow));

                // Build the MERGE SQL query
                String sql = "MERGE INTO " + tableName + " AS target " +
                        "USING (VALUES " + rowPlaceholders + ") AS source(" + columns + ") " +
                        "ON target.observation_uid = source.observation_uid " +
                        "WHEN MATCHED THEN UPDATE SET " + updates + " " +
                        "WHEN NOT MATCHED BY TARGET THEN INSERT (" + columns + ") VALUES (" + valuesForQuery + ");";

                // Execute batch update using JdbcTemplate
                rdbJdbcTemplate.update(sql, combinedValues.toArray());
            }
        } catch (Exception e) {
            throw new SQLException("Batch update failed", e);
        }
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

    private String formatQueryWithValues(String sql, List<Object> values) {
        Iterator<Object> iterator = values.iterator();
        StringBuilder formattedQuery = new StringBuilder();
        int paramIndex = 0;

        for (char c : sql.toCharArray()) {
            if (c == '?' && iterator.hasNext()) {
                Object value = iterator.next();
                formattedQuery.append(value == null ? "NULL" : "'" + value.toString().replace("'", "''") + "'");
                paramIndex++;
            } else {
                formattedQuery.append(c);
            }
        }

        return formattedQuery.toString();
    }
}
