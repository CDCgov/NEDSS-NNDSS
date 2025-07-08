package gov.cdc.nnddataexchangeservice.service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.rdb.DataViewConfigRepository;
import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataViewConfig;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataViewService;
import gov.cdc.nnddataexchangeservice.service.model.dto.DataViewConfigDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static gov.cdc.nnddataexchangeservice.constant.DataSyncConstant.*;
import static gov.cdc.nnddataexchangeservice.constant.DataViewConstant.DATA_VIEW_CUSTOM_PARAM;
import static gov.cdc.nnddataexchangeservice.constant.DataViewConstant.DATA_VIEW_PARAM;
import static gov.cdc.nnddataexchangeservice.shared.TimestampHandler.getCurrentTimeStamp;

@Service
public class DataViewService implements IDataViewService {
    private final DataViewConfigRepository dataViewConfigRepository;
    private final JdbcTemplate jdbcTemplate;
    private final JdbcTemplate srteJdbcTemplate;
    private final JdbcTemplate rdbModernJdbcTemplate;
    private final JdbcTemplate odseJdbcTemplate;
    private final JdbcTemplate msgJdbcTemplate;
    private final JdbcTemplate crossJdbcTemplate;
    @Value("${service.timezone}")
    private String tz = "UTC";
    public DataViewService(
            DataViewConfigRepository dataViewConfigRepository,
            @Qualifier("rdbJdbcTemplate") JdbcTemplate jdbcTemplate,
            @Qualifier("srteJdbcTemplate") JdbcTemplate srteJdbcTemplate,
            @Qualifier("rdbModernJdbcTemplate") JdbcTemplate rdbModernJdbcTemplate,
            @Qualifier("odseJdbcTemplate") JdbcTemplate odseJdbcTemplate,
            @Qualifier("msgJdbcTemplate")  JdbcTemplate msgJdbcTemplate,
            @Qualifier("crossDbJdbcTemplate")  JdbcTemplate crossJdbcTemplate
    )
    {
        this.dataViewConfigRepository = dataViewConfigRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.srteJdbcTemplate = srteJdbcTemplate;
        this.rdbModernJdbcTemplate = rdbModernJdbcTemplate;
        this.odseJdbcTemplate = odseJdbcTemplate;
        this.msgJdbcTemplate = msgJdbcTemplate;
        this.crossJdbcTemplate = crossJdbcTemplate;
    }

    public List<DataViewConfig> getConfigs(String queryName) throws DataExchangeException {
        List<DataViewConfig> dataViewConfigList = new ArrayList<>();
        if (queryName != null && !queryName.isEmpty()) {
            var dataViewConfig = getConfigByTableName(queryName);
            dataViewConfigList.add(dataViewConfig);
        }
        else {
            dataViewConfigList = dataViewConfigRepository.findAll();
        }

        return dataViewConfigList;
    }

    public DataViewConfig saveConfig(DataViewConfigDto dataViewConfigDto) throws DataExchangeException {
        DataViewConfig dataViewConfig = null;
        try {
            dataViewConfig = getConfigByTableName(dataViewConfigDto.getQueryName());
        } catch (Exception e) {
            if (!e.getMessage().contains("No Config found for table")) {
                throw new DataExchangeException("Error executing query: " + e.getMessage());
            }
        }
        if (dataViewConfig != null) {
            dataViewConfig.setSourceDb(dataViewConfigDto.getSourceDb());
            dataViewConfig.setQuery(dataViewConfigDto.getQuery());
            dataViewConfig.setMetaData(dataViewConfigDto.getMetaData());
            dataViewConfig.setCustomParamApplied(dataViewConfigDto.getCustomParamApplied());
            dataViewConfig.setUpdatedAt(getCurrentTimeStamp(tz));
        }
        else {
            dataViewConfig = new DataViewConfig(dataViewConfigDto);
        }

        var config = dataViewConfigRepository.save(dataViewConfig);
        return config;
    }

    public String getDataForDataView(String queryName, String param, String where) throws DataExchangeException {
        var config = getConfigByTableName(queryName);

        String[] paramArray;
        if (config.getCustomParamApplied()) {
            paramArray = new String[1];
            paramArray[0] = where;
        }
        else {
            paramArray = (param != null && !param.isEmpty()) ? param.split("~") : new String[0];
        }
        try {
            List<Map<String, Object>> result = jdbcTemplateHelperForDataRetrieval(config, paramArray, config.getCrossDbApplied());
            List<String> metaColumns = parseMetaColumns(config.getMetaData());
            JsonArray parentArray = buildJsonArrayFromResult(result, metaColumns);
            return new GsonBuilder().setPrettyPrinting().create().toJson(parentArray);
        } catch (Exception e) {
            throw new DataExchangeException("Error executing query: " + e.getMessage());
        }
    }


    public List<Map<String, Object>> jdbcTemplateHelperForDataRetrieval(DataViewConfig config,
                                                                        String[] paramArray,
                                                                        boolean crossDbApplied) throws DataExchangeException {
        String sourceDb = config.getSourceDb();
        String query = config.getQuery();
        Object[] params =  new Object[]{};
        if (config.getCustomParamApplied()) {
            boolean whereApplied = query.contains(DATA_VIEW_CUSTOM_PARAM);
            if (whereApplied && (paramArray == null || paramArray.length == 0)) {
                throw new DataExchangeException("Query expects a custom WHERE statement, but none were provided.");
            }

            if (paramArray[0].contains("WHERE ") || paramArray[0].contains("where ") || paramArray[0].contains(" WHERE ") || paramArray[0].contains(" where ")) {
                throw new DataExchangeException("Provided Where Expression container WHERE key word, please remove and try again.");
            }
            query = query.replace(DATA_VIEW_CUSTOM_PARAM, " WHERE\n" + paramArray[0] + "\n");
        }
        else {
            boolean paramApplied = query.contains(DATA_VIEW_PARAM);
            if (paramApplied && (paramArray == null || paramArray.length == 0)) {
                throw new DataExchangeException("Query expects parameters, but none were provided.");
            }
            params = paramApplied ? paramArray : new Object[]{};
        }
        JdbcTemplate selectedTemplate;
        if (crossDbApplied) {
            selectedTemplate = crossJdbcTemplate;
        }
        else {
            selectedTemplate = switch (sourceDb.toUpperCase()) {
                case DB_RDB -> jdbcTemplate;
                case DB_SRTE -> srteJdbcTemplate;
                case DB_RDB_MODERN -> rdbModernJdbcTemplate;
                case DB_NBS_ODSE -> odseJdbcTemplate;
                case DB_NBS_MSG -> msgJdbcTemplate;
                default -> throw new DataExchangeException("Database Not Supported: " + sourceDb);
            };

        }

        return selectedTemplate.queryForList(query, params);
    }


    private List<String> parseMetaColumns(String metaData) {
        return Arrays.stream(metaData.split(","))
                .map(String::trim)
                .toList();
    }

    private JsonArray buildJsonArrayFromResult(List<Map<String, Object>> result, List<String> columns) {
        JsonArray jsonArray = new JsonArray();
        for (Map<String, Object> row : result) {
            JsonObject jsonObject = new JsonObject();
            for (String column : columns) {
                Object value = row.get(column);
                jsonObject.addProperty(column, value != null ? value.toString() : null);
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


    private DataViewConfig getConfigByTableName(String tableName) throws DataExchangeException {
        var result = dataViewConfigRepository.findByName(tableName);
        if (result == null) {
            throw new DataExchangeException("No Config found for table: " + tableName);
        }

        return result;
    }
}
