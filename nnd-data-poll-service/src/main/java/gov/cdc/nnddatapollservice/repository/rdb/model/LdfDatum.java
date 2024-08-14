package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LDF_DATA")
public class LdfDatum {
    @Id
    @Column(name = "LDF_DATA_KEY", nullable = false)
    private Long id;

    @Column(name = "LDF_GROUP_KEY")
    private Long ldfGroupKey;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "LDF_COLUMN_TYPE", length = 300)
    private String ldfColumnType;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "CONDITION_CD", length = 10)
    private String conditionCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "CONDITION_DESC_TXT", length = 100)
    private String conditionDescTxt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CDC_NATIONAL_ID", length = 50)
    private String cdcNationalId;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "CLASS_CD", length = 20)
    private String classCd;

    @jakarta.validation.constraints.Size(max = 256)
    @Column(name = "CODE_SET_NM", length = 256)
    private String codeSetNm;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BUSINESS_OBJ_NM", length = 50)
    private String businessObjNm;

    @Column(name = "DISPLAY_ORDER_NUMBER")
    private Integer displayOrderNumber;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "FIELD_SIZE", length = 10)
    private String fieldSize;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LDF_VALUE", length = 2000)
    private String ldfValue;

    @Column(name = "IMPORT_VERSION_NBR")
    private Long importVersionNbr;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "LABEL_TXT", length = 300)
    private String labelTxt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "LDF_OID", length = 50)
    private String ldfOid;

    @jakarta.validation.constraints.Size(max = 1)
    @Column(name = "NND_IND", length = 1)
    private String nndInd;

    @jakarta.validation.constraints.Size(max = 8)
    @jakarta.validation.constraints.NotNull
    @Column(name = "RECORD_STATUS_CD", nullable = false, length = 8)
    private String recordStatusCd;

}