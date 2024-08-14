package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "RUBELLA_CASE")
public class RubellaCase {
    @EmbeddedId
    private RubellaCaseId id;

    @MapsId("invAssignedDtKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INV_ASSIGNED_DT_KEY", nullable = false)
    private RdbDate invAssignedDtKey;

    @MapsId("conditionKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONDITION_KEY", nullable = false)
    private Condition conditionKey;

    @Column(name = "LENGTH_OF_TIME_IN_US", precision = 18)
    private BigDecimal lengthOfTimeInUs;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "MACULOPAPULAR_RASH_IND", length = 50)
    private String maculopapularRashInd;

    @Column(name = "PATIENT_RASH_ONSET_DT")
    private Instant patientRashOnsetDt;

    @Column(name = "RASH_DURATION_DAYS", precision = 18)
    private BigDecimal rashDurationDays;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_FEVER_IND", length = 50)
    private String patientFeverInd;

    @Column(name = "HIGHEST_MEASURED_TEMPERATURE", precision = 18)
    private BigDecimal highestMeasuredTemperature;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "HIGHEST_TEMPERATUR_UNIT", length = 50)
    private String highestTemperaturUnit;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "ARTHRALGIA_ARTHRITIS_SYMPTOM", length = 50)
    private String arthralgiaArthritisSymptom;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "LYMPHADENOPATHY_IND", length = 50)
    private String lymphadenopathyInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CONJUNCTIVITIS_IND", length = 50)
    private String conjunctivitisInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "ENCEPHALITIS_IND", length = 50)
    private String encephalitisInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "THROMBOCYTOPENIA_IND", length = 50)
    private String thrombocytopeniaInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OTHER_COMPLICATIONS_IND", length = 50)
    private String otherComplicationsInd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_COMPLICATIONS_DESC", length = 2000)
    private String otherComplicationsDesc;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "CAUSE_OF_DEATH", length = 2000)
    private String causeOfDeath;

    @Column(name = "HSPTL_ADMISSION_DT")
    private Instant hsptlAdmissionDt;

    @Column(name = "HSPTL_DISCHARGE_DT")
    private Instant hsptlDischargeDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_LAB_TEST_DONE_IND", length = 50)
    private String rubellaLabTestDoneInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGM_EIA_TEST_IND", length = 50)
    private String rubellaIgmEiaTestInd;

    @Column(name = "RUBELLA_IGM_EIA_TEST_DT")
    private Instant rubellaIgmEiaTestDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGM_EIA_TEST_RESULT", length = 50)
    private String rubellaIgmEiaTestResult;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGM_EIA_CAPTURE_IND", length = 50)
    private String rubellaIgmEiaCaptureInd;

    @Column(name = "RUBELLA_IGM_EIA_CAPTURE_DT")
    private Instant rubellaIgmEiaCaptureDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGM_EIA_CAPTURE_RESULT", length = 50)
    private String rubellaIgmEiaCaptureResult;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OTHER_RUBELLA_IGM_IND", length = 50)
    private String otherRubellaIgmInd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_RUBELLA_IGM_DESC", length = 2000)
    private String otherRubellaIgmDesc;

    @Column(name = "OTHER_RUBELLA_IGM_DT")
    private Instant otherRubellaIgmDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OTHER_RUBELLA_IGM_RESULT", length = 50)
    private String otherRubellaIgmResult;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGG_EIA_ACUTE_IND", length = 50)
    private String rubellaIggEiaAcuteInd;

    @Column(name = "RUBELLA_IGG_EIA_ACUTE_DT")
    private Instant rubellaIggEiaAcuteDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGG_EIA_CNVLSNT_IND", length = 50)
    private String rubellaIggEiaCnvlsntInd;

    @Column(name = "RUBELLA_IGG_EIA_CNVLSNT_DT")
    private Instant rubellaIggEiaCnvlsntDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IGG_EIA_ACUTE_CNVLSNT_DIFF", length = 50)
    private String iggEiaAcuteCnvlsntDiff;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "HEMAG_INHIBIT_ACUTE_IND", length = 50)
    private String hemagInhibitAcuteInd;

    @Column(name = "HEMAG_INHIBIT_ACUTE_DT")
    private Instant hemagInhibitAcuteDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "HEMAG_INHIBIT_CNVLSNT_IND", length = 50)
    private String hemagInhibitCnvlsntInd;

    @Column(name = "HEMAG_INHIBIT_CNVLSNT_DT")
    private Instant hemagInhibitCnvlsntDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "HEMAGINHIBIT_ACUTECNVLSNT_DIFF", length = 50)
    private String hemaginhibitAcutecnvlsntDiff;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CMPLMNT_FIXATION_ACUTE_IND", length = 50)
    private String cmplmntFixationAcuteInd;

    @Column(name = "CMPLMNT_FIXATION_ACUTE_DT")
    private Instant cmplmntFixationAcuteDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CMPLMNT_FIXATION_CNVLSNT_IND", length = 50)
    private String cmplmntFixationCnvlsntInd;

    @Column(name = "CMPLMNT_FIXATION_CNVLSNT_DT")
    private Instant cmplmntFixationCnvlsntDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CMPLMNT_FIX_ACUTE_CNVLSNT_DIFF", length = 50)
    private String cmplmntFixAcuteCnvlsntDiff;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGG_OTHER_TEST1_IND", length = 50)
    private String rubellaIggOtherTest1Ind;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RUBELLA_IGG_OTHER_TEST1_DESC", length = 2000)
    private String rubellaIggOtherTest1Desc;

    @Column(name = "RUBELLA_IGG_OTHER_TEST1_DT")
    private Instant rubellaIggOtherTest1Dt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGG_OTHER_TEST1_RESULT", length = 50)
    private String rubellaIggOtherTest1Result;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGG_OTHER_TEST2_IND", length = 50)
    private String rubellaIggOtherTest2Ind;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RUBELLA_IGG_OTHER_TEST2_DESC", length = 2000)
    private String rubellaIggOtherTest2Desc;

    @Column(name = "RUBELLA_IGG_OTHER_TEST2_DT")
    private Instant rubellaIggOtherTest2Dt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGG_OTHER_TEST2_RESULT", length = 50)
    private String rubellaIggOtherTest2Result;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGG_OTHER_TEST3_IND", length = 50)
    private String rubellaIggOtherTest3Ind;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RUBELLA_IGG_OTHER_TEST3_DESC", length = 2000)
    private String rubellaIggOtherTest3Desc;

    @Column(name = "RUBELLA_IGG_OTHER_TEST_3_DT")
    private Instant rubellaIggOtherTest3Dt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_IGG_OTHER_TEST3_RESULT", length = 50)
    private String rubellaIggOtherTest3Result;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "VIRUS_ISOLATION_PERFORMED_IND", length = 50)
    private String virusIsolationPerformedInd;

    @Column(name = "VIRUS_ISOLATION_PERFORMED_DT")
    private Instant virusIsolationPerformedDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "VIRUS_ISOLATION_PERFORMED_SRC", length = 50)
    private String virusIsolationPerformedSrc;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "VIRUS_ISOLATION_OTHER_SRC", length = 2000)
    private String virusIsolationOtherSrc;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RT_PCR_PERFORMED_IND", length = 50)
    private String rtPcrPerformedInd;

    @Column(name = "RT_PCR_DT")
    private Instant rtPcrDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RT_PCR_SRC", length = 50)
    private String rtPcrSrc;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RT_PCR_RESULT", length = 50)
    private String rtPcrResult;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RT_PCR_OTHER_SRC", length = 2000)
    private String rtPcrOtherSrc;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "LATEX_AGGLUTINATION_TESTED_IND", length = 50)
    private String latexAgglutinationTestedInd;

    @Column(name = "LATEX_AGGLUTINATION_TESTED_DT")
    private Instant latexAgglutinationTestedDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "LATEX_AGGLUTINATION_TESTRESULT", length = 50)
    private String latexAgglutinationTestresult;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IMMUNOFLUORESCENT_ASSAY_IND", length = 50)
    private String immunofluorescentAssayInd;

    @Column(name = "IMMUNOFLUORESCENT_ASSAY_DT")
    private Instant immunofluorescentAssayDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IMMUNOFLUORESCENT_ASSAY_SRC", length = 50)
    private String immunofluorescentAssaySrc;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IMMUNOFLUORESCENT_ASSAY_RESULT", length = 50)
    private String immunofluorescentAssayResult;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "IMUNOFLRESNT_ASSAY_OTHER_SRC", length = 2000)
    private String imunoflresntAssayOtherSrc;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OTHER_RUBELLA_TEST_DONE_IND", length = 50)
    private String otherRubellaTestDoneInd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_RUBELLA_TEST_DESC", length = 2000)
    private String otherRubellaTestDesc;

    @Column(name = "OTHER_RUBELLA_TEST_DT")
    private Instant otherRubellaTestDt;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_RUBELLA_TEST_RESULT", length = 2000)
    private String otherRubellaTestResult;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "SPECIMEN_TO_CDC_GENOTYPING_IND", length = 50)
    private String specimenToCdcGenotypingInd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "SPECIMENTO_CDC_GENOTYPING_TYPE", length = 20)
    private String specimentoCdcGenotypingType;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "GENOTYPING_SPECIMEN_OTHER_TYPE", length = 2000)
    private String genotypingSpecimenOtherType;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_VACCINE_RECEIVED_IND", length = 50)
    private String rubellaVaccineReceivedInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_VACCINED_NEVER_REASON", length = 50)
    private String rubellaVaccinedNeverReason;

    @Column(name = "ON_AFTER_1ST_DOB_DOSES_NBR", precision = 18)
    private BigDecimal onAfter1stDobDosesNbr;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "TRANSMISSION_SETTING", length = 20)
    private String transmissionSetting;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "EPI_LINKED_TO_ANOTHER_CASE_IND", length = 50)
    private String epiLinkedToAnotherCaseInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PREGNANCY_IND", length = 50)
    private String pregnancyInd;

    @Column(name = "PREGNANCY_DELIVERY_EXPECTED_DT")
    private Instant pregnancyDeliveryExpectedDt;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EXPECTED_DELIVERY_PLACE", length = 2000)
    private String expectedDeliveryPlace;

    @Column(name = "GESTATION_WK_NBR_AT_RUBELLA", precision = 18)
    private BigDecimal gestationWkNbrAtRubella;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GESTATION_TRIMESTER_ST_RUBELLA", length = 50)
    private String gestationTrimesterStRubella;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PREVIOUS_RUBELLA_IMMUNITY_DOC", length = 50)
    private String previousRubellaImmunityDoc;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PREVIOUSIMMUNITY_TESTINGRESULT", length = 50)
    private String previousimmunityTestingresult;

    @Column(name = "PREVIOUS_IMMUNITY_TESTING_YR", precision = 18)
    private BigDecimal previousImmunityTestingYr;

    @Column(name = "WOMAN_AGE_AT_IMMUNITY_TESTING", precision = 18)
    private BigDecimal womanAgeAtImmunityTesting;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_PRIOR_TO_PREGNANCY_IND", length = 50)
    private String rubellaPriorToPregnancyInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "SEROLOGICALLYCONFIRMED_RUBELLA", length = 50)
    private String serologicallyconfirmedRubella;

    @Column(name = "PREVIOUS_DISS_YR", precision = 18)
    private BigDecimal previousDissYr;

    @Column(name = "PREVIOUS_DISS_AGE", precision = 18)
    private BigDecimal previousDissAge;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PREGNANCY_CURRENT_OUTCOME", length = 50)
    private String pregnancyCurrentOutcome;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "LIVE_BIRTH_OUTCOME", length = 50)
    private String liveBirthOutcome;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "NON_LIVING_BIRTH_OUTCOME", length = 50)
    private String nonLivingBirthOutcome;

    @Column(name = "PREGNANCY_CESSATION_FETUS_WK", precision = 18)
    private BigDecimal pregnancyCessationFetusWk;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "NON_LIVING_BIRTH_AUTOPSY_STUDY", length = 50)
    private String nonLivingBirthAutopsyStudy;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "AUTOPSY_PATHOLOGY_STUDY_RESULT", length = 2000)
    private String autopsyPathologyStudyResult;

    @Column(name = "RUBELLA_GENOTYPING_DT")
    private Instant rubellaGenotypingDt;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EIA_ACUTE_TEST_RESULT_VALUE", length = 2000)
    private String eiaAcuteTestResultValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EIA_CNVLSNT_TEST_RESULT_VALUE", length = 2000)
    private String eiaCnvlsntTestResultValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "HEMAG_INHIBIT_ACUTE_VALUE", length = 2000)
    private String hemagInhibitAcuteValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "HEMAG_INHIBIT_CNVLSNT_VALUE", length = 2000)
    private String hemagInhibitCnvlsntValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "CMPLMNT_FIXATION_ACUTE_VALUE", length = 2000)
    private String cmplmntFixationAcuteValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "CMPLMNT_FIXATION_CNVLSNT_VALUE", length = 2000)
    private String cmplmntFixationCnvlsntValue;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "ARTHRALGIA_ARTHRITIS_COMPLICAT", length = 50)
    private String arthralgiaArthritisComplicat;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "IGM_EIA_1ST_TEST_RESULT_VALUE", length = 2000)
    private String igmEia1stTestResultValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "IGM_EIA_2ND_TEST_RESULT_VALUE", length = 2000)
    private String igmEia2ndTestResultValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_IGM_TEST_RESULT_VALUE", length = 2000)
    private String otherIgmTestResultValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "IGG_OTHER_TEST_1_RESULT_VALUE", length = 2000)
    private String iggOtherTest1ResultValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "IGG_OTHER_TEST_2_RESULT_VALUE", length = 2000)
    private String iggOtherTest2ResultValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RT_PCR_TEST_RESULT_VALUE", length = 2000)
    private String rtPcrTestResultValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LATEX_AGG_TEST_RESULT_VALUE", length = 2000)
    private String latexAggTestResultValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ASSAY_TEST_RESULT_VALUE", length = 2000)
    private String assayTestResultValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_RUBELLA_TESTRESULT_VALUE", length = 2000)
    private String otherRubellaTestresultValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "INFECTION_SRC", length = 2000)
    private String infectionSrc;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RASH_ONSET_ENTERING_USA", length = 50)
    private String rashOnsetEnteringUsa;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IGM_EIA_1ST_METHOD_USED", length = 50)
    private String igmEia1stMethodUsed;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IGM_EIA_2ND_METHOD_USED", length = 50)
    private String igmEia2ndMethodUsed;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "IGG_OTHER_TEST_3_RESULT_VALUE", length = 2000)
    private String iggOtherTest3ResultValue;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "VIRUS_ISOLATION_RESULT", length = 50)
    private String virusIsolationResult;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BIRTH_COUNTRY", length = 50)
    private String birthCountry;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUBELLA_CASE_TRACEABLE_IND", length = 50)
    private String rubellaCaseTraceableInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GENOTYPE_SEQUENCED_RUBELLA", length = 50)
    private String genotypeSequencedRubella;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GENOTYPE_ID_RUBELLA", length = 50)
    private String genotypeIdRubella;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GENOTYPE_OTHER_ID_RUBELLA", length = 50)
    private String genotypeOtherIdRubella;

    @Column(name = "GEOCODING_LOCATION_KEY")
    private Long geocodingLocationKey;

}