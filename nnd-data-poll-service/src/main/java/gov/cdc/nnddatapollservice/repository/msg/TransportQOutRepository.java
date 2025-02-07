package gov.cdc.nnddatapollservice.repository.msg;

import gov.cdc.nnddatapollservice.repository.msg.model.TransportQOut;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface TransportQOutRepository extends JpaRepository<TransportQOut, Long> {

    @Query("SELECT MAX(a.messageCreationTime) FROM TransportQOut a ")
    Optional<String> findMaxTimeStamp ();

    @Query(value = "SELECT MAX(a.messageCreationTime) FROM TransportQ_out a WHERE a.messageId NOT IN (SELECT DISTINCT notification_local_id FROM CN_transportq_out)", nativeQuery = true)
    Optional<String> findMaxTimeStampInvolvingWithNotification();

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE TransportQ_out", nativeQuery = true)
    void truncateTable();
}
