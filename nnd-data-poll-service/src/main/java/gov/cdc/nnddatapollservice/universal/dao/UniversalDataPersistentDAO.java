package gov.cdc.nnddatapollservice.universal.dao;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.universal.dto.NrtObservationCodedDto;
import gov.cdc.nnddatapollservice.universal.dto.NrtObservationDto;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationCodedRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservation;
import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservationCoded;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;
import static gov.cdc.nnddatapollservice.share.StringUtil.getStackTraceAsString;

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
        LogResponseModel logBuilder = null;
        logBuilder = jdbcTemplateUtil.persistingGenericTable ( jsonData,config, initialLoad);


        return logBuilder;
    }
}
