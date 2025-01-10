package gov.cdc.nnddatapollservice.repository.nbs_odse;

import gov.cdc.nnddatapollservice.repository.nbs_odse.model.EDXActivityDetailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EDXActivityDetailLogRepository extends JpaRepository<EDXActivityDetailLog, Long> {
}
