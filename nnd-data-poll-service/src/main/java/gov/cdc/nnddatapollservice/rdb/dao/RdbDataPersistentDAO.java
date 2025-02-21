package gov.cdc.nnddatapollservice.rdb.dao;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.rdb.dto.Condition;
import gov.cdc.nnddatapollservice.rdb.dto.ConfirmationMethod;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.dto.RdbDate;
import gov.cdc.nnddatapollservice.repository.config.PollDataLogRepository;
import gov.cdc.nnddatapollservice.repository.config.model.PollDataLog;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.HandleError;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;
import static gov.cdc.nnddatapollservice.share.StringUtil.getStackTraceAsString;

@Service
@Slf4j
public class RdbDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(RdbDataPersistentDAO.class);
    private JdbcTemplate jdbcTemplate;
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private final HandleError handleError;
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
            .create();
    private final PollDataLogRepository pollDataLogRepository;
    private final JdbcTemplateUtil jdbcTemplateUtil;
    @Value("${datasync.sql_error_handle_log}")
    protected String sqlErrorPath = "";
    @Value("${datasync.data_sync_batch_limit}")
    protected Integer batchSize = 1000;
    private final Gson gsonNorm = new Gson();
    @Autowired
    public RdbDataPersistentDAO(@Qualifier("rdbJdbcTemplate") JdbcTemplate jdbcTemplate, HandleError handleError, PollDataLogRepository pollDataLogRepository, JdbcTemplateUtil jdbcTemplateUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.handleError = handleError;
        this.pollDataLogRepository = pollDataLogRepository;
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