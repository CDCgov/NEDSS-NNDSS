package gov.cdc.nnddataexchangeservice.repository.rdb.model;

import gov.cdc.nnddataexchangeservice.service.model.dto.DataViewConfigDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "data_view_config")
@Data
@AllArgsConstructor
@Builder
public class DataViewConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "query_name", nullable = false, unique = true)
    private String queryName;

    @Column(name = "source_db", nullable = false)
    private String sourceDb;

    @Column(name = "query", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String query;

    @Column(name = "meta_data", columnDefinition = "NVARCHAR(MAX)")
    private String metaData;


    @Column(name = "custom_param_applied")
    private Boolean customParamApplied;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public DataViewConfig() {

    }

    public DataViewConfig(DataViewConfigDto dto) {
        this.queryName = dto.getQueryName();
        this.sourceDb = dto.getSourceDb();
        this.query = dto.getQuery();
        this.metaData = dto.getMetaData();
        this.customParamApplied = dto.getCustomParamApplied();
    }

}
