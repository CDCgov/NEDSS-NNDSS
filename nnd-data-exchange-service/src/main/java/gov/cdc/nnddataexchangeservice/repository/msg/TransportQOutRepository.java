package gov.cdc.nnddataexchangeservice.repository.msg;

import gov.cdc.nnddataexchangeservice.repository.msg.model.NETSSTransportQOut;
import gov.cdc.nnddataexchangeservice.repository.msg.model.TransportQOut;
import gov.cdc.nnddataexchangeservice.repository.odse.model.CNTransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface TransportQOutRepository extends JpaRepository<TransportQOut, Long> {

    @Query("SELECT a FROM TransportQOut a WHERE a.messageCreationTime <= :recordStatusTime")
    Optional<Collection<TransportQOut>> findTransportByCreationTime(@Param("recordStatusTime") String recordStatusTime);

    @Query("SELECT a FROM TransportQOut a")
    Optional<Collection<TransportQOut>> findTransportByWithoutCreationTime();
}
