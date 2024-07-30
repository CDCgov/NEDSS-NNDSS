package gov.cdc.nndmessageprocessor.repository;

import gov.cdc.nndmessageprocessor.repository.model.NETSSTransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NetssTransportQOutRepository extends JpaRepository<NETSSTransportQOut, Long> {
    String SELECT_NETSS_CASE_DATA_COLLECTION =
            "SELECT NETSS_TransportQ_out_uid AS netssTransportqOutUid, " +
                    "record_type_cd AS recordTypeCd, " +
                    "mmwr_year AS mmwrYear, " +
                    "mmwr_week AS mmwrWeek, " +
                    "netss_case_id AS netssCaseId, " +
                    "phc_local_id AS phcLocalId, " +
                    "notification_local_id AS notificationLocalId, " +
                    "add_time AS addTime, " +
                    "payload AS payload, " +
                    "record_status_cd AS recordStatusCd " +
                    "FROM (SELECT NETSS_TransportQ_out_uid, " +
                    "record_type_cd, " +
                    "mmwr_year, " +
                    "mmwr_week, " +
                    "netss_case_id, " +
                    "phc_local_id, " +
                    "notification_local_id, " +
                    "add_time, " +
                    "payload, " +
                    "record_status_cd, " +
                    "ROW_NUMBER() OVER (PARTITION BY netss_case_id ORDER BY add_time DESC) AS rn " +
                    "FROM NETSS_TransportQ_out) nt ";

    String WHERE_CLAUSE_YTD_AND_PRIOR_YEAR =
            "WHERE rn = 1 " +
                    "AND ((mmwr_Year = :currentYear AND mmwr_Week <= :currentWeek) OR (mmwr_Year = :priorYear)) " +
                    "AND record_Status_Cd != 'LOG_DEL' " +
                    "ORDER BY mmwr_Year, mmwr_Week";

    String WHERE_CLAUSE_YTD =
            "WHERE rn = 1 " +
                    "AND mmwr_Year = :currentYear " +
                    "AND mmwr_Week <= :currentWeek " +
                    "AND record_Status_Cd != 'LOG_DEL' " +
                    "ORDER BY mmwr_Year, mmwr_Week";

    @Query(value = SELECT_NETSS_CASE_DATA_COLLECTION + " " + WHERE_CLAUSE_YTD_AND_PRIOR_YEAR, nativeQuery = true)
    Optional<List<NETSSTransportQOut>> findNetssCaseDataYtdAndPriorYear(@Param("currentYear") int currentYear,
                                                                        @Param("currentWeek") int currentWeek,
                                                                        @Param("priorYear") int priorYear);

    @Query(value = SELECT_NETSS_CASE_DATA_COLLECTION + " " + WHERE_CLAUSE_YTD, nativeQuery = true)
    Optional<List<NETSSTransportQOut>> findNetssCaseDataYtd(@Param("currentYear") int currentYear,
                                                  @Param("currentWeek") int currentWeek);
}
