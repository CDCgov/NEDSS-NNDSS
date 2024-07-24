package gov.cdc.nnddataexchangeservice.repository.odse;

import gov.cdc.nnddataexchangeservice.repository.msg.model.TransportQOut;
import gov.cdc.nnddataexchangeservice.repository.odse.model.CNTransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("SELECT a FROM CNTransportQOut a WHERE a.recordStatusTime <= :recordStatusTime AND a.recordStatusCd = :recordStatusCd")
    Optional<Collection<CNTransportQOut>> findTransportByCreationTimeAndStatus (@Param("recordStatusTime") String recordStatusTime,
                                                                                @Param("recordStatusCd") String recordStatusCd);



    @Modifying
    @Query("UPDATE CNTransportQOut a SET a.recordStatusCd = :statusCd, a.recordStatusTime = :statusTime WHERE a.cnTransportqOutUid = :uid")
    void updateTransportStatusByUid(Long uid, String statusCd, Timestamp statusTime);
}