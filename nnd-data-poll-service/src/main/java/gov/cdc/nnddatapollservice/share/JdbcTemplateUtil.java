package gov.cdc.nnddatapollservice.share;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
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

    @SuppressWarnings("java:S1192")
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

    @SuppressWarnings("java:S3776")
    public void upsertBatch(String tableName, List<Map<String, Object>> dataList, String keyList) throws SQLException {

        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        if (keyList == null || keyList.trim().isEmpty()) {
            throw new IllegalArgumentException("Key list cannot be null or empty");
        }

        dataList.forEach(data -> data.remove("RowNum"));
        // Extract valid column names from the data list directly
        Set<String> validColumns = dataList.stream()
                .flatMap(map -> map.keySet().stream())
                .collect(Collectors.toSet());

        // Filter data and retain only valid columns
        List<Map<String, Object>> validDataList = dataList.stream()
                .map(data -> {
                    Map<String, Object> filteredData = new LinkedHashMap<>(data);
                    filteredData.keySet().retainAll(validColumns);
                    return filteredData.isEmpty() ? null : filteredData;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()); //NOSONAR

        if (validDataList.isEmpty()) {
            throw new IllegalArgumentException("No valid columns found for table: " + tableName);
        }

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

        String[] keys = keyList.split("\\s*,\\s*");
        StringBuilder condition = new StringBuilder("ON ");
        for (int k = 0; k < keys.length; k++) {
            if (k > 0) {
                condition.append(" AND ");
            }
            condition.append("target.").append(keys[k].trim()).append(" = source.").append(keys[k].trim());
        }

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
                        " USING (VALUES " + rowPlaceholders + ") AS source(" + columns + ") " +
                         condition +
                        " WHEN MATCHED THEN UPDATE SET " + updates + " " +
                        " WHEN NOT MATCHED BY TARGET THEN INSERT (" + columns + ") VALUES (" + valuesForQuery + ");";

                // Execute batch update using JdbcTemplate
                rdbJdbcTemplate.update(sql, combinedValues.toArray());
            }
        } catch (Exception e) {
            throw new SQLException("Batch update failed", e);
        }
    }


    private Set<String> getColumnNames(String tableName) throws SQLException {
        Set<String> columnNames = new HashSet<>();

        try (Connection connection = Objects.requireNonNull(rdbJdbcTemplate.getDataSource()).getConnection();
             ResultSet columns = connection.getMetaData().getColumns(null, null, tableName, null)) {

            while (columns.next()) {
                columnNames.add(columns.getString("COLUMN_NAME"));
            }
        }
        return columnNames;
    }

}
