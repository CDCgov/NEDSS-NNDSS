package gov.cdc.nnddatapollservice.rdb.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SrteDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(RdbDataPersistentDAO.class);
    private JdbcTemplate jdbcTemplate;
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    @Autowired
    public SrteDataPersistentDAO(@Qualifier("srteJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

   // @SuppressWarnings("java:S3776")
    public int saveSRTEData(String tableName, String jsonData) {
        logger.info("saveSRTEData tableName: {}", tableName);
        int noOfRecordsSaved = 0;
        try {
            SimpleJdbcInsert simpleJdbcInsert =
                    new SimpleJdbcInsert(jdbcTemplate);
            if (tableName != null && !tableName.isEmpty()) {
                deleteTable(tableName);//Delete first
                simpleJdbcInsert = simpleJdbcInsert.withTableName(tableName);
                List<Map<String, Object>> records = jsonToListOfMap(jsonData);
                if (records != null && !records.isEmpty()) {
                    logger.info("Inside generic code before executeBatch tableName: {} Records size:{}", tableName, records.size());
                    int[] noOfInserts = simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
                    noOfRecordsSaved = noOfInserts.length;
                    logger.info("executeBatch completed. tableName: {}", tableName);
                } else {
                    logger.info("Inside generic code tableName: {} Records size:0", tableName);
                }
            }
        } catch (Exception e) {
            logger.error("Error executeBatch. tableName: {}, Error:{}", tableName, e.getMessage());
        }
        logger.info("saveSRTEData tableName: {} Records size:{}", tableName, noOfRecordsSaved);
        return noOfRecordsSaved;
    }

    private List<Map<String, Object>> jsonToListOfMap(String jsonData) {
        List<Map<String, Object>> list = null;
        if (jsonData != null && !jsonData.isEmpty()) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            Type resultType = new TypeToken<List<Map<String, Object>>>() {
            }.getType();
            list = gson.fromJson(jsonData, resultType);
        }
        return list;
    }

    private void deleteTable(String tableName) {
        String deleteSql = "delete " + tableName;
        jdbcTemplate.execute(deleteSql);
    }
}
