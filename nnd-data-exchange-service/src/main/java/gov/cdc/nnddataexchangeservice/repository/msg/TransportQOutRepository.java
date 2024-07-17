package gov.cdc.nnddataexchangeservice.repository.msg;

import gov.cdc.nnddataexchangeservice.repository.msg.model.NETSSTransportQOut;
import gov.cdc.nnddataexchangeservice.repository.msg.model.TransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportQOutRepository extends JpaRepository<TransportQOut, Long> {
}
