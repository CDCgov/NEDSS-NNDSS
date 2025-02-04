package gov.cdc.nnddatapollservice.share;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
