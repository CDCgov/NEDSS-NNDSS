package gov.cdc.nnddatapollservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    public RDBDataPersistentService(@Qualifier("rdbDataSource")DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert =
                new SimpleJdbcInsert(dataSource);
    }

    //@PostConstruct
    public void saveRDBData(String tableName, String jsonData) {
        System.out.println("CALLING RDBDataSync--syncRDB");
        simpleJdbcInsert=simpleJdbcInsert.withTableName(tableName);
        //multiple rows
        List<Map<String, Object>> records=jsonToListOfMap(jsonData);
        simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
    }
    private List jsonToListOfMap(String jsonData) {
        ObjectMapper mapper = new ObjectMapper();
        Gson gson = new Gson();
        Type resultType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> list = gson.fromJson(jsonData, resultType);

        System.out.println("json to list of maps:"+list);
        return list;
    }
    public String getLastUpdatedTime(String tableName){
        String sql="select last_update_time from data_exchange_config where table_name=?";
        Timestamp lastTime= jdbcTemplate.queryForObject(
                sql, new Object[]{tableName}, Timestamp.class);
        System.out.println("lastTime timestamp:"+lastTime);
        if (lastTime!=null) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            return formatter.format(lastTime);
        }
        else {
            return "";
        }
    }
    public void updateLastUpdatedTime(String tableName) {
        String updateSql="update RDB.dbo.data_exchange_config set last_update_time =? where table_name=?;";
        Timestamp timestamp = Timestamp.from(Instant.now());
        int updateCount=jdbcTemplate.update(
                updateSql,
                timestamp, tableName);
        System.out.println("updateCount:"+updateCount);
    }
//    private List jsonToListOfMap1111() {
//        ObjectMapper mapper = new ObjectMapper();
//
//        String jsonString ="[\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 1,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : null,\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : null\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 3,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : \"NA\",\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : null\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 5,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : \"AS\",\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : \"Active Surveillance\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 7,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : \"OD\",\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : \"Occupational disease surveillance\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 8,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : \"PD\",\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : \"Provider certified\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 10,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : \"S\",\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : \"Local\\/State specified\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 12,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : \"CD\",\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : \"Clinical diagnosis (non-laboratory confirmed)\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 14,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : \"CI\",\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : \"Case\\/Outbreak Investigation\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 16,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : \"NI\",\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : \"No information given\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 18,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : \"E\",\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : \"Epidemiologically linked\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 20,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : \"LD\",\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : \"Laboratory confirmed\"\n" +
//                "\t},\n" +
//                "\t{\n" +
//                "\t\t\"CONFIRMATION_METHOD_KEY\" : 21,\n" +
//                "\t\t\"CONFIRMATION_METHOD_CD\" : \"LR\",\n" +
//                "\t\t\"CONFIRMATION_METHOD_DESC\" : \"Laboratory report\"\n" +
//                "\t}\n" +
//                "]";
//        Gson gson = new Gson();
//        Type resultType = new TypeToken<List<Map<String, Object>>>(){}.getType();
//        List<Map<String, Object>> list = gson.fromJson(jsonString, resultType);
//
//        System.out.println("json to list of maps:"+list);
//        return list;
//    }

}