package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "CASE_COUNT")
public class CaseCount {
    @EmbeddedId
    private CaseCountId id;

    @MapsId("invAssignedDtKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INV_ASSIGNED_DT_KEY", nullable = false)
    private RdbDate invAssignedDtKey;

    @MapsId("conditionKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONDITION_KEY", nullable = false)
    private Condition conditionKey;

    @Column(name = "CASE_COUNT", precision = 18)
    private BigDecimal caseCount;

    @Column(name = "INVESTIGATION_COUNT")
    private Long investigationCount;

    @Column(name = "ADT_HSPTL_KEY")
    private Long adtHsptlKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "INV_START_DT_KEY", nullable = false)
    private Long invStartDtKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "DIAGNOSIS_DT_KEY", nullable = false)
    private Long diagnosisDtKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "INV_RPT_DT_KEY", nullable = false)
    private Long invRptDtKey;

    @Column(name = "GEOCODING_LOCATION_KEY")
    private Long geocodingLocationKey;

}