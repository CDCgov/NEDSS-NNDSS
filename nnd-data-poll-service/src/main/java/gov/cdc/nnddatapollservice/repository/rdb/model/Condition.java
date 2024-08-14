package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "CONDITION")
public class Condition {
    @Id
    @Column(name = "CONDITION_KEY", nullable = false)
    private Long id;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CONDITION_CD", length = 50)
    private String conditionCd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "CONDITION_DESC", length = 300)
    private String conditionDesc;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CONDITION_SHORT_NM", length = 50)
    private String conditionShortNm;

    @Column(name = "CONDITION_CD_EFF_DT")
    private Instant conditionCdEffDt;

    @Column(name = "CONDITION_CD_END_DT")
    private Instant conditionCdEndDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "NND_IND", length = 50)
    private String nndInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "DISEASE_GRP_CD", length = 50)
    private String diseaseGrpCd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "DISEASE_GRP_DESC", length = 50)
    private String diseaseGrpDesc;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "PROGRAM_AREA_CD", length = 20)
    private String programAreaCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "PROGRAM_AREA_DESC", length = 100)
    private String programAreaDesc;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "CONDITION_CD_SYS_CD_NM", length = 100)
    private String conditionCdSysCdNm;

    @jakarta.validation.constraints.Size(max = 199)
    @Column(name = "ASSIGNING_AUTHORITY_CD", length = 199)
    private String assigningAuthorityCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "ASSIGNING_AUTHORITY_DESC", length = 100)
    private String assigningAuthorityDesc;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CONDITION_CD_SYS_CD", length = 50)
    private String conditionCdSysCd;

}