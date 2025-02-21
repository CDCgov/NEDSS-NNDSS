package gov.cdc.nnddatapollservice.rdb.dao;

import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.ERROR;
import static gov.cdc.nnddatapollservice.share.StringUtil.getStackTraceAsString;

@Service
@Slf4j
public class RdbDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(RdbDataPersistentDAO.class);

    private final JdbcTemplateUtil jdbcTemplateUtil;

    @Value("${datasync.data_sync_batch_limit}")
    protected Integer batchSize = 1000;

    @Autowired
    public RdbDataPersistentDAO(JdbcTemplateUtil jdbcTemplateUtil) {

        this.jdbcTemplateUtil = jdbcTemplateUtil;
    }

    @SuppressWarnings({"java:S3776", "java:S125"})
    public LogResponseModel saveRDBData(PollDataSyncConfig config, String jsonData, boolean initialLoad) {
        LogResponseModel logBuilder = new LogResponseModel();
        StringBuilder builder = new StringBuilder();

        try {
            jdbcTemplateUtil.persistingGenericTable(jsonData,config, initialLoad);
        }
        catch (Exception e) {
            logBuilder =  new LogResponseModel();
            logBuilder.setLog(e.getMessage());
            logBuilder.setStatus(ERROR);
            logBuilder.setStackTrace(getStackTraceAsString(e));
            logger.error("Error executeBatch. class: {}, tableName: {}, Error:{}", e.getClass() ,config.getTableName(), e.getMessage());
        }


        logBuilder.setLog(builder.toString());
        return logBuilder;
    }

}