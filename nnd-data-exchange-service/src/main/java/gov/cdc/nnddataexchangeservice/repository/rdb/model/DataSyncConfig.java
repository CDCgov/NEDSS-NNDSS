package gov.cdc.nnddataexchangeservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "data_sync_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataSyncConfig {
    @Id
    @Column(name = "table_name")
    private String tableName;

    @Column(name = "source_db")
    private String sourceDb;

    @Column(name = "query")
    private String query;

    @Column(name = "query_with_limit")
    private String queryWithLimit;


    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
