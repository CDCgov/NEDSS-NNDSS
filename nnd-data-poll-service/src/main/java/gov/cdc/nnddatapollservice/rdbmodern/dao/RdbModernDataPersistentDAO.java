package gov.cdc.nnddatapollservice.rdbmodern.dao;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.rdbmodern.dto.NrtObservationCodedDto;
import gov.cdc.nnddatapollservice.rdbmodern.dto.NrtObservationDto;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationCodedRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservation;
import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservationCoded;
import gov.cdc.nnddatapollservice.share.HandleError;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
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
    private final NrtObservationCodedRepository nrtObservationCodedRepository;

    private final JdbcTemplateUtil jdbcTemplateUtil;

    private final HandleError handleError;

    @Value("${datasync.sql_error_handle_log}")
    protected String sqlErrorPath = "";

    @Value("${datasync.data_sync_batch_limit}")
    protected Integer batchSize = 1000;
    private final Gson gsonNorm = new Gson();
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
            .create();
    @Autowired
    public RdbModernDataPersistentDAO(@Qualifier("rdbJdbcTemplate") JdbcTemplate jdbcTemplate,
                                      NrtObservationRepository nrtObservationRepository, NrtObservationCodedRepository nrtObservationCodedRepository, JdbcTemplateUtil jdbcTemplateUtil, HandleError handleError) {
        this.jdbcTemplate = jdbcTemplate;
        this.nrtObservationRepository = nrtObservationRepository;
        this.nrtObservationCodedRepository = nrtObservationCodedRepository;
        this.jdbcTemplateUtil = jdbcTemplateUtil;
        this.handleError = handleError;
    }

    @SuppressWarnings("java:S2139")
    protected void persistingNrtObsCoded(List<NrtObservationCodedDto> list, String tableName) {
        for (NrtObservationCodedDto data : list) {
            try {
                var domainModel = new NrtObservationCoded(data);
                nrtObservationCodedRepository.save(domainModel);
            } catch (Exception e) {
                logger.error("ERROR occured at record: {}, {}", gsonNorm.toJson(data), e.getMessage()); // NOSONAR
                handleError.writeRecordToFileTypedObject(gsonNorm, data, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + e.getClass().getSimpleName() + "/" + tableName + "/"); // NOSONAR
            }
        }
    }

    @SuppressWarnings("java:S2139")
    protected void persistingNrtObs( List<NrtObservationDto> list, String tableName) {
        for (NrtObservationDto data : list) {
            try {
                var domainModel = new NrtObservation(data);
                nrtObservationRepository.save(domainModel);
            } catch (Exception e) {
                logger.error("ERROR occured at record: {}, {}", gsonNorm.toJson(data), e.getMessage()); // NOSONAR
                handleError.writeRecordToFileTypedObject(gsonNorm, data, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + e.getClass().getSimpleName() + "/" + tableName + "/"); // NOSONAR
            }
        }
    }

    @SuppressWarnings({"java:S3776","java:S1141"})
    protected StringBuilder persistingGenericTable (StringBuilder logBuilder,
                                                    String tableName,
                                                    String jsonData,
                                                    String keyList,
                                                    boolean initialLoad) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            if (tableName != null && !tableName.isEmpty()) {
                jdbcInsert = jdbcInsert.withTableName(tableName);
                List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
                if (records != null && !records.isEmpty()) {
                    logger.info("Inside generic code before executeBatch tableName: {} Records size:{}", tableName, records.size());

                    try {
                        if (records.size() >  batchSize) {
                            int sublistSize = batchSize;
                            for (int i = 0; i < records.size(); i += sublistSize) {
                                int end = Math.min(i + sublistSize, records.size());
                                List<Map<String, Object>> sublist = records.subList(i, end);
                                if (initialLoad) {
                                    sublist.forEach(data -> data.remove("RowNum"));
                                    jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(sublist));

                                } else {
                                    jdbcTemplateUtil.upsertBatch(tableName, sublist, keyList);
                                }
                            }
                        } else {
                            if (initialLoad) {
                                records.forEach(data -> data.remove("RowNum"));
                                jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));

                            } else {
                                jdbcTemplateUtil.upsertBatch(tableName, records, keyList);
                            }
                        }
                    } catch (Exception e) {
                        for (Map<String, Object> res : records) {
                            try {
                                jdbcInsert.execute(new MapSqlParameterSource(res));
                            } catch (Exception ei) {
                                if (ei instanceof DuplicateKeyException) // NOSONAR
                                {
                                    logger.debug("Duplicated Key Exception Resolved");
                                } else {
                                    logger.error("ERROR occured at record: {}, {}", gsonNorm.toJson(res), e.getMessage()); // NOSONAR
                                    handleError.writeRecordToFile(gsonNorm, res, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + ei.getClass().getSimpleName() + "/" + tableName + "/"); // NOSONAR
                                }

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



    public String saveRdbModernData(String tableName, String jsonData, String keyList, boolean initialLoad) {
        logger.info("saveRdbModernData tableName: {}", tableName);
        StringBuilder logBuilder = new StringBuilder(LOG_SUCCESS);



        if ("NRT_OBSERVATION".equalsIgnoreCase(tableName)) {
            logBuilder = new StringBuilder(LOG_SUCCESS);
            Type resultType = new TypeToken<List<NrtObservationDto>>() {
            }.getType();
            List<NrtObservationDto> list = gson.fromJson(jsonData, resultType);
            persistingNrtObs(list, tableName);
        }
        else if ("nrt_observation_coded".equalsIgnoreCase(tableName)) {
            logBuilder = new StringBuilder(LOG_SUCCESS);
            Type resultType = new TypeToken<List<NrtObservationCodedDto>>() {
            }.getType();
            List<NrtObservationCodedDto> list = gson.fromJson(jsonData, resultType);
            persistingNrtObsCoded(list, tableName);
        }
        else {
            logBuilder = persistingGenericTable (logBuilder, tableName, jsonData,
                    keyList, initialLoad);
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
