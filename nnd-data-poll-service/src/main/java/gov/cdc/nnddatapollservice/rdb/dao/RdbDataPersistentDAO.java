package gov.cdc.nnddatapollservice.rdb.dao;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.Condition;
import gov.cdc.nnddatapollservice.rdb.dto.ConfirmationMethod;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.dto.RdbDate;
import gov.cdc.nnddatapollservice.share.HandleError;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.LOG_SUCCESS;

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

    @Value("${datasync.sql_error_handle_log}")
    protected String sqlErrorPath = "";
    @Value("${datasync.data_sync_batch_limit}")
    protected Integer batchSize = 1000;
    private final Gson gsonNorm = new Gson();
    @Autowired
    public RdbDataPersistentDAO(@Qualifier("rdbJdbcTemplate") JdbcTemplate jdbcTemplate, HandleError handleError) {
        this.jdbcTemplate = jdbcTemplate;
        this.handleError = handleError;
    }

    @SuppressWarnings("java:S3776")
    protected void persistingGenericTable (String tableName, String jsonData) throws DataPollException {
        if (tableName != null && !tableName.isEmpty()) {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert = simpleJdbcInsert.withTableName(tableName);
            List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
            if (records != null && !records.isEmpty()) {
                logger.info("Inside generic code before executeBatch tableName: {} Records size:{}", tableName, records.size());
                try {
                    if (records.size() > batchSize) {
                        int sublistSize = batchSize;
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
                            handleError.writeRecordToFile(gsonNorm, record, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB/" + ei.getClass().getSimpleName() + "/" + tableName + "/");
                            throw new DataPollException("Tried individual process, but not success");
                        }
                    }
                }

            } else {
                logger.info("Inside generic code tableName: {} Records size:0", tableName);
            }
        }
    }

    @SuppressWarnings("java:S3776")
    public String saveRDBData(String tableName, String jsonData) {
        logger.info("saveRDBData tableName: {}", tableName);
        StringBuilder logBuilder = new StringBuilder(LOG_SUCCESS);
        if ("CONFIRMATION_METHOD".equalsIgnoreCase(tableName)) {
            logBuilder = new StringBuilder(LOG_SUCCESS);
            Type resultType = new TypeToken<List<ConfirmationMethod>>() {
            }.getType();
            List<ConfirmationMethod> list = gson.fromJson(jsonData, resultType);
            for (ConfirmationMethod confirmationMethod : list) {
                logBuilder.append(", ").append(upsertConfirmationMethod(confirmationMethod));
            }
        } else if ("CONDITION".equalsIgnoreCase(tableName)) {
            logBuilder = new StringBuilder(LOG_SUCCESS);
            Type resultType = new TypeToken<List<Condition>>() {
            }.getType();
            List<Condition> list = gson.fromJson(jsonData, resultType);
            for (Condition condition : list) {
                logBuilder.append(", ").append(upsertCondition(condition));
            }
        } else if ("RDB_DATE".equalsIgnoreCase(tableName)) {
            logBuilder = new StringBuilder(LOG_SUCCESS);
            Type resultType = new TypeToken<List<RdbDate>>() {
            }.getType();
            List<RdbDate> list = gson.fromJson(jsonData, resultType);
            for (RdbDate rdbDate : list) {
                logBuilder.append(", ").append(upsertRdbDate(rdbDate));
            }
        } else {
            try {
                persistingGenericTable (tableName, jsonData);
            }
            catch (Exception e) {
                logBuilder = new StringBuilder(e.getMessage());
                logger.error("Error executeBatch. class: {}, tableName: {}, Error:{}", e.getClass() ,tableName, e.getMessage());
            }
        }
        return logBuilder.toString();
    }

    private String upsertConfirmationMethod(ConfirmationMethod confirmationMethod) {
        String log = LOG_SUCCESS;
        String sql = "MERGE INTO CONFIRMATION_METHOD AS target USING " +
                "(select " + confirmationMethod.getConfirmationMethodKey() + " as id) AS source " +
                "ON (target.CONFIRMATION_METHOD_KEY = source.id) " +
                "WHEN MATCHED THEN " +
                " UPDATE SET target.CONFIRMATION_METHOD_CD = " + getSqlString(confirmationMethod.getConfirmationMethodCd()) +
                ",target.CONFIRMATION_METHOD_DESC = " + getSqlString(confirmationMethod.getConfirmationMethodDesc()) +
                " WHEN NOT MATCHED THEN " +
                " INSERT (CONFIRMATION_METHOD_KEY, CONFIRMATION_METHOD_CD,CONFIRMATION_METHOD_DESC) " +
                "VALUES (" + confirmationMethod.getConfirmationMethodKey() +
                "," + getSqlString(confirmationMethod.getConfirmationMethodCd()) +
                "," + getSqlString(confirmationMethod.getConfirmationMethodDesc()) + ");";
        try {
            jdbcTemplate.update(sql);
        } catch (Exception e) {
            logger.error("Error in upsert for CONFIRMATION_METHOD table:{}", e.getMessage());
            log = e.getMessage();
        }
        return log;
    }

    protected String upsertCondition(Condition condition) {
        String sql = "MERGE INTO CONDITION AS target " +
                "USING (select " + condition.getConditionKey() + " as id) AS source ON" +
                "(target.CONDITION_KEY = source.id)" +
                "WHEN MATCHED THEN " +
                "UPDATE " +
                "SET " +
                " target.CONDITION_CD = " + getSqlString(condition.getConditionCd()) + "," +
                " target.CONDITION_DESC = " + getSqlString(condition.getConditionDesc()) + "," +
                " target.CONDITION_SHORT_NM = " + getSqlString(condition.getConditionShortNm()) + "," +
                " target.CONDITION_CD_EFF_DT = " + getSqlString(condition.getConditionCdEffDt()) + "," +
                " target.CONDITION_CD_END_DT = " + getSqlString(condition.getConditionCdEndDt()) + "," +
                " target.NND_IND = " + getSqlString(condition.getNndInd()) + "," +
                " target.DISEASE_GRP_CD = " + getSqlString(condition.getDiseaseGrpCd()) + "," +
                " target.DISEASE_GRP_DESC = " + getSqlString(condition.getDiseaseGrpDesc()) + "," +
                " target.PROGRAM_AREA_CD = " + getSqlString(condition.getProgramAreaCd()) + "," +
                " target.PROGRAM_AREA_DESC = " + getSqlString(condition.getProgramAreaDesc()) + "," +
                " target.CONDITION_CD_SYS_CD_NM = " + getSqlString(condition.getConditionCdSysCdNm()) + "," +
                " target.ASSIGNING_AUTHORITY_CD = " + getSqlString(condition.getAssigningAuthorityCd()) + "," +
                " target.ASSIGNING_AUTHORITY_DESC = " + getSqlString(condition.getAssigningAuthorityDesc()) + "," +
                " target.CONDITION_CD_SYS_CD = " + getSqlString(condition.getConditionCdSysCd()) +
                " WHEN NOT MATCHED THEN " +
                "INSERT " +
                " (CONDITION_KEY," +
                " CONDITION_CD," +
                " CONDITION_DESC,CONDITION_SHORT_NM,CONDITION_CD_EFF_DT,CONDITION_CD_END_DT,NND_IND," +
                " DISEASE_GRP_CD,DISEASE_GRP_DESC,PROGRAM_AREA_CD,PROGRAM_AREA_DESC," +
                " CONDITION_CD_SYS_CD_NM,ASSIGNING_AUTHORITY_CD,ASSIGNING_AUTHORITY_DESC,CONDITION_CD_SYS_CD)" +
                "VALUES ("
                + condition.getConditionKey() + "," +
                getSqlString(condition.getConditionCd()) + "," +
                getSqlString(condition.getConditionDesc()) + "," +
                getSqlString(condition.getConditionShortNm()) + "," +
                getSqlString(condition.getConditionCdEffDt()) + "," +
                getSqlString(condition.getConditionCdEndDt()) + "," +
                getSqlString(condition.getNndInd()) + "," +
                getSqlString(condition.getDiseaseGrpCd()) + "," +
                getSqlString(condition.getDiseaseGrpDesc()) + "," +
                getSqlString(condition.getProgramAreaCd()) + "," +
                getSqlString(condition.getProgramAreaDesc()) + "," +
                getSqlString(condition.getConditionCdSysCdNm()) + "," +
                getSqlString(condition.getAssigningAuthorityCd()) + "," +
                getSqlString(condition.getAssigningAuthorityDesc()) + "," +
                getSqlString(condition.getConditionCdSysCd()) +
                ");";
        String log = LOG_SUCCESS;
        try {
            jdbcTemplate.update(sql);
        } catch (Exception e) {
            logger.error("Error in upsert for CONDITION table:{}", e.getMessage());
            log = e.getMessage();
        }
        return log;
    }

    protected String upsertRdbDate(RdbDate rdbDate) {
        String sql = "MERGE INTO RDB_DATE AS target " +
                "USING (select " + rdbDate.getDateKey() + " as id) AS source ON" +
                "(target.DATE_KEY = source.id)" +
                "WHEN MATCHED THEN " +
                "UPDATE " +
                "SET " +
                " target.DATE_MM_DD_YYYY = " + getSqlString(rdbDate.getDateMmDdYyyy()) + "," +
                " target.DAY_OF_WEEK = " + getSqlString(rdbDate.getDayOfWeek()) + "," +
                " target.DAY_NBR_IN_CLNDR_MON = " + rdbDate.getDayNbrInClndrMon() + "," +
                " target.DAY_NBR_IN_CLNDR_YR = " + rdbDate.getDayNbrInClndrYr() + "," +
                " target.WK_NBR_IN_CLNDR_MON = " + rdbDate.getWkNbrInClndrMon() + "," +
                " target.WK_NBR_IN_CLNDR_YR = " + rdbDate.getWkNbrInClndrYr() + "," +
                " target.CLNDR_MON_NAME = " + getSqlString(rdbDate.getClndrMonName()) + "," +
                " target.CLNDR_MON_IN_YR = " + rdbDate.getClndrMonInYr() + "," +
                " target.CLNDR_QRTR = " + rdbDate.getClndrQrtr() + "," +
                " target.CLNDR_YR = " + rdbDate.getClndrYr() +
                " WHEN NOT MATCHED THEN " +
                "INSERT " +
                " (DATE_KEY," +
                " DATE_MM_DD_YYYY," +
                " DAY_OF_WEEK,DAY_NBR_IN_CLNDR_MON,DAY_NBR_IN_CLNDR_YR,WK_NBR_IN_CLNDR_MON,WK_NBR_IN_CLNDR_YR," +
                " CLNDR_MON_NAME,CLNDR_MON_IN_YR,CLNDR_QRTR,CLNDR_YR)" +
                " VALUES ("
                + rdbDate.getDateKey() + "," +
                getSqlString(rdbDate.getDateMmDdYyyy()) + "," +
                getSqlString(rdbDate.getDayOfWeek()) + "," +
                rdbDate.getDayNbrInClndrMon() + "," +
                rdbDate.getDayNbrInClndrYr() + "," +
                rdbDate.getWkNbrInClndrMon() + "," +
                rdbDate.getWkNbrInClndrYr() + "," +
                getSqlString(rdbDate.getClndrMonName()) + "," +
                rdbDate.getClndrMonInYr() + "," +
                rdbDate.getClndrQrtr() + "," +
                rdbDate.getClndrYr() +
                ");";

        String log = LOG_SUCCESS;
        try {
            jdbcTemplate.update(sql);
        } catch (Exception e) {
            logger.error("Error in upsert for RDB_DATE table:{}", e.getMessage());
            log = e.getMessage();
        }
        return log;
    }

    private static String getSqlString(String val) {
        if (val != null && !val.isEmpty()) {
            return "'" + val + "'";
        }
        return val;
    }

    public String getLastUpdatedTime(String tableName) {
        String sql = "select last_update_time from POLL_DATA_SYNC_CONFIG where table_name=?";
        String updatedTime = "";
        Timestamp lastTime = this.jdbcTemplate.queryForObject(
                sql,
                Timestamp.class, tableName);
        if (lastTime != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            updatedTime = formatter.format(lastTime); // NOSONAR
        }
        logger.info("getLastUpdatedTime from config. tableName: {} lastUpdatedTime:{}", tableName, lastTime);
        return updatedTime;
    }

    public String getLastUpdatedTimeS3(String tableName) {
        String sql = "select last_update_time_s3 from POLL_DATA_SYNC_CONFIG where table_name=?";
        String updatedTime = "";
        Timestamp lastTime = this.jdbcTemplate.queryForObject(
                sql,
                Timestamp.class, tableName);
        if (lastTime != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            updatedTime = formatter.format(lastTime); // NOSONAR
        }
        logger.info("getLastUpdatedTime from config. tableName: {} lastUpdatedTime:{}", tableName, lastTime);
        return updatedTime;
    }

    public String getLastUpdatedTimeLocalDir(String tableName) {
        String sql = "select last_update_time_local_dir from POLL_DATA_SYNC_CONFIG where table_name=?";
        String updatedTime = "";
        Timestamp lastTime = this.jdbcTemplate.queryForObject(
                sql,
                Timestamp.class, tableName);
        if (lastTime != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            updatedTime = formatter.format(lastTime); // NOSONAR
        }
        logger.info("getLastUpdatedTime from config. tableName: {} lastUpdatedTime:{}", tableName, lastTime);
        return updatedTime;
    }

    public void updateLastUpdatedTime(String tableName, Timestamp timestamp) {
        String updateSql = "update POLL_DATA_SYNC_CONFIG set last_update_time =? where table_name=?;";
        jdbcTemplate.update(updateSql, timestamp, tableName);
    }


    public void updateLastUpdatedTimeAndLog(String tableName, Timestamp timestamp, String log) {
        String updateSql = "update RDB.dbo.POLL_DATA_SYNC_CONFIG set last_update_time =?, last_executed_log=? where table_name=?;";
        jdbcTemplate.update(updateSql, timestamp, log, tableName);
    }

    public void updateLastUpdatedTimeAndLogS3(String tableName, Timestamp timestamp, String log) {
        String updateSql = "update RDB.dbo.POLL_DATA_SYNC_CONFIG set last_update_time_s3 =?, last_executed_log=? where table_name=?;";
        jdbcTemplate.update(updateSql, timestamp, log, tableName);
    }

    public void updateLastUpdatedTimeAndLogLocalDir(String tableName, Timestamp timestamp, String log) {
        String updateSql = "update RDB.dbo.POLL_DATA_SYNC_CONFIG set last_update_time_local_dir =?, last_executed_log=? where table_name=?;";
        jdbcTemplate.update(updateSql, timestamp, log, tableName);
    }



    public List<PollDataSyncConfig> getTableListFromConfig() {
        String sql = "select * from poll_data_sync_config pdsc order by table_order";
        List<PollDataSyncConfig> tableList = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(PollDataSyncConfig.class));
        logger.info("getTableListFromConfig size:{}", tableList.size());
        return tableList;
    }

    public void deleteTable(String tableName) {
        try {
            String deleteSql = "delete FROM " + tableName;
            jdbcTemplate.execute(deleteSql);
        } catch (Exception e) {
            logger.error("RDB:Error in deleting table:{}", e.getMessage());
        }
    }
}