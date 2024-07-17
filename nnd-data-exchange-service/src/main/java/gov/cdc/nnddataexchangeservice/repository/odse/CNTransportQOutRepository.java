package gov.cdc.nnddataexchangeservice.repository.odse;

import gov.cdc.nnddataexchangeservice.repository.msg.model.TransportQOut;
import gov.cdc.nnddataexchangeservice.repository.odse.model.CNTransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CNTransportQOutRepository extends JpaRepository<CNTransportQOut, Long> {
}
