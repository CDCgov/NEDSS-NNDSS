package gov.cdc.nnddatapollservice.universal.dao;

import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class UniversalDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(UniversalDataPersistentDAO.class);

    private final JdbcTemplateUtil jdbcTemplateUtil;


    @Value("${thread.jdbc-batch-level.enabled}")
    protected boolean jdbcBatchLevelEnabled = true;

    @Autowired
    public UniversalDataPersistentDAO(JdbcTemplateUtil jdbcTemplateUtil) {
        this.jdbcTemplateUtil = jdbcTemplateUtil;
    }

    public LogResponseModel saveUniversalData(PollDataSyncConfig config, String jsonData , boolean initialLoad, Timestamp timestamp, ApiResponseModel<?> apiResponseModel) {
        logger.info("saveUniversalData tableName: {}", config.getTableName());
        LogResponseModel logBuilder;
        if (jdbcBatchLevelEnabled) {
            logBuilder = jdbcTemplateUtil.persistingGenericTableMultiThread ( jsonData,config, initialLoad, timestamp, apiResponseModel);
        }
        else {
            logBuilder = jdbcTemplateUtil.persistingGenericTable ( jsonData,config, initialLoad, timestamp, apiResponseModel);
        }


        return logBuilder;
    }
}
