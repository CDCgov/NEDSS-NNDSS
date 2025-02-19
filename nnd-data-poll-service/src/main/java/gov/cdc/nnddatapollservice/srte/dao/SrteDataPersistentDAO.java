package gov.cdc.nnddatapollservice.srte.dao;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.repository.srte.CodeToConditionRepository;
import gov.cdc.nnddatapollservice.repository.srte.model.CodeToCondition;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.HandleError;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import gov.cdc.nnddatapollservice.srte.dto.CodeToConditionDto;
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

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;
import static gov.cdc.nnddatapollservice.share.StringUtil.getStackTraceAsString;

@Service
@Slf4j
public class SrteDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(SrteDataPersistentDAO.class);
    private JdbcTemplate jdbcTemplate;
    @Value("${datasync.sql_error_handle_log}")
    protected String sqlErrorPath = "";
    @Value("${datasync.data_sync_batch_limit}")
    protected Integer batchSize = 1000;
    private final JdbcTemplateUtil jdbcTemplateUtil;
    private final HandleError handleError;
    private final CodeToConditionRepository codeToConditionRepository;
    private final Gson gsonNorm = new Gson();
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
            .create();
    @Autowired
    public SrteDataPersistentDAO(@Qualifier("rdbJdbcTemplate") JdbcTemplate jdbcTemplate, JdbcTemplateUtil jdbcTemplateUtil, HandleError handleError, CodeToConditionRepository codeToConditionRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateUtil = jdbcTemplateUtil;
        this.handleError = handleError;
        this.codeToConditionRepository = codeToConditionRepository;
    }

    protected LogResponseModel handlingSrteTable(PollDataSyncConfig pollConfig, String jsonData, boolean initialLoad) {
        LogResponseModel log = new LogResponseModel();
        log.setLog(LOG_SUCCESS);
        log.setStatus(SUCCESS);
//        if ("CODE_TO_CONDITION".equalsIgnoreCase(pollConfig.getTableName())) {
//            Type resultType = new TypeToken<List<CodeToConditionDto>>() {
//            }.getType();
//            List<CodeToConditionDto> list = gson.fromJson(jsonData, resultType);
//            persistingCodeToCondition(list, pollConfig.getTableName());
//        } else {
            log = persistingGenericTable(pollConfig, jsonData, initialLoad);
//        }

        return log;
    }

    protected void persistingCodeToCondition(List<CodeToConditionDto> list, String tableName) {
        for (CodeToConditionDto data : list) {
            try {
                var domainModel = new CodeToCondition(data);
                codeToConditionRepository.save(domainModel);
            } catch (Exception e) {
                logger.error("ERROR occured at record: {}, {}", gsonNorm.toJson(data), e.getMessage()); // NOSONAR
                handleError.writeRecordToFileTypedObject(gsonNorm, data, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + e.getClass().getSimpleName() + "/" + tableName + "/"); // NOSONAR
            }
        }
    }


    @SuppressWarnings("java:S3776")
    protected LogResponseModel persistingGenericTable (PollDataSyncConfig pollConfig, String jsonData, boolean initialLoad) {
        LogResponseModel log = new LogResponseModel();
        log.setLog(LOG_SUCCESS);
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert = jdbcInsert.withTableName(pollConfig.getTableName());
        List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
        if (records != null && !records.isEmpty()) {
            try {
                if (records.size() > batchSize) {
                    int sublistSize = batchSize;
                    for (int i = 0; i < records.size(); i += sublistSize) {
                        int end = Math.min(i + sublistSize, records.size());
                        List<Map<String, Object>> sublist = records.subList(i, end);
//                        jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(sublist));
                        if (initialLoad || pollConfig.getKeyList().isEmpty()) {
                            sublist.forEach(data -> data.remove("RowNum"));
                            jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(sublist));

                        } else {
                            jdbcTemplateUtil.upsertBatch(pollConfig.getTableName(), sublist, pollConfig.getKeyList());
                        }
                    }
                } else {
                    jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
                }
            } catch (Exception e) {
                for (Map<String, Object> res : records) {
                    try {
//                        jdbcInsert.execute(new MapSqlParameterSource(res));
                        if (initialLoad || pollConfig.getKeyList().isEmpty()) {
                            records.forEach(data -> data.remove("RowNum"));
                            jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));

                        } else {
                            jdbcTemplateUtil.upsertBatch(pollConfig.getTableName(), records, pollConfig.getKeyList());
                        }
                    } catch (Exception ei) {
                        logger.error("ERROR occured at record: {}, {}", gsonNorm.toJson(res), ei.getMessage()); // NOSONAR
                        handleError.writeRecordToFile(gsonNorm, res, pollConfig.getTableName() + UUID.randomUUID(), sqlErrorPath + "/SRTE/" + ei.getClass().getSimpleName() + "/" + pollConfig.getTableName() + "/");
                    }
                }
            }

        } else {
            log.setLog("No Data");
            logger.info("saveSRTEData tableName: {} Records size:0", pollConfig.getTableName());
        }
        return log;
    }
    public LogResponseModel saveSRTEData(PollDataSyncConfig pollConfig, String jsonData, boolean initialLoad) {
        logger.info("saveSRTEData tableName: {}", pollConfig.getTableName());
        LogResponseModel log = new LogResponseModel();
        log.setLog(LOG_SUCCESS);
        log.setStatus(SUCCESS);
        try {
            if (pollConfig.getTableName() != null && !pollConfig.getTableName().isEmpty()) {
              log = handlingSrteTable ( pollConfig,  jsonData, initialLoad);
            }
        } catch (Exception e) {
            log.setStatus(ERROR);
            log.setLog(e.getMessage());
            log.setStackTrace(getStackTraceAsString(e));
            logger.error("Error executeBatch. class: {}, tableName: {}, Error:{}", e.getClass() ,pollConfig.getTableName(), e.getMessage());

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
