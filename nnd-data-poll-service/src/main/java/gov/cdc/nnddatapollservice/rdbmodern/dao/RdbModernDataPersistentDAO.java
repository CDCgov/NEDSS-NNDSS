package gov.cdc.nnddatapollservice.rdbmodern.dao;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.constant.ConstantValue;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdbmodern.dto.NrtObservationDto;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservation;
import gov.cdc.nnddatapollservice.share.HandleError;
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

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.LOG_SUCCESS;

@Service
@Slf4j
public class RdbModernDataPersistentDAO {
    private static Logger logger = LoggerFactory.getLogger(RdbModernDataPersistentDAO.class);
    private JdbcTemplate jdbcTemplate;

    private final NrtObservationRepository nrtObservationRepository;

    @Value("${datasync.sql_error_handle_log}")
    protected String sqlErrorPath = "";
    private final Gson gsonNorm = new Gson();
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
            .create();
    @Autowired
    public RdbModernDataPersistentDAO(@Qualifier("rdbmodernJdbcTemplate") JdbcTemplate jdbcTemplate,
                                      NrtObservationRepository nrtObservationRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.nrtObservationRepository = nrtObservationRepository;
    }

    public String saveRdbModernData(String tableName, String jsonData) throws DataPollException {
        logger.info("saveRdbModernData tableName: {}", tableName);
        StringBuilder logBuilder = new StringBuilder(LOG_SUCCESS);
        if ("NRT_OBSERVATION".equalsIgnoreCase(tableName)) {
            logBuilder = new StringBuilder();
            Type resultType = new TypeToken<List<NrtObservationDto>>() {
            }.getType();
            List<NrtObservationDto> list = gson.fromJson(jsonData, resultType);
            for (NrtObservationDto data : list) {
                try {
                    var domainModel = new NrtObservation(data);
                    nrtObservationRepository.save(domainModel);
                } catch (Exception e) {
                    logger.error("ERROR occured at record: {}", gsonNorm.toJson(data));
                    HandleError.writeRecordToFileTypedObject(gsonNorm, data, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + e.getClass().getSimpleName() + "/" + tableName + "/");
                    throw new DataPollException("Tried individual process, but not success");
                }
            }
        } else {
            try {
                SimpleJdbcInsert simpleJdbcInsert =
                        new SimpleJdbcInsert(jdbcTemplate);
                if (tableName != null && !tableName.isEmpty()) {
                    deleteTable(tableName);//Delete first
                    simpleJdbcInsert = simpleJdbcInsert.withTableName(tableName);
                    List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
                    if (records != null && !records.isEmpty()) {
                        logger.info("Inside generic code before executeBatch tableName: {} Records size:{}", tableName, records.size());

                        try {
                            if (records.size() > ConstantValue.SQL_BATCH_SIZE) {
                                int sublistSize = ConstantValue.SQL_BATCH_SIZE;
                                for (int i = 0; i < records.size(); i += sublistSize) {
                                    int end = Math.min(i + sublistSize, records.size());
                                    List<Map<String, Object>> sublist = records.subList(i, end);
                                    simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(sublist));
                                }
                            } else {
                                simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
                            }
                        } catch (DuplicateKeyException e) {
                            for (Map<String, Object> record : records) {
                                try {
                                    simpleJdbcInsert.execute(new MapSqlParameterSource(record));
                                } catch (Exception ei) {
                                    // TODO: LOG THESE
                                    logger.error("ERROR occured at record: {}", gsonNorm.toJson(record));
                                    HandleError.writeRecordToFile(gsonNorm, record, tableName + UUID.randomUUID(), sqlErrorPath + "/RDB_MODERN/" + ei.getClass().getSimpleName() + "/" + tableName + "/");
                                    throw new DataPollException("Tried individual process, but not success");
                                }
                            }
                        }


                    } else {
                        logger.info("saveRdbModernData tableName: {} Records size:0", tableName);
                    }
                }

            } catch (Exception e) {
                logger.error("Error executeBatch. class: {}, tableName: {}, Error:{}", e.getClass() ,tableName, e.getMessage());
                logBuilder = new StringBuilder(e.getMessage());
            }

        }



        return logBuilder.toString();
    }



    public String upsertNrtObservation(NrtObservationDto observation) {
        String log = "SUCCESS";
        String sql = "MERGE INTO nrt_observation AS target USING " +
                "(SELECT " + observation.getObservationUid() + " AS id) AS source " +
                "ON (target.observation_uid = source.id) " +
                "WHEN MATCHED THEN " +
                " UPDATE SET " +
                "target.class_cd = " + getSqlString(observation.getClassCd()) + ", " +
                "target.mood_cd = " + getSqlString(observation.getMoodCd()) + ", " +
                "target.act_uid = " + observation.getActUid() + ", " +
                "target.cd_desc_text = " + getSqlString(observation.getCdDescText()) + ", " +
                "target.record_status_cd = " + getSqlString(observation.getRecordStatusCd()) + ", " +
                "target.jurisdiction_cd = " + getSqlString(observation.getJurisdictionCd()) + ", " +
                "target.program_jurisdiction_oid = " + observation.getProgramJurisdictionOid() + ", " +
                "target.prog_area_cd = " + getSqlString(observation.getProgAreaCd()) + ", " +
                "target.pregnant_ind_cd = " + getSqlString(observation.getPregnantIndCd()) + ", " +
                "target.local_id = " + getSqlString(observation.getLocalId()) + ", " +
                "target.activity_to_time = " + getSqlString(observation.getActivityToTime()) + ", " +
                "target.effective_from_time = " + getSqlString(observation.getEffectiveFromTime()) + ", " +
                "target.rpt_to_state_time = " + getSqlString(observation.getRptToStateTime()) + ", " +
                "target.electronic_ind = " + getSqlString(observation.getElectronicInd()) + ", " +
                "target.version_ctrl_nbr = " + observation.getVersionCtrlNbr() + ", " +
                "target.ordering_person_id = " + observation.getOrderingPersonId() + ", " +
                "target.patient_id = " + observation.getPatientId() + ", " +
                "target.last_chg_time = " + getSqlString(observation.getLastChgTime()) + ", " +
                "target.refresh_datetime = " + getSqlString(observation.getRefreshDatetime()) + " " +
                "WHEN NOT MATCHED THEN " +
                " INSERT (observation_uid, class_cd, mood_cd, act_uid, cd_desc_text, record_status_cd, jurisdiction_cd, " +
                "program_jurisdiction_oid, prog_area_cd, pregnant_ind_cd, local_id, activity_to_time, effective_from_time, " +
                "rpt_to_state_time, electronic_ind, version_ctrl_nbr, ordering_person_id, patient_id, last_chg_time, refresh_datetime) " +
                "VALUES (" +
                observation.getObservationUid() + ", " +
                getSqlString(observation.getClassCd()) + ", " +
                getSqlString(observation.getMoodCd()) + ", " +
                observation.getActUid() + ", " +
                getSqlString(observation.getCdDescText()) + ", " +
                getSqlString(observation.getRecordStatusCd()) + ", " +
                getSqlString(observation.getJurisdictionCd()) + ", " +
                observation.getProgramJurisdictionOid() + ", " +
                getSqlString(observation.getProgAreaCd()) + ", " +
                getSqlString(observation.getPregnantIndCd()) + ", " +
                getSqlString(observation.getLocalId()) + ", " +
                getSqlString(observation.getActivityToTime()) + ", " +
                getSqlString(observation.getEffectiveFromTime()) + ", " +
                getSqlString(observation.getRptToStateTime()) + ", " +
                getSqlString(observation.getElectronicInd()) + ", " +
                observation.getVersionCtrlNbr() + ", " +
                observation.getOrderingPersonId() + ", " +
                observation.getPatientId() + ", " +
                getSqlString(observation.getLastChgTime()) + ", " +
                getSqlString(observation.getRefreshDatetime()) + ");";
        try {
            jdbcTemplate.update(sql);
        } catch (Exception e) {
            logger.error("Error in upsert for nrt_observation table: {}", e.getMessage());
            log = e.getMessage();
        }
        return log;
    }

    private String getSqlString(String value) {
        return value != null ? "'" + value + "'" : "NULL";
    }

    private String getSqlTimestamp(Timestamp timestamp) {
        return timestamp != null ? "'" + timestamp + "'" : "NULL";
    }


    public void deleteTable(String tableName) {
        try {
            String deleteSql = "delete FROM " + tableName;
            jdbcTemplate.execute(deleteSql);
        } catch (Exception e) {
            logger.error("RDB_MODERN:Error in deleting table:{}", e.getMessage());
        }
    }
}
