package gov.cdc.nnddatapollservice.repository.srte;

import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservationCoded;
import gov.cdc.nnddatapollservice.repository.srte.model.CodeToCondition;
import gov.cdc.nnddatapollservice.repository.srte.model.CodeToConditionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeToConditionRepository extends JpaRepository<CodeToCondition, CodeToConditionId> {
}
