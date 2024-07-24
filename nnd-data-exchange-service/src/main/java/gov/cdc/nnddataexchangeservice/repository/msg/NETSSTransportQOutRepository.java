package gov.cdc.nnddataexchangeservice.repository.msg;

import gov.cdc.nnddataexchangeservice.repository.msg.model.NETSSTransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NETSSTransportQOutRepository extends JpaRepository<NETSSTransportQOut, Long> {
}
