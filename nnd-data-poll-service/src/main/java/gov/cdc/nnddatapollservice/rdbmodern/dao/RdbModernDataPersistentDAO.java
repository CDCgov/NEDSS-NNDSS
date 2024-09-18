package gov.cdc.nnddatapollservice.rdbmodern.dao;

import gov.cdc.nnddatapollservice.constant.ConstantValue;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.LOG_SUCCESS;

@Service
@Slf4j
public class RdbModernDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(RdbModernDataPersistentDAO.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RdbModernDataPersistentDAO(@Qualifier("rdbmodernJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String saveRdbModernData(String tableName, String jsonData) {
        logger.info("saveRdbModernData tableName: {}", tableName);
        String log = LOG_SUCCESS;

        try {
            SimpleJdbcInsert simpleJdbcInsert =
                    new SimpleJdbcInsert(jdbcTemplate);
            if (tableName != null && !tableName.isEmpty()) {
                deleteTable(tableName);//Delete first
                simpleJdbcInsert = simpleJdbcInsert.withTableName(tableName);
                List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
                if (records != null && !records.isEmpty()) {
                    logger.info("Inside generic code before executeBatch tableName: {} Records size:{}", tableName, records.size());
                    if (records.size() > ConstantValue.SQL_BATCH_SIZE) {
                        int sublistSize = ConstantValue.SQL_BATCH_SIZE;
                        for (int i = 0; i < records.size(); i += sublistSize) {
                            int end = Math.min(i + sublistSize, records.size());
                            List<Map<String, Object>> sublist = records.subList(i, end);
                            simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(sublist));
                        }
                    } else {
                        simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
                    }
                } else {
                    logger.info("saveRdbModernData tableName: {} Records size:0", tableName);
                }
            }

        } catch (Exception e) {
            log = e.getMessage();
        }

        return log;
    }

    public void deleteTable(String tableName) {
        try {
            String deleteSql = "delete " + tableName;
            jdbcTemplate.execute(deleteSql);
        } catch (Exception e) {
            logger.error("RDB_MODERN:Error in deleting table:{}", e.getMessage());
        }
    }
}
