package gov.cdc.nnddatapollservice.rdb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Condition {
    private Long conditionKey;
    private String conditionCd;
    private String conditionDesc;
    private String conditionShortNm;
    private String conditionCdEffDt;//timestamp
    private String conditionCdEndDt;//timestamp
    private String nndInd;
    private String diseaseGrpCd;
    private String diseaseGrpDesc;
    private String programAreaCd;
    private String programAreaDesc;
    private String conditionCdSysCdNm;
    private String assigningAuthorityCd;
    private String assigningAuthorityDesc;
    private String conditionCdSysCd;
}