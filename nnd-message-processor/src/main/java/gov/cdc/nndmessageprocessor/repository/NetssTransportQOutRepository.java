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

    String SELECT_NETSS_CASE_DATA_COLLECTION = "SELECT " +
            "nt.NETSS_TransportQ_out_uid, " +
            "nt.record_type_cd , " +
            "nt.mmwr_year, " +
            "nt.mmwr_week , " +
            "nt.netss_case_id, " +
            "nt.phc_local_id , " +
            "nt.notification_local_id, " +
            "nt.add_time, " +
            "nt.payload, " +
            "nt.record_status_cd " +
            "FROM (SELECT " +
            "NETSS_TransportQ_out_uid, " +
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

    String WHERE_CLAUSE_YTD_AND_PRIOR_YEAR = "WHERE nt.rn = 1 " +
            "AND ((nt.mmwr_year = :currentYear AND nt.mmwr_week <= :currentWeek) OR (nt.mmwr_year = :priorYear)) " +
            "AND nt.record_status_cd != 'LOG_DEL' " +
            "ORDER BY nt.mmwr_year, nt.mmwr_week";

    String WHERE_CLAUSE_YTD = "WHERE nt.rn = 1 " +
            "AND nt.mmwr_year = :currentYear " +
            "AND nt.mmwr_week <= :currentWeek " +
            "AND nt.record_status_cd != 'LOG_DEL' " +
            "ORDER BY nt.mmwr_year, nt.mmwr_week";

    @Query(value = SELECT_NETSS_CASE_DATA_COLLECTION + WHERE_CLAUSE_YTD_AND_PRIOR_YEAR, nativeQuery = true)
    Optional<List<NETSSTransportQOut>> findNetssCaseDataYtdAndPriorYear(@Param("currentYear") int currentYear,
                                                                        @Param("currentWeek") int currentWeek,
                                                                        @Param("priorYear") int priorYear);

    @Query(value = SELECT_NETSS_CASE_DATA_COLLECTION + WHERE_CLAUSE_YTD, nativeQuery = true)
    Optional<List<NETSSTransportQOut>> findNetssCaseDataYtd(@Param("currentYear") int currentYear,
                                                            @Param("currentWeek") int currentWeek);
}