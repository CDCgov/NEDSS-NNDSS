package gov.cdc.nnddataexchangeservice.repository.rdb;

import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataSyncConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSyncConfigRepository extends JpaRepository<DataSyncConfig, String> {
    List<DataSyncConfig> findByTableNameAndSourceDb(String tableName, String sourceDb);

    List<DataSyncConfig> findByTableName(String tableName);

    List<DataSyncConfig> findBySourceDb(String sourceDbName);
}
