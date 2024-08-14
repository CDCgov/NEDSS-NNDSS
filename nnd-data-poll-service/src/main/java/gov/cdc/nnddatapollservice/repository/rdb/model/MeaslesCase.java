package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "MEASLES_CASE")
public class MeaslesCase {
    @EmbeddedId
    private MeaslesCaseId id;

    @MapsId("invRptDtKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INV_RPT_DT_KEY", nullable = false)
    private RdbDate invRptDtKey;

    @MapsId("invAssignedDtKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INV_ASSIGNED_DT_KEY", nullable = false)
    private RdbDate invAssignedDtKey;

    @MapsId("conditionKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONDITION_KEY", nullable = false)
    private Condition conditionKey;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_HAVE_RASH_IND", length = 50)
    private String patientHaveRashInd;

    @Column(name = "RASH_ONSET_DT")
    private Instant rashOnsetDt;

    @Column(name = "RASH_LAST_DAY_NBR", precision = 18)
    private BigDecimal rashLastDayNbr;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_HAVE_FEVER_IND", length = 50)
    private String patientHaveFeverInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RASH_GENERALIZED_IND", length = 50)
    private String rashGeneralizedInd;

    @Column(name = "HIGHEST_TEMPERATURE_MEASURED", precision = 18)
    private BigDecimal highestTemperatureMeasured;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "HIGHEST_TEMPERATURE_UNIT", length = 50)
    private String highestTemperatureUnit;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "COUGH_IND", length = 50)
    private String coughInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CROUP_IND", length = 50)
    private String croupInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CORYZA_IND", length = 50)
    private String coryzaInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CONJUNCTIVITIS_IND", length = 50)
    private String conjunctivitisInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OTTIS_MEDIA_IND", length = 50)
    private String ottisMediaInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "DIARRHEA_IND", length = 50)
    private String diarrheaInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PNEUMONIA_IND", length = 50)
    private String pneumoniaInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "ENCEPHALITIS_IND", length = 50)
    private String encephalitisInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "THROMBOCYTOPENIA_IND", length = 50)
    private String thrombocytopeniaInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OTHER_COMPLICATION_IND", length = 50)
    private String otherComplicationInd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_COMPLICATIONS", length = 2000)
    private String otherComplications;

    @Column(name = "IGM_SPECIMEN_TAKEN_DT")
    private Instant igmSpecimenTakenDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IGM_TEST_RESULT", length = 50)
    private String igmTestResult;

    @Column(name = "IGG_ACUTE_SPECIMEN_TAKEN_DT")
    private Instant iggAcuteSpecimenTakenDt;

    @Column(name = "IGG_CONVALESCENT_SPECIMEN_DT")
    private Instant iggConvalescentSpecimenDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IGG_TEST_RESULT", length = 50)
    private String iggTestResult;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OTHER_LAB_TEST_DONE_IND", length = 50)
    private String otherLabTestDoneInd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_LAB_TEST_DESC", length = 2000)
    private String otherLabTestDesc;

    @Column(name = "OTHER_LAB_TEST_DT")
    private Instant otherLabTestDt;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_LAB_TEST_RESULT", length = 2000)
    private String otherLabTestResult;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "SPECIMEN_TO_CDC_GENOTYPING_IND", length = 50)
    private String specimenToCdcGenotypingInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CONTAIN_MEASLES_VACC_RECEIVD", length = 50)
    private String containMeaslesVaccReceivd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "NO_MEASLES_VACC_REASON", length = 50)
    private String noMeaslesVaccReason;

    @Column(name = "NBR_DOSE_RECEIVED_PRIOR_1BDAY", precision = 18)
    private BigDecimal nbrDoseReceivedPrior1bday;

    @Column(name = "NBR_DOSE_RECEIVED_SINCE_1BDAY", precision = 18)
    private BigDecimal nbrDoseReceivedSince1bday;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "TRANSMISSION_SETTING", length = 50)
    private String transmissionSetting;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "AGE_AND_SETTING_VERIFIED_IND", length = 50)
    private String ageAndSettingVerifiedInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RESIDE_IN_USA_IND", length = 50)
    private String patientResideInUsaInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "EPI_LINKED_TOANOTHER_CASE_IND", length = 50)
    private String epiLinkedToanotherCaseInd;

    @Column(name = "FEVER_ONSET_DT")
    private Instant feverOnsetDt;

    @Column(name = "GENOTYPING_DT")
    private Instant genotypingDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IGM_TESTING_PERFORMED_IND", length = 50)
    private String igmTestingPerformedInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IGG_TESTING_PERFORMED_IND", length = 50)
    private String iggTestingPerformedInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RASH_OCCUR_IN_18DAYS_ENTER_USA", length = 50)
    private String rashOccurIn18daysEnterUsa;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "SRC_OF_INFECTION", length = 2000)
    private String srcOfInfection;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MEASLES_SPECIMEN_TYPE", length = 2000)
    private String measlesSpecimenType;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BEFORE_B_DAY_VACCINE_REASON", length = 50)
    private String beforeBDayVaccineReason;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "AFTER_B_DAY_VACCINE_REASON", length = 50)
    private String afterBDayVaccineReason;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "LAB_MEASLES_TEST_DONE_IND", length = 50)
    private String labMeaslesTestDoneInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "HEPATITIS_IND", length = 50)
    private String hepatitisInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CASE_TRACEABLE_IND", length = 50)
    private String caseTraceableInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GENOTYPE_SEQUENCED_MEASLES", length = 50)
    private String genotypeSequencedMeasles;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GENOTYPE_ID_MEASLES", length = 50)
    private String genotypeIdMeasles;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GENOTYPE_OTHER_ID_MEASLES", length = 50)
    private String genotypeOtherIdMeasles;

    @Column(name = "GEOCODING_LOCATION_KEY")
    private Long geocodingLocationKey;

}