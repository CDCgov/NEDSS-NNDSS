package gov.cdc.nnddatapollservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.rdb.dto.Condition;
import gov.cdc.nnddatapollservice.rdb.dto.ConfirmationMethod;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.dto.RdbDate;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RDBDataPersistentService {
    JdbcTemplate jdbcTemplate;
//    SimpleJdbcInsert simpleJdbcInsert;
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    @Autowired
    @Qualifier("rdbDataSource")
    private DataSource dataSource1;

    @Autowired
    public RDBDataPersistentService(@Qualifier("rdbDataSource") DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
//        simpleJdbcInsert =
//                new SimpleJdbcInsert(dataSource);
    }

    //@PostConstruct
    public void saveRDBData(String tableName, String jsonData) {
        System.out.println("CALLING RDBDataSync--saveRDBData tableName: " + tableName);
        if(tableName!=null && tableName.equalsIgnoreCase("CONFIRMATION_METHOD")) {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
                    .create();
            Type resultType = new TypeToken<List<ConfirmationMethod>>() {
            }.getType();
            List<ConfirmationMethod> list = gson.fromJson(jsonData, resultType);
            for(ConfirmationMethod confirmationMethod : list) {
                upsertConfirmationMethod(confirmationMethod);
            }
            System.out.println("CONFIRMATION_METHOD list: " + list.size());
        }else if(tableName!=null && tableName.equalsIgnoreCase("CONDITION")) {
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
            System.out.println("CONDITION list: " + list.size());
        }else if(tableName!=null && tableName.equalsIgnoreCase("RDB_DATE")) {
            System.out.println("--Inside RDB_DATE--");
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
                    .create();
            Type resultType = new TypeToken<List<RdbDate>>() {
            }.getType();
            List<RdbDate> list = gson.fromJson(jsonData, resultType);
            System.out.println("RDB_DATE list: " + list.size());
            for(RdbDate rdbDate : list) {
                upsertRdbDate(rdbDate);
            }
            System.out.println("Added RDB_DATE list: " + list.size());
        }
        else{
            try {
                SimpleJdbcInsert simpleJdbcInsert =
                        new SimpleJdbcInsert(dataSource1);
                simpleJdbcInsert = simpleJdbcInsert.withTableName(tableName);
                List<Map<String, Object>> records = jsonToListOfMap(jsonData);
                simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
                System.out.println("After executeBatch tableName: " + tableName);
            }catch(Exception e){
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
        System.out.println("json to list of maps:" + list.size());
        return list;
    }

    public String getLastUpdatedTime(String tableName) {
        String sql = "select last_update_time from POLL_DATA_SYNC_CONFIG where table_name=?";
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
        String updateSql = "update RDB.dbo.POLL_DATA_SYNC_CONFIG set last_update_time =? where table_name=?;";
        Timestamp timestamp = Timestamp.from(Instant.now());
        int updateCount = jdbcTemplate.update(
                updateSql,
                timestamp, tableName);
        //System.out.println("updateCount:" + updateCount);
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
        //System.out.println("merge sql:" + sql);
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
        //System.out.println("merge sql:" + sql);
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
        //System.out.println("merge sql:" + sql);
        jdbcTemplate.update(sql);
    }
    private static String getSqlString(String val){
        if(val!=null && !val.isEmpty()){
            return "'"+val+"'";
        }
        return val;
    }
public List<PollDataSyncConfig> getTableListFromConfig() {
        String sql="select * from RDB.dbo.poll_data_sync_config pdsc order by table_order";
    // Implementation of RowMapper interface
//    return jdbcTemplate.query("SELECT * FROM student", new RowMapper<PollDataSyncConfig>() {
//
//        public PollDataSyncConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
//            PollDataSyncConfig pollDataSyncConfig = new PollDataSyncConfig();
//            pollDataSyncConfig.setTableName(rs.getString("TABLE_NAME"));
//            pollDataSyncConfig.setSourceDb(rs.getString("SOURCE_DB"));
//            pollDataSyncConfig.setLastUpdateTime(rs.getTimestamp("LAST_UPDATE_TIME"));
//            pollDataSyncConfig.setTableOrder(rs.getString("TABLE_ORDER"));
//            return pollDataSyncConfig;
//        }
//    });
    List<PollDataSyncConfig> tableList = jdbcTemplate.query(
            sql,
            new BeanPropertyRowMapper(PollDataSyncConfig.class));
    System.out.println("tableList size::"+tableList.size());
    System.out.println("tableList:"+tableList);
    return tableList;
}
    //@PostConstruct
//    public void syncRDB() {
//        System.out.println("CALLING RDBDataSync--syncRDB");
//        simpleJdbcInsert=simpleJdbcInsert.withTableName("D_CASE_MANAGEMENT");
//        //multiple rows
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonString ="[{\"ACT_REF_TYPE_CD\":\"2\",\"ADD_USER_ID\":10055282,\"ADI_900_STATUS_CD\":\"02\",\"CA_INIT_INTVWR_ASSGN_DT\":\"2024-07-22 00:00:00.000\",\"CA_INTERVIEWER_ASSIGN_DT\":\"2024-07-22 00:00:00.000\",\"CA_PATIENT_INTV_STATUS\":\"A - Awaiting\",\"CASE_OID\":1300100016,\"D_CASE_MANAGEMENT_KEY\":53.0,\"EPI_LINK_ID\":\"1310005124\",\"FL_FUP_ACTUAL_REF_TYPE\":\"2 - Provider\",\"FL_FUP_DISPO_DT\":\"2024-07-23 00:00:00.000\",\"FL_FUP_DISPOSITION_CD\":\"1\",\"FL_FUP_DISPOSITION_DESC\":\"1 - Prev. Pos\",\"FL_FUP_EXAM_DT\":\"2024-07-19 00:00:00.000\",\"FL_FUP_EXPECTED_DT\":\"2024-07-21 00:00:00.000\",\"FL_FUP_EXPECTED_IN_IND\":\"Yes\",\"FL_FUP_FIELD_RECORD_NUM\":\"1310005124\",\"FL_FUP_INIT_ASSGN_DT\":\"2024-07-23 00:00:00.000\",\"FL_FUP_INTERNET_OUTCOME\":\"I1 - Informed, Urgent Health Matter\",\"FL_FUP_INTERNET_OUTCOME_CD\":\"I1\",\"FL_FUP_INVESTIGATOR_ASSGN_DT\":\"2024-07-23 00:00:00.000\",\"FL_FUP_NOTIFICATION_PLAN_CD\":\"3 - Dual\",\"FL_FUP_PROV_DIAGNOSIS\":\"900\",\"FL_FUP_PROV_EXM_REASON\":\"Community Screening\",\"FLD_FOLL_UP_EXPECTED_IN\":\"Y\",\"FLD_FOLL_UP_NOTIFICATION_PLAN\":\"3\",\"FLD_FOLL_UP_PROV_DIAGNOSIS\":\"900\",\"FLD_FOLL_UP_PROV_EXM_REASON\":\"M\",\"INIT_FUP_CLINIC_CODE\":\"80000\",\"INIT_FUP_INITIAL_FOLL_UP\":\"Surveillance Follow-up\",\"INIT_FUP_INITIAL_FOLL_UP_CD\":\"SF\",\"INIT_FUP_INTERNET_FOLL_UP_CD\":\"N\",\"INIT_FOLL_UP_NOTIFIABLE\":\"6-Yes, Notifiable\",\"INIT_FUP_NOTIFIABLE_CD\":\"06\",\"INITIATING_AGNCY\":\"Arizona\",\"INTERNET_FOLL_UP\":\"No\",\"INVESTIGATION_KEY\":264,\"OOJ_INITG_AGNCY_RECD_DATE\":\"2024-07-15 00:00:00.000\",\"PAT_INTV_STATUS_CD\":\"A\",\"STATUS_900\":\"2 - Newly Diagnosed\",\"SURV_CLOSED_DT\":\"2024-07-22 00:00:00.000\",\"SURV_INVESTIGATOR_ASSGN_DT\":\"2024-07-15 00:00:00.000\",\"SURV_PATIENT_FOLL_UP\":\"FF\",\"SURV_PATIENT_FOLL_UP_CD\":\"Field Follow-up\",\"SURV_PROV_EXM_REASON\":\"M\",\"SURV_PROVIDER_CONTACT\":\"S - Successful\",\"SURV_PROVIDER_CONTACT_CD\":\"S\",\"SURV_PROVIDER_DIAGNOSIS\":\"900\",\"SURV_PROVIDER_EXAM_REASON\":\"Community Screening\"},{\"ADD_USER_ID\":10000015,\"CASE_OID\":1300100015,\"D_CASE_MANAGEMENT_KEY\":2.0,\"EPI_LINK_ID\":\"1310000024\",\"FL_FUP_FIELD_RECORD_NUM\":\"1310000024\",\"INVESTIGATION_KEY\":267}]";
//        //Gson gson = new Gson();
//        Gson gson = new GsonBuilder().serializeNulls().create();
//        Type resultType = new TypeToken<List<Map<String, Object>>>(){}.getType();
//        List<Map<String, Object>> records = gson.fromJson(jsonString, resultType);
//
//        simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
//    }
}

