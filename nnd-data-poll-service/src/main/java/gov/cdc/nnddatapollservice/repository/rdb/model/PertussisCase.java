package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "PERTUSSIS_CASE")
public class PertussisCase {
    @EmbeddedId
    private PertussisCaseId id;

    @MapsId("invAssignedDtKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INV_ASSIGNED_DT_KEY", nullable = false)
    private RdbDate invAssignedDtKey;

    @MapsId("conditionKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONDITION_KEY", nullable = false)
    private Condition conditionKey;

    @Column(name = "COUGH_ONSET_DT")
    private Instant coughOnsetDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PAROXYSMAL_COUGH_IND", length = 50)
    private String paroxysmalCoughInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "WHOOP_IND", length = 50)
    private String whoopInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "POST_TUSSIVE_VOMITING_IND", length = 50)
    private String postTussiveVomitingInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "COUGH_IND", length = 50)
    private String coughInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "APNEA_IND", length = 50)
    private String apneaInd;

    @Column(name = "FINAL_INTERVIEW_DT")
    private Instant finalInterviewDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "COUGH_AT_FINAL_INTERVIEW_IND", length = 50)
    private String coughAtFinalInterviewInd;

    @Column(name = "COUGH_DURATION_DAYS", precision = 18)
    private BigDecimal coughDurationDays;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PNEUMONIA_XRAY_RESULT", length = 50)
    private String pneumoniaXrayResult;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GENERIZED_FOCAL_SEIZURE_IND", length = 50)
    private String generizedFocalSeizureInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "ACUTE_ENCEPHALOPATHY_IND", length = 50)
    private String acuteEncephalopathyInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "ANTIBIOTICS_GIVEN_IND", length = 50)
    private String antibioticsGivenInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "LAB_TESTING_DONE_IND", length = 50)
    private String labTestingDoneInd;

    @Column(name = "CULTURE_DT")
    private Instant cultureDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BORDETELLA_PRT_CULTURE_RESULT", length = 50)
    private String bordetellaPrtCultureResult;

    @Column(name = "SEROLOGY_1_DT")
    private Instant serology1Dt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "SEROLOGY_1_RESULT", length = 50)
    private String serology1Result;

    @Column(name = "SEROLOGY_2_DT")
    private Instant serology2Dt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "SEROLOGY_2_RESULT", length = 50)
    private String serology2Result;

    @Column(name = "PCR_SPECIMEN_DT")
    private Instant pcrSpecimenDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PCR_RESULT", length = 50)
    private String pcrResult;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "EVER_RECEIVED_VACCINE_IND", length = 50)
    private String everReceivedVaccineInd;

    @Column(name = "BEFORE_ILLNESS_LAST_VACCINE_DT")
    private Instant beforeIllnessLastVaccineDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "VACCINE_GIVEN_DOSE_NBR", length = 50)
    private String vaccineGivenDoseNbr;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "LESS_THAN_3_DOSES_REASON", length = 50)
    private String lessThan3DosesReason;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "EPI_LINK_TO_OTHER_CASE_IND", length = 50)
    private String epiLinkToOtherCaseInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "EPI_LINKED_TO_CASE_ID", length = 50)
    private String epiLinkedToCaseId;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "TRANSMISSION_SETTING", length = 20)
    private String transmissionSetting;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "SPREAD_BEYOND_XMISSION_SETTING", length = 50)
    private String spreadBeyondXmissionSetting;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "SPREAD_SETTING_OUTSIDE_HOUSE", length = 50)
    private String spreadSettingOutsideHouse;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "SPREAD_SETTING_OUT_HOUSE_OTHER", length = 2000)
    private String spreadSettingOutHouseOther;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "ONE_OR_MORE_SUSPECT_SRC_IND", length = 50)
    private String oneOrMoreSuspectSrcInd;

    @Column(name = "SUSPECT_INFECTION_SRC_NBR", precision = 18)
    private BigDecimal suspectInfectionSrcNbr;

    @Column(name = "CONTACT_TO_RECEIVE_PROPHYLAXIS", precision = 18)
    private BigDecimal contactToReceiveProphylaxis;

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
    @Column(name = "BORDETELLA_CULTURE_TAKEN_IND", length = 50)
    private String bordetellaCultureTakenInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BORDETELLA_SEROLOGY_1_DONE_IND", length = 50)
    private String bordetellaSerology1DoneInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BORDETELLA_SEROLOGY_2_DONE_IND", length = 50)
    private String bordetellaSerology2DoneInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BORDETELLA_PCR_TAKEN_IND", length = 50)
    private String bordetellaPcrTakenInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "SPECIMEN_TO_CDC_GENOTYPING_IND", length = 50)
    private String specimenToCdcGenotypingInd;

    @Column(name = "SPECIMEN_TO_CDC_GENOTYPING_DT")
    private Instant specimenToCdcGenotypingDt;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "SPECIMENTO_CDC_GENOTYPING_TYPE", length = 2000)
    private String specimentoCdcGenotypingType;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "NOT_PERTUSSIS_VACCINED_REASON", length = 50)
    private String notPertussisVaccinedReason;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "DOSES_NBR_2WEEKS_BEFORE_SICK", length = 50)
    private String dosesNbr2weeksBeforeSick;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "SEROLOGY_LAB1_NM", length = 2000)
    private String serologyLab1Nm;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "SEROLOGY_LAB2_NM", length = 2000)
    private String serologyLab2Nm;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PCR_LAB", length = 50)
    private String pcrLab;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "PCR_LAB_NM", length = 2000)
    private String pcrLabNm;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "SEROLOGY_LAB1", length = 50)
    private String serologyLab1;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "SEROLOGY_LAB2", length = 50)
    private String serologyLab2;

    @Column(name = "BIRTH_WEIGHT_IN_GRAMS", precision = 18)
    private BigDecimal birthWeightInGrams;

    @Column(name = "BIRTH_WEIGHT_POUNDS", precision = 18)
    private BigDecimal birthWeightPounds;

    @Column(name = "BIRTH_WEIGHT_OUNCES", precision = 18)
    private BigDecimal birthWeightOunces;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BIRTH_WEIGHT_UNKNOWN", length = 50)
    private String birthWeightUnknown;

    @Column(name = "MOTHERS_AGE", precision = 18)
    private BigDecimal mothersAge;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_LESS_THAN_12MONTHS", length = 50)
    private String patientLessThan12months;

    @Column(name = "GEOCODING_LOCATION_KEY")
    private Long geocodingLocationKey;

}