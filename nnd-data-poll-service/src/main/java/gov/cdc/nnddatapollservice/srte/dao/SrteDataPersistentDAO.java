package gov.cdc.nnddatapollservice.srte.dao;

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
public class SrteDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(SrteDataPersistentDAO.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SrteDataPersistentDAO(@Qualifier("srteJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String saveSRTEData(String tableName, String jsonData) {
        logger.info("saveSRTEData tableName: {}", tableName);
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
                    simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
                    logger.info("executeBatch completed. tableName: {}", tableName);
                } else {
                    logger.info("Inside generic code tableName: {} Records size:0", tableName);
                }
            }

        } catch (Exception e) {
            log = e.getMessage();
        }

        return log;
    }

    public void deleteTable(String tableName) {
        try{
            String deleteSql = "delete " + tableName;
            jdbcTemplate.execute(deleteSql);
        }catch (Exception e){
            logger.error("SRTE:Error in deleting table:{}",e.getMessage());
        }
    }
}
