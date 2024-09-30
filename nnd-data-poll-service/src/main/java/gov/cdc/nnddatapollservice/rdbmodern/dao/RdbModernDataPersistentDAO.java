package gov.cdc.nnddatapollservice.rdbmodern.dao;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.constant.ConstantValue;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdbmodern.dto.NrtObservationDto;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservation;
import gov.cdc.nnddatapollservice.share.HandleError;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.LOG_SUCCESS;

@Service
@Slf4j
public class RdbModernDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(RdbModernDataPersistentDAO.class);
    private JdbcTemplate jdbcTemplate;

    private final NrtObservationRepository nrtObservationRepository;

    private final HandleError handleError;

    @Value("${datasync.sql_error_handle_log}")
    protected String sqlErrorPath = "";
    private final Gson gsonNorm = new Gson();
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
            .create();
    @Autowired
    public RdbModernDataPersistentDAO(@Qualifier("rdbmodernJdbcTemplate") JdbcTemplate jdbcTemplate,
                                      NrtObservationRepository nrtObservationRepository, HandleError handleError) {
        this.jdbcTemplate = jdbcTemplate;
        this.nrtObservationRepository = nrtObservationRepository;
        this.handleError = handleError;
    }

    protected void persistingNrtObs( List<NrtObservationDto> list, String tableName) throws DataPollException {
        for (NrtObservationDto data : list) {
            try {
                var domainModel = new NrtObservation(data);
                nrtObservationRepository.save(domainModel);
            } catch (Exception e) {
                logger.error("ERROR occured at record: {}", gsonNorm.toJson(data));
                handleError.writeRecordToFileTypedObject(gsonNorm, data, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + e.getClass().getSimpleName() + "/" + tableName + "/");
                throw new DataPollException("Tried individual process, but not success");
            }
        }
    }

    protected StringBuilder persistingGenericTable (StringBuilder logBuilder, String tableName, String jsonData) {
        try {
            SimpleJdbcInsert simpleJdbcInsert =
                    new SimpleJdbcInsert(jdbcTemplate);
            if (tableName != null && !tableName.isEmpty()) {
                deleteTable(tableName);//Delete first
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
                                logger.error("ERROR occured at record: {}", gsonNorm.toJson(record));
                                handleError.writeRecordToFile(gsonNorm, record, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + ei.getClass().getSimpleName() + "/" + tableName + "/");
                                throw new DataPollException("Tried individual process, but not success");
                            }
                        }
                    }


                } else {
                    logger.info("saveRdbModernData tableName: {} Records size:0", tableName);
                }
            }

        } catch (Exception e) {
            logger.error("Error executeBatch. class: {}, tableName: {}, Error:{}", e.getClass() ,tableName, e.getMessage());
            logBuilder = new StringBuilder(e.getMessage());
        }

        return logBuilder;
    }

    public String saveRdbModernData(String tableName, String jsonData) throws DataPollException {
        logger.info("saveRdbModernData tableName: {}", tableName);
        StringBuilder logBuilder = new StringBuilder(LOG_SUCCESS);
        if ("NRT_OBSERVATION".equalsIgnoreCase(tableName)) {
            logBuilder = new StringBuilder();
            Type resultType = new TypeToken<List<NrtObservationDto>>() {
            }.getType();
            List<NrtObservationDto> list = gson.fromJson(jsonData, resultType);
            persistingNrtObs(list, tableName);
        } else {
            logBuilder = persistingGenericTable (logBuilder, tableName, jsonData);
        }

        return logBuilder.toString();
    }


    public void deleteTable(String tableName) {
        try {
            String deleteSql = "delete FROM " + tableName;
            jdbcTemplate.execute(deleteSql);
        } catch (Exception e) {
            logger.error("RDB_MODERN:Error in deleting table:{}", e.getMessage());
        }
    }
}
