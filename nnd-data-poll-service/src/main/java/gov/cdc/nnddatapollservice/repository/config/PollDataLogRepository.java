package gov.cdc.nnddatapollservice.repository.config;

import gov.cdc.nnddatapollservice.repository.config.model.PollDataLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollDataLogRepository extends JpaRepository<PollDataLog, Integer> {
}
