package gov.cdc.nnddatapollservice.repository.rdb_modern;

import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NrtObservationRepository extends JpaRepository<NrtObservation, Long> {
}
