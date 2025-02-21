package gov.cdc.nnddatapollservice.edx_nbs_odse.dao;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.configuration.TimestampAdapter;
import gov.cdc.nnddatapollservice.edx_nbs_odse.dto.EDXActivityDetailLogDto;
import gov.cdc.nnddatapollservice.edx_nbs_odse.dto.EDXActivityLogDto;
import gov.cdc.nnddatapollservice.repository.nbs_odse.EDXActivityDetailLogRepository;
import gov.cdc.nnddatapollservice.repository.nbs_odse.EDXActivityLogRepository;
import gov.cdc.nnddatapollservice.repository.nbs_odse.model.EDXActivityDetailLog;
import gov.cdc.nnddatapollservice.repository.nbs_odse.model.EDXActivityLog;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.HandleError;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.LOG_SUCCESS;
import static gov.cdc.nnddatapollservice.constant.ConstantValue.SUCCESS;

@Service
public class EdxNbsOdseDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(EdxNbsOdseDataPersistentDAO.class);

    private JdbcTemplate jdbcTemplate;
    private final HandleError handleError;

    private final EDXActivityLogRepository edxActivityLogRepository;
    private final EDXActivityDetailLogRepository edxActivityDetailLogRepository;
    private final JdbcTemplateUtil jdbcTemplateUtil;

    @Value("${datasync.sql_error_handle_log}")
    protected String sqlErrorPath = "";

    private final Gson gsonNorm = new Gson();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampSerializer())
            .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampDeserializer())
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
            .create();

    public EdxNbsOdseDataPersistentDAO(@Qualifier("rdbJdbcTemplate") JdbcTemplate jdbcTemplate, HandleError handleError, EDXActivityLogRepository edxActivityLogRepository, EDXActivityDetailLogRepository edxActivityDetailLogRepository, JdbcTemplateUtil jdbcTemplateUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.handleError = handleError;
        this.edxActivityLogRepository = edxActivityLogRepository;
        this.edxActivityDetailLogRepository = edxActivityDetailLogRepository;
        this.jdbcTemplateUtil = jdbcTemplateUtil;
    }

    public LogResponseModel saveNbsOdseData(String tableName, String jsonData) {
        LogResponseModel logBuilder = new LogResponseModel();
        if ("EDX_ACTIVITY_LOG".equalsIgnoreCase(tableName)) {
            logBuilder.setLog(LOG_SUCCESS);
            logBuilder.setStatus(SUCCESS);
            Type resultType = new TypeToken<List<EDXActivityLogDto>>() {
            }.getType();
            List<EDXActivityLogDto> list = gson.fromJson(jsonData, resultType);
            persistingEdxActivity(list, tableName);
        }
        else if ("EDX_ACTIVITY_DETAIL_LOG".equalsIgnoreCase(tableName)) {
            logBuilder.setLog(LOG_SUCCESS);
            logBuilder.setStatus(SUCCESS);
            Type resultType = new TypeToken<List<EDXActivityDetailLogDto>>() {
            }.getType();
            List<EDXActivityDetailLogDto> list = gson.fromJson(jsonData, resultType);
            persistingEdxActivityDetail(list, tableName);
        }

        return logBuilder;

    }



    protected void persistingEdxActivity(List<EDXActivityLogDto> list, String tableName) {
        for (EDXActivityLogDto data : list) {
            try {
                jdbcTemplate.execute("SET IDENTITY_INSERT " + tableName + " ON");
                var domainModel = new EDXActivityLog(data);
                edxActivityLogRepository.save(domainModel);
            } catch (Exception e) {
                logger.error("ERROR occured at record: {}, {}", gsonNorm.toJson(data), e.getMessage()); // NOSONAR
                handleError.writeRecordToFileTypedObject(gsonNorm, data, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + e.getClass().getSimpleName() + "/" + tableName + "/"); // NOSONAR
            }
        }
        jdbcTemplate.execute("SET IDENTITY_INSERT " + tableName + " OFF");

    }

    protected void persistingEdxActivityDetail(List<EDXActivityDetailLogDto> list, String tableName) {
        for (EDXActivityDetailLogDto data : list) {
            try {
                jdbcTemplate.execute("SET IDENTITY_INSERT " + tableName + " ON");
                var domainModel = new EDXActivityDetailLog(data);
                edxActivityDetailLogRepository.save(domainModel);
            } catch (Exception e) {
                logger.error("ERROR occured at record: {}, {}", gsonNorm.toJson(data), e.getMessage()); // NOSONAR
                handleError.writeRecordToFileTypedObject(gsonNorm, data, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + e.getClass().getSimpleName() + "/" + tableName + "/"); // NOSONAR
            }
            jdbcTemplate.execute("SET IDENTITY_INSERT " + tableName + " OFF");
        }
    }

    public void deleteTable(String tableName) {
        jdbcTemplateUtil.deleteTable(tableName);
    }

}
