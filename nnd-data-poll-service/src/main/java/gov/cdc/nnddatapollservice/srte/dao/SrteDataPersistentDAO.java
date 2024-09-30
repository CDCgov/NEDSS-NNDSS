package gov.cdc.nnddatapollservice.srte.dao;

import com.google.gson.Gson;
import gov.cdc.nnddatapollservice.constant.ConstantValue;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.share.HandleError;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.LOG_SUCCESS;

@Service
@Slf4j
public class SrteDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(SrteDataPersistentDAO.class);
    private JdbcTemplate jdbcTemplate;
    @Value("${datasync.sql_error_handle_log}")
    protected String sqlErrorPath = "";
    private final Gson gsonNorm = new Gson();
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
                simpleJdbcInsert = simpleJdbcInsert.withTableName(tableName);
                List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
                if (records != null && !records.isEmpty()) {
                    logger.info("Inside generic code before executeBatch tableName: {} Records size:{}", tableName, records.size());

                    try {
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
                    } catch (Exception e) {
                        for (Map<String, Object> record : records) {
                            try {
                                simpleJdbcInsert.execute(new MapSqlParameterSource(record));
                            } catch (Exception ei) {
                                // TODO: LOG THESE
                                logger.error("ERROR occured at record: {}", gsonNorm.toJson(record));
                                HandleError.writeRecordToFile(gsonNorm, record, tableName + UUID.randomUUID(), sqlErrorPath + "/SRTE/" + ei.getClass().getSimpleName() + "/" + tableName + "/");
                                throw new DataPollException("Tried individual process, but not success");
                            }
                        }
                    }

                } else {
                    logger.info("saveSRTEData tableName: {} Records size:0", tableName);
                }
            }
        } catch (Exception e) {
            log = e.getMessage();
            logger.error("Error executeBatch. class: {}, tableName: {}, Error:{}", e.getClass() ,tableName, e.getMessage());

        }

        return log;
    }

    public void deleteTable(String tableName) {
        try {
            String deleteSql = "delete FROM " + tableName;
            jdbcTemplate.execute(deleteSql);
        } catch (Exception e) {
            logger.error("SRTE:Error in deleting table:{}", e.getMessage());
        }
    }
}
