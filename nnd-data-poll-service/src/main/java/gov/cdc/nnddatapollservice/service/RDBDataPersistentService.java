package gov.cdc.nnddatapollservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.rdb.dto.Condition;
import gov.cdc.nnddatapollservice.rdb.dto.ConfirmationMethod;
import gov.cdc.nnddatapollservice.rdb.dto.RdbDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class RDBDataPersistentService {
    JdbcTemplate jdbcTemplate;
    SimpleJdbcInsert simpleJdbcInsert;
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    @Autowired
    public RDBDataPersistentService(@Qualifier("rdbDataSource") DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert =
                new SimpleJdbcInsert(dataSource);
    }

    //@PostConstruct
    public void saveRDBData(String tableName, String jsonData) {
        System.out.println("CALLING RDBDataSync--syncRDB");
        if(tableName!=null && tableName.equalsIgnoreCase("CONFIRMATION_METHOD")) {
            System.out.println("--Inside CONFIRMATION_METHOD--");
            //Gson gson = new Gson();
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
                    .create();
//            String testJson="{\"CONFIRMATION_METHOD_KEY\":4,\"CONFIRMATION_METHOD_CD\":\"AS\",\"CONFIRMATION_METHOD_DESC\":\"Active Surveillance4\"}";
//            ConfirmationMethod confirmationMethod1= gson.fromJson(testJson, ConfirmationMethod.class);
//            System.out.println("getConfirmationMethodDesc:"+confirmationMethod1.getConfirmationMethodDesc());
//            upsertConfirmationMethod(confirmationMethod1);
            Type resultType = new TypeToken<List<ConfirmationMethod>>() {
            }.getType();
            List<ConfirmationMethod> list = gson.fromJson(jsonData, resultType);
            for(ConfirmationMethod confirmationMethod : list) {
                upsertConfirmationMethod(confirmationMethod);
            }
            System.out.println("CONFIRMATION_METHOD list: " + list.size());
        }else if(tableName!=null && tableName.equalsIgnoreCase("CONDITION")) {
            System.out.println("--Inside CONDITION--");
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
                    .create();
            Type resultType = new TypeToken<List<Condition>>() {
            }.getType();
            List<Condition> list = gson.fromJson(jsonData, resultType);
            System.out.println("CONDITION list: " + list.size());
            for(Condition condition : list) {
                upsertCondition(condition);
            }
        }else if(tableName!=null && tableName.equalsIgnoreCase("RDB_DATE")) {
            System.out.println("--Inside RDB_DATE--");
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
                    .create();
            Type resultType = new TypeToken<List<RdbDate>>() {
            }.getType();
            List<RdbDate> list = gson.fromJson(jsonData, resultType);
            System.out.println("CONDITION list: " + list.size());
            for(RdbDate rdbDate : list) {
                upsertRdbDate(rdbDate);
            }
        }
        else{
            simpleJdbcInsert = simpleJdbcInsert.withTableName(tableName);
            List<Map<String, Object>> records = jsonToListOfMap(jsonData);
            simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
        }
    }

    private List jsonToListOfMap(String jsonData) {
        ObjectMapper mapper = new ObjectMapper();
        Gson gson = new Gson();
        Type resultType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> list = gson.fromJson(jsonData, resultType);

        System.out.println("json to list of maps:" + list);
        return list;
    }

    public String getLastUpdatedTime(String tableName) {
        String sql = "select last_update_time from data_exchange_config where table_name=?";
        Timestamp lastTime = jdbcTemplate.queryForObject(
                sql, new Object[]{tableName}, Timestamp.class);
        System.out.println("lastTime timestamp:" + lastTime);
        if (lastTime != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            return formatter.format(lastTime);
        } else {
            return "";
        }
    }

    public void updateLastUpdatedTime(String tableName) {
        String updateSql = "update RDB.dbo.data_exchange_config set last_update_time =? where table_name=?;";
        Timestamp timestamp = Timestamp.from(Instant.now());
        int updateCount = jdbcTemplate.update(
                updateSql,
                timestamp, tableName);
        System.out.println("updateCount:" + updateCount);
    }

    public void upsertConfirmationMethod(ConfirmationMethod confirmationMethod) {
        String sql = "MERGE INTO CONFIRMATION_METHOD AS target " +
                "USING (select " + confirmationMethod.getConfirmationMethodKey() + " as id) AS source " +
                "ON (target.CONFIRMATION_METHOD_KEY = source.id) " +
                "WHEN MATCHED THEN " +
                " UPDATE SET target.CONFIRMATION_METHOD_CD = " + getSqlString(confirmationMethod.getConfirmationMethodCd())+
                ",target.CONFIRMATION_METHOD_DESC = " + getSqlString(confirmationMethod.getConfirmationMethodDesc()) +
                " WHEN NOT MATCHED THEN " +
                " INSERT (CONFIRMATION_METHOD_KEY, CONFIRMATION_METHOD_CD,CONFIRMATION_METHOD_DESC) " +
                "VALUES (" + confirmationMethod.getConfirmationMethodKey() +
                "," + getSqlString(confirmationMethod.getConfirmationMethodCd()) +
                "," + getSqlString(confirmationMethod.getConfirmationMethodDesc()) + ");";
        System.out.println("merge sql:" + sql);
        jdbcTemplate.update(sql);
    }
    public void upsertCondition(Condition condition) {
        String sql = "MERGE INTO CONDITION AS target " +
                "USING (select "+condition.getConditionKey()+" as id) AS source ON" +
                "(target.CONDITION_KEY = source.id)" +
                "WHEN MATCHED THEN " +
                "UPDATE " +
                "SET " +
                " target.CONDITION_CD = "+getSqlString(condition.getConditionCd())+"," +
                " target.CONDITION_DESC = "+getSqlString(condition.getConditionDesc())+"," +
                " target.CONDITION_SHORT_NM = "+getSqlString(condition.getConditionShortNm())+"," +
                " target.CONDITION_CD_EFF_DT = "+getSqlString(condition.getConditionCdEffDt())+"," +
                " target.CONDITION_CD_END_DT = "+getSqlString(condition.getConditionCdEndDt())+"," +
                " target.NND_IND = "+getSqlString(condition.getNndInd())+"," +
                " target.DISEASE_GRP_CD = "+getSqlString(condition.getDiseaseGrpCd())+"," +
                " target.DISEASE_GRP_DESC = "+getSqlString(condition.getDiseaseGrpDesc())+"," +
                " target.PROGRAM_AREA_CD = "+getSqlString(condition.getProgramAreaCd())+"," +
                " target.PROGRAM_AREA_DESC = "+getSqlString(condition.getProgramAreaDesc())+"," +
                " target.CONDITION_CD_SYS_CD_NM = "+getSqlString(condition.getConditionCdSysCdNm())+"," +
                " target.ASSIGNING_AUTHORITY_CD = "+getSqlString(condition.getAssigningAuthorityCd())+"," +
                " target.ASSIGNING_AUTHORITY_DESC = "+getSqlString(condition.getAssigningAuthorityDesc())+"," +
                " target.CONDITION_CD_SYS_CD = "+getSqlString(condition.getConditionCdSysCd())+
                " WHEN NOT MATCHED THEN " +
                "INSERT " +
                " (CONDITION_KEY," +
                " CONDITION_CD," +
                " CONDITION_DESC,CONDITION_SHORT_NM,CONDITION_CD_EFF_DT,CONDITION_CD_END_DT,NND_IND," +
                " DISEASE_GRP_CD,DISEASE_GRP_DESC,PROGRAM_AREA_CD,PROGRAM_AREA_DESC," +
                " CONDITION_CD_SYS_CD_NM,ASSIGNING_AUTHORITY_CD,ASSIGNING_AUTHORITY_DESC,CONDITION_CD_SYS_CD)" +
                "VALUES ("
                +condition.getConditionKey()+"," +
                getSqlString(condition.getConditionCd())+"," +
                getSqlString(condition.getConditionDesc())+"," +
                getSqlString(condition.getConditionShortNm())+"," +
                getSqlString(condition.getConditionCdEffDt())+"," +
                getSqlString(condition.getConditionCdEndDt())+"," +
                getSqlString(condition.getNndInd())+"," +
                getSqlString(condition.getDiseaseGrpCd())+"," +
                getSqlString(condition.getDiseaseGrpDesc())+"," +
                getSqlString(condition.getProgramAreaCd())+"," +
                getSqlString(condition.getProgramAreaDesc())+"," +
                getSqlString(condition.getConditionCdSysCdNm())+"," +
                getSqlString(condition.getAssigningAuthorityCd())+"," +
                getSqlString(condition.getAssigningAuthorityDesc())+"," +
                getSqlString(condition.getConditionCdSysCd()) +
                ");";
        System.out.println("merge sql:" + sql);
        jdbcTemplate.update(sql);
    }
    public void upsertRdbDate(RdbDate rdbDate) {
        String sql = "MERGE INTO RDB_DATE AS target " +
                "USING (select "+rdbDate.getDateKey()+" as id) AS source ON" +
                "(target.DATE_KEY = source.id)" +
                "WHEN MATCHED THEN " +
                "UPDATE " +
                "SET " +
                " target.DATE_MM_DD_YYYY = "+getSqlString(rdbDate.getDateMmDdYyyy())+"," +
                " target.DAY_OF_WEEK = "+getSqlString(rdbDate.getDayOfWeek())+"," +
                " target.DAY_NBR_IN_CLNDR_MON = "+rdbDate.getDayNbrInClndrMon()+"," +
                " target.DAY_NBR_IN_CLNDR_YR = "+rdbDate.getDayNbrInClndrYr()+"," +
                " target.WK_NBR_IN_CLNDR_MON = "+rdbDate.getWkNbrInClndrMon()+"," +
                " target.WK_NBR_IN_CLNDR_YR = "+rdbDate.getWkNbrInClndrYr()+"," +
                " target.CLNDR_MON_NAME = "+getSqlString(rdbDate.getClndrMonName())+"," +
                " target.CLNDR_MON_IN_YR = "+rdbDate.getClndrMonInYr()+"," +
                " target.CLNDR_QRTR = "+rdbDate.getClndrQrtr()+"," +
                " target.CLNDR_YR = "+rdbDate.getClndrYr()+
                " WHEN NOT MATCHED THEN " +
                "INSERT " +
                " (DATE_KEY," +
                " DATE_MM_DD_YYYY," +
                " DAY_OF_WEEK,DAY_NBR_IN_CLNDR_MON,DAY_NBR_IN_CLNDR_YR,WK_NBR_IN_CLNDR_MON,WK_NBR_IN_CLNDR_YR," +
                " CLNDR_MON_NAME,CLNDR_MON_IN_YR,CLNDR_QRTR,CLNDR_YR)" +
                "VALUES ("
                +rdbDate.getDateKey()+"," +
                getSqlString(rdbDate.getDateMmDdYyyy())+"," +
                getSqlString(rdbDate.getDayOfWeek())+"," +
                rdbDate.getDayNbrInClndrMon()+"," +
                rdbDate.getDayNbrInClndrYr()+"," +
                rdbDate.getWkNbrInClndrMon()+"," +
                rdbDate.getWkNbrInClndrYr()+"," +
                getSqlString(rdbDate.getClndrMonName())+"," +
                rdbDate.getClndrMonInYr()+"," +
                rdbDate.getClndrQrtr()+"," +
                rdbDate.getClndrYr()+
                ");";
        System.out.println("merge sql:" + sql);
        jdbcTemplate.update(sql);
    }
    private static String getSqlString(String val){
        if(val!=null && !val.isEmpty()){
            return "'"+val+"'";
        }
        return val;
    }
}

