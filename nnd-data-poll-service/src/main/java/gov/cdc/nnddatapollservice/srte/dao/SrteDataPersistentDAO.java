package gov.cdc.nnddatapollservice.srte.dao;

import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;
import static gov.cdc.nnddatapollservice.share.StringUtil.getStackTraceAsString;

@Service
@Slf4j
public class SrteDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(SrteDataPersistentDAO.class);
    @Value("${datasync.data_sync_batch_limit}")
    protected Integer batchSize = 1000;
    private final JdbcTemplateUtil jdbcTemplateUtil;
    @Autowired
    public SrteDataPersistentDAO(
            JdbcTemplateUtil jdbcTemplateUtil
    ) {
        this.jdbcTemplateUtil = jdbcTemplateUtil;
    }

    protected LogResponseModel handlingSrteTable(PollDataSyncConfig pollConfig, String jsonData, boolean initialLoad) {
        LogResponseModel log = new LogResponseModel();
        log.setLog(LOG_SUCCESS);
        log.setStatus(SUCCESS);
        log = jdbcTemplateUtil.persistingGenericTable(jsonData, pollConfig, initialLoad);

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
}
