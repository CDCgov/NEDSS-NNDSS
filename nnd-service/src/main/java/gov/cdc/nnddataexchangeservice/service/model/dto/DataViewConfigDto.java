package gov.cdc.nnddataexchangeservice.service.model.dto;

import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataViewConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataViewConfigDto {
    private Long id;
    private String queryName;
    private String sourceDb;
    private String query;
    private String metaData;
    private Boolean customParamApplied;
    private Boolean crossDbApplied;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static DataViewConfigDto fromEntity(DataViewConfig entity) {
        return DataViewConfigDto.builder()
                .id(entity.getId())
                .queryName(entity.getQueryName())
                .sourceDb(entity.getSourceDb())
                .query(entity.getQuery())
                .metaData(entity.getMetaData())
                .customParamApplied(entity.getCustomParamApplied())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .crossDbApplied(entity.getCrossDbApplied())
                .build();
    }
}
