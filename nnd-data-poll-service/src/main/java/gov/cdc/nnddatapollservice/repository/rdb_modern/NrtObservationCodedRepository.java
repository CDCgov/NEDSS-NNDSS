package gov.cdc.nnddatapollservice.repository.rdb_modern;

import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservationCoded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NrtObservationCodedRepository extends JpaRepository<NrtObservationCoded, Long> {
}
