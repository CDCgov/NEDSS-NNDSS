package gov.cdc.nnddatapollservice.nbs_odse.dao;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.nbs_odse.dto.EDXActivityDetailLogDto;
import gov.cdc.nnddatapollservice.nbs_odse.dto.EDXActivityLogDto;
import gov.cdc.nnddatapollservice.rdbmodern.dto.NrtObservationCodedDto;
import gov.cdc.nnddatapollservice.rdbmodern.dto.NrtObservationDto;
import gov.cdc.nnddatapollservice.repository.nbs_odse.EDXActivityDetailLogRepository;
import gov.cdc.nnddatapollservice.repository.nbs_odse.EDXActivityLogRepository;
import gov.cdc.nnddatapollservice.repository.nbs_odse.model.EDXActivityDetailLog;
import gov.cdc.nnddatapollservice.repository.nbs_odse.model.EDXActivityLog;
import gov.cdc.nnddatapollservice.share.HandleError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.LOG_SUCCESS;

@Service
public class NbsOdseDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(NbsOdseDataPersistentDAO.class);

    private JdbcTemplate jdbcTemplate;
    private final HandleError handleError;

    private final EDXActivityLogRepository edxActivityLogRepository;
    private final EDXActivityDetailLogRepository edxActivityDetailLogRepository;

    @Value("${datasync.sql_error_handle_log}")
    protected String sqlErrorPath = "";

    @Value("${datasync.data_sync_batch_limit}")
    protected Integer batchSize = 1000;
    private final Gson gsonNorm = new Gson();
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
            .create();

    public NbsOdseDataPersistentDAO(@Qualifier("nbsOdseJdbcTemplate") JdbcTemplate jdbcTemplate, HandleError handleError, EDXActivityLogRepository edxActivityLogRepository, EDXActivityDetailLogRepository edxActivityDetailLogRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.handleError = handleError;
        this.edxActivityLogRepository = edxActivityLogRepository;
        this.edxActivityDetailLogRepository = edxActivityDetailLogRepository;
    }

    public String saveNbsOdseData(String tableName, String jsonData) {
        logger.info("saveNbsOdseData tableName: {}", tableName);
        StringBuilder logBuilder = new StringBuilder(LOG_SUCCESS);
        if ("EDX_ACTIVITY_LOG".equalsIgnoreCase(tableName)) {
            logBuilder = new StringBuilder(LOG_SUCCESS);
            Type resultType = new TypeToken<List<EDXActivityLogDto>>() {
            }.getType();
            List<EDXActivityLogDto> list = gson.fromJson(jsonData, resultType);
            persistingEdxActivity(list, tableName);
        }
        else if ("EDX_ACTIVITY_DETAIL_LOG".equalsIgnoreCase(tableName)) {
            logBuilder = new StringBuilder(LOG_SUCCESS);
            Type resultType = new TypeToken<List<EDXActivityDetailLogDto>>() {
            }.getType();
            List<EDXActivityDetailLogDto> list = gson.fromJson(jsonData, resultType);
            persistingEdxActivityDetail(list, tableName);
        }

        return logBuilder.toString();
    }


    protected void persistingEdxActivity(List<EDXActivityLogDto> list, String tableName) {
        for (EDXActivityLogDto data : list) {
            try {
                var domainModel = new EDXActivityLog(data);
                edxActivityLogRepository.save(domainModel);
            } catch (Exception e) {
                logger.error("ERROR occured at record: {}, {}", gsonNorm.toJson(data), e.getMessage()); // NOSONAR
                handleError.writeRecordToFileTypedObject(gsonNorm, data, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + e.getClass().getSimpleName() + "/" + tableName + "/"); // NOSONAR
            }
        }
    }

    protected void persistingEdxActivityDetail(List<EDXActivityDetailLogDto> list, String tableName) {
        for (EDXActivityDetailLogDto data : list) {
            try {
                var domainModel = new EDXActivityDetailLog(data);
                edxActivityDetailLogRepository.save(domainModel);
            } catch (Exception e) {
                logger.error("ERROR occured at record: {}, {}", gsonNorm.toJson(data), e.getMessage()); // NOSONAR
                handleError.writeRecordToFileTypedObject(gsonNorm, data, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + e.getClass().getSimpleName() + "/" + tableName + "/"); // NOSONAR
            }
        }
    }

}
