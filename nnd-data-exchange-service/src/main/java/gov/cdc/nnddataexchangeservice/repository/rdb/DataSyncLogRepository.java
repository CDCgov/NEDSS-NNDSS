package gov.cdc.nnddataexchangeservice.repository.rdb;

import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataSyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSyncLogRepository extends JpaRepository<DataSyncLog, Long> {

}
