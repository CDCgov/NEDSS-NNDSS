package gov.cdc.nnddatapollservice.repository.msg;

import gov.cdc.nnddatapollservice.repository.msg.model.TransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportQOutRepository extends JpaRepository<TransportQOut, Long> {
}
