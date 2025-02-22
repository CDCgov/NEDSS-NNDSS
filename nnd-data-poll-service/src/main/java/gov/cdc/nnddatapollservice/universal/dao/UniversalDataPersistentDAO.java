package gov.cdc.nnddatapollservice.universal.dao;

import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UniversalDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(UniversalDataPersistentDAO.class);

    private final JdbcTemplateUtil jdbcTemplateUtil;

    @Value("${datasync.data_sync_batch_limit}")
    protected Integer batchSize = 1000;

    @Autowired
    public UniversalDataPersistentDAO(JdbcTemplateUtil jdbcTemplateUtil) {
        this.jdbcTemplateUtil = jdbcTemplateUtil;
    }

    public LogResponseModel saveRdbModernData(PollDataSyncConfig config, String jsonData , boolean initialLoad) {
        logger.info("saveRdbModernData tableName: {}", config.getTableName());
        LogResponseModel logBuilder;
        logBuilder = jdbcTemplateUtil.persistingGenericTable ( jsonData,config, initialLoad);


        return logBuilder;
    }
}
