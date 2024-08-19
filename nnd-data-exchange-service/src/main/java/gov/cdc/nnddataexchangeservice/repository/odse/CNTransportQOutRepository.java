package gov.cdc.nnddataexchangeservice.repository.odse;

import gov.cdc.nnddataexchangeservice.repository.odse.model.CNTransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface CNTransportQOutRepository extends JpaRepository<CNTransportQOut, Long> {
    @Query("SELECT a FROM CNTransportQOut a WHERE a.recordStatusCd = :statusCd")
    Optional<Collection<CNTransportQOut>> findTransportByStatusCd (@Param("statusCd") String statusCd);

    @Query("SELECT a FROM CNTransportQOut a WHERE a.recordStatusTime > :recordStatusTime AND a.recordStatusCd = :recordStatusCd")
    Optional<Collection<CNTransportQOut>> findTransportByCreationTimeAndStatus (@Param("recordStatusTime") Timestamp recordStatusTime,
                                                                                @Param("recordStatusCd") String recordStatusCd);

    @Query(value = "SELECT TOP (:limit) a.*  FROM CN_transportq_out a WHERE a.record_status_cd = :statusCd ORDER BY a.record_status_time ASC", nativeQuery = true)
    Optional<Collection<CNTransportQOut>> findTransportByStatusCdWLimit (@Param("statusCd") String statusCd, @Param("limit") int limit);

    @Query(value = "SELECT TOP (:limit) a.*  FROM CN_transportq_out a WHERE a.record_status_time > :recordStatusTime AND a.record_status_cd = :recordStatusCd ORDER BY a.record_status_time ASC", nativeQuery = true)
    Optional<Collection<CNTransportQOut>> findTransportByCreationTimeAndStatusWLimit (@Param("recordStatusTime") Timestamp recordStatusTime,
                                                                                @Param("recordStatusCd") String recordStatusCd,
                                                                                @Param("limit") int limit);


}
