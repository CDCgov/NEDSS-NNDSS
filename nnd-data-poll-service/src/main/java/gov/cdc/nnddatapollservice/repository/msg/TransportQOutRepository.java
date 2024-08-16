package gov.cdc.nnddatapollservice.repository.msg;

import gov.cdc.nnddatapollservice.repository.msg.model.TransportQOut;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

//@Repository
//public interface TransportQOutRepository extends JpaRepository<TransportQOut, Long> {
//    @Query("SELECT MAX(a.recordStatusTime) FROM CNTransportQOut a ")
//    Optional<Timestamp> findMaxTimeStamp ();
//
//}
//

@Repository

public interface TransportQOutRepository extends JpaRepository<TransportQOut, Long> {

    @Query("SELECT MAX(a.messageCreationTime) FROM TransportQOut a ")
    Optional<String> findMaxTimeStamp ();


    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE TransportQ_out", nativeQuery = true)
    void truncateTable();
}
