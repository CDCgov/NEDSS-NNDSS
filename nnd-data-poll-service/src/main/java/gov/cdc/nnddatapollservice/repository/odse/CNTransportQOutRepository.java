package gov.cdc.nnddatapollservice.repository.odse;

import gov.cdc.nnddatapollservice.repository.odse.model.CNTransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface CNTransportQOutRepository extends JpaRepository<CNTransportQOut, Long> {
    @Query("SELECT MAX(a.recordStatusTime) FROM CNTransportQOut a ")
    Optional<Timestamp> findMaxTimeStamp ();
}