package gov.cdc.nnddatapollservice.rdb.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.rdb.dto.Condition;
import gov.cdc.nnddatapollservice.rdb.dto.ConfirmationMethod;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.dto.RdbDate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RdbDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(RdbDataPersistentDAO.class);
    private JdbcTemplate jdbcTemplate;
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private DataSource dataSource;

    @Autowired
    public RdbDataPersistentDAO(@Qualifier("rdbDataSource") DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    public void saveRDBData(String tableName, String jsonData) {
        logger.info("saveRDBData tableName: {0}",tableName);
        if (tableName != null && tableName.equalsIgnoreCase("CONFIRMATION_METHOD")) {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
                    .create();
            Type resultType = new TypeToken<List<ConfirmationMethod>>() {
            }.getType();
            List<ConfirmationMethod> list = gson.fromJson(jsonData, resultType);
            for (ConfirmationMethod confirmationMethod : list) {
                upsertConfirmationMethod(confirmationMethod);
            }
            logger.info("saveRDBData tableName:: {0} Inserted Records:{1}",tableName,list.size());
        } else if (tableName != null && tableName.equalsIgnoreCase("CONDITION")) {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
                    .create();
            Type resultType = new TypeToken<List<Condition>>() {
            }.getType();
            List<Condition> list = gson.fromJson(jsonData, resultType);
            for (Condition condition : list) {
                upsertCondition(condition);
            }
            logger.info("saveRDBData tableName:: {0} Inserted Records:{1}",tableName,list.size());
        } else if (tableName != null && tableName.equalsIgnoreCase("RDB_DATE")) {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
                    .create();
            Type resultType = new TypeToken<List<RdbDate>>() {
            }.getType();
            List<RdbDate> list = gson.fromJson(jsonData, resultType);
            for (RdbDate rdbDate : list) {
                upsertRdbDate(rdbDate);
            }

            logger.info("saveRDBData tableName:: {0} Inserted Records:{1}",tableName,list.size());
        } else {
            try {
                SimpleJdbcInsert simpleJdbcInsert =
                        new SimpleJdbcInsert(dataSource);
                simpleJdbcInsert = simpleJdbcInsert.withTableName(tableName);
                List<Map<String, Object>> records = jsonToListOfMap(jsonData);
                logger.info("Inside generic code before executeBatch tableName: {0} Updated Records:{1}",tableName,records.size());
                simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
                logger.info("Success.. executeBatch tableName: {0}",tableName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List jsonToListOfMap(String jsonData) {
        ObjectMapper mapper = new ObjectMapper();
        Gson gson = new GsonBuilder().serializeNulls().create();
        Type resultType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> list = gson.fromJson(jsonData, resultType);
        return list;
    }

    private void upsertConfirmationMethod(ConfirmationMethod confirmationMethod) {
        String sql = "MERGE INTO CONFIRMATION_METHOD AS target " +
                "USING (select " + confirmationMethod.getConfirmationMethodKey() + " as id) AS source " +
                "ON (target.CONFIRMATION_METHOD_KEY = source.id) " +
                "WHEN MATCHED THEN " +
                " UPDATE SET target.CONFIRMATION_METHOD_CD = " + getSqlString(confirmationMethod.getConfirmationMethodCd()) +
                ",target.CONFIRMATION_METHOD_DESC = " + getSqlString(confirmationMethod.getConfirmationMethodDesc()) +
                " WHEN NOT MATCHED THEN " +
                " INSERT (CONFIRMATION_METHOD_KEY, CONFIRMATION_METHOD_CD,CONFIRMATION_METHOD_DESC) " +
                "VALUES (" + confirmationMethod.getConfirmationMethodKey() +
                "," + getSqlString(confirmationMethod.getConfirmationMethodCd()) +
                "," + getSqlString(confirmationMethod.getConfirmationMethodDesc()) + ");";
        jdbcTemplate.update(sql);
    }

    private void upsertCondition(Condition condition) {
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
        jdbcTemplate.update(sql);
    }

    private void upsertRdbDate(RdbDate rdbDate) {
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
                "VALUES ("
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
        jdbcTemplate.update(sql);
    }

    private static String getSqlString(String val) {
        if (val != null && !val.isEmpty()) {
            return "'" + val + "'";
        }
        return val;
    }

    public String getLastUpdatedTime(String tableName) {
        String sql = "select last_update_time from POLL_DATA_SYNC_CONFIG where table_name=?";
        String updatedTime="";
        Timestamp lastTime = jdbcTemplate.queryForObject(
                sql, new Object[]{tableName}, Timestamp.class);
        if (lastTime != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            updatedTime= formatter.format(lastTime);
        }
        logger.info("getLastUpdatedTime tableName: {0} lastUpdatedTime:{1}", tableName,lastTime);
        return updatedTime;
    }

    public void updateLastUpdatedTime(String tableName,Timestamp timestamp) {
        String updateSql = "update RDB.dbo.POLL_DATA_SYNC_CONFIG set last_update_time =? where table_name=?;";
        jdbcTemplate.update(updateSql, timestamp, tableName);
    }

    public List<PollDataSyncConfig> getTableListFromConfig() {
        String sql = "select * from RDB.dbo.poll_data_sync_config pdsc order by table_order";
        List<PollDataSyncConfig> tableList = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper(PollDataSyncConfig.class));
        logger.info("getTableListFromConfig size:{0}",tableList.size());
        return tableList;
    }

    public void deleteTable(String tableName) {
        String truncateSql = "delete "+tableName;
        jdbcTemplate.execute(truncateSql);
    }
}
