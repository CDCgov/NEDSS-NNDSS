package gov.cdc.nnddatapollservice.service.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableMetaDataDto {
    private String columnName;
    private String dataType;
    private Integer characterMaximumLength;
    private String isNullable;
    private String columnDefault;
}
