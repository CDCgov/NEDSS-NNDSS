package gov.cdc.nnddatapollservice.repository.msg;

import gov.cdc.nnddatapollservice.repository.msg.model.NETSSTransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface NETSSTransportQOutRepository extends JpaRepository<NETSSTransportQOut, Long> {
    @Query("SELECT MAX(a.addTime) FROM NETSSTransportQOut a ")
    Optional<Timestamp> findMaxTimeStamp ();}
