package gov.cdc.nnddatapollservice.repository.odse;

import gov.cdc.nnddatapollservice.repository.odse.model.CNTransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CNTransportQOutRepository extends JpaRepository<CNTransportQOut, Long> {
}
