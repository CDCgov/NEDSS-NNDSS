package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "GENERIC_CASE")
public class GenericCase {
    @EmbeddedId
    private GenericCaseId id;

    @MapsId("invAssignedDtKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INV_ASSIGNED_DT_KEY", nullable = false)
    private RdbDate invAssignedDtKey;

    @MapsId("conditionKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONDITION_KEY", nullable = false)
    private Condition conditionKey;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "ILLNESS_DURATION", length = 20)
    private String illnessDuration;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "ILLNESS_DURATION_UNIT", length = 20)
    private String illnessDurationUnit;

    @Column(name = "PATIENT_AGE_AT_ONSET", precision = 18)
    private BigDecimal patientAgeAtOnset;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "PATIENT_AGE_AT_ONSET_UNIT", length = 20)
    private String patientAgeAtOnsetUnit;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "FOOD_HANDLR_IND", length = 50)
    private String foodHandlrInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "DAYCARE_ASSOCIATION_IND", length = 50)
    private String daycareAssociationInd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "DETECTION_METHOD", length = 20)
    private String detectionMethod;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "DETECTION_METHOD_OTHER", length = 100)
    private String detectionMethodOther;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_PREGNANCY_STATUS", length = 50)
    private String patientPregnancyStatus;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PELVIC_INFLAMMATORY_DISS_IND", length = 50)
    private String pelvicInflammatoryDissInd;

    @Column(name = "GEOCODING_LOCATION_KEY")
    private Long geocodingLocationKey;

}