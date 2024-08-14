package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "BMIRD_CASE")
public class BmirdCase {
    @EmbeddedId
    private BmirdCaseId id;

    @MapsId("invAssignedDtKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INV_ASSIGNED_DT_KEY", nullable = false)
    private RdbDate invAssignedDtKey;

    @MapsId("conditionKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONDITION_KEY", nullable = false)
    private Condition conditionKey;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "TRANSFERED_IND", length = 50)
    private String transferedInd;

    @Column(name = "BIRTH_WEIGHT_IN_GRAMS", precision = 18)
    private BigDecimal birthWeightInGrams;

    @Column(name = "BIRTH_WEIGHT_POUNDS", precision = 18)
    private BigDecimal birthWeightPounds;

    @Column(name = "WEIGHT_IN_POUNDS", precision = 18)
    private BigDecimal weightInPounds;

    @Column(name = "WEIGHT_IN_OUNCES", precision = 18)
    private BigDecimal weightInOunces;

    @Column(name = "WEIGHT_IN_KILOGRAM", precision = 18)
    private BigDecimal weightInKilogram;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "WEIGHT_UNKNOWN", length = 50)
    private String weightUnknown;

    @Column(name = "HEIGHT_IN_FEET", precision = 18)
    private BigDecimal heightInFeet;

    @Column(name = "HEIGHT_IN_INCHES", precision = 18)
    private BigDecimal heightInInches;

    @Column(name = "HEIGHT_IN_CENTIMETERS", precision = 18)
    private BigDecimal heightInCentimeters;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "HEIGHT_UNKNOWN", length = 50)
    private String heightUnknown;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OTH_STREP_PNEUMO1_CULT_SITES", length = 50)
    private String othStrepPneumo1CultSites;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OTH_STREP_PNEUMO2_CULT_SITES", length = 50)
    private String othStrepPneumo2CultSites;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IHC_SPECIMEN_1", length = 50)
    private String ihcSpecimen1;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IHC_SPECIMEN_2", length = 50)
    private String ihcSpecimen2;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IHC_SPECIMEN_3", length = 50)
    private String ihcSpecimen3;

    @Column(name = "SAMPLE_COLLECTION_DT")
    private Instant sampleCollectionDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CONJ_MENING_VACC", length = 50)
    private String conjMeningVacc;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "TREATMENT_HOSPITAL_NM", length = 100)
    private String treatmentHospitalNm;

    @Column(name = "TREATMENT_HOSPITAL_KEY")
    private Long treatmentHospitalKey;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OTH_TYPE_OF_INSURANCE", length = 50)
    private String othTypeOfInsurance;

    @Column(name = "BIRTH_WEIGHT_OUNCES", precision = 18)
    private BigDecimal birthWeightOunces;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PREGNANT_IND", length = 50)
    private String pregnantInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OUTCOME_OF_FETUS", length = 50)
    private String outcomeOfFetus;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "UNDER_1_MONTH_IND", length = 50)
    private String under1MonthInd;

    @Column(name = "GESTATIONAL_AGE", precision = 18)
    private BigDecimal gestationalAge;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BACTERIAL_SPECIES_ISOLATED", length = 50)
    private String bacterialSpeciesIsolated;

    @Column(name = "FIRST_POSITIVE_CULTURE_DT")
    private Instant firstPositiveCultureDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "UNDERLYING_CONDITION_IND", length = 50)
    private String underlyingConditionInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_YR_IN_COLLEGE", length = 50)
    private String patientYrInCollege;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CULTURE_SEROTYPE", length = 50)
    private String cultureSerotype;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_STATUS_IN_COLLEGE", length = 50)
    private String patientStatusInCollege;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_CURR_LIVING_SITUATION", length = 50)
    private String patientCurrLivingSituation;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "HIB_VACC_RECEIVED_IND", length = 50)
    private String hibVaccReceivedInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CULTURE_SEROGROUP", length = 50)
    private String cultureSerogroup;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "ATTENDING_COLLEGE_IND", length = 50)
    private String attendingCollegeInd;

    @Column(name = "OXACILLIN_ZONE_SIZE", precision = 18)
    private BigDecimal oxacillinZoneSize;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OXACILLIN_INTERPRETATION", length = 50)
    private String oxacillinInterpretation;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PNEUVACC_RECEIVED_IND", length = 50)
    private String pneuvaccReceivedInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PNEUCONJ_RECEIVED_IND", length = 50)
    private String pneuconjReceivedInd;

    @Column(name = "FIRST_ADDITIONAL_SPECIMEN_DT")
    private Instant firstAdditionalSpecimenDt;

    @Column(name = "SECOND_ADDITIONAL_SPECIMEN_DT")
    private Instant secondAdditionalSpecimenDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_HAD_SURGERY_IND", length = 50)
    private String patientHadSurgeryInd;

    @Column(name = "SURGERY_DT")
    private Instant surgeryDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_HAVE_BABY_IND", length = 50)
    private String patientHaveBabyInd;

    @Column(name = "BABY_DELIVERY_DT")
    private Instant babyDeliveryDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IDENT_THRU_AUDIT_IND", length = 50)
    private String identThruAuditInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "SAME_PATHOGEN_RECURRENT_IND", length = 50)
    private String samePathogenRecurrentInd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_SPECIES_ISOLATE_SITE", length = 2000)
    private String otherSpeciesIsolateSite;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_CASE_IDENT_METHOD", length = 2000)
    private String otherCaseIdentMethod;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_HOUSING_OPTION", length = 2000)
    private String otherHousingOption;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "PATIENT_CURR_ATTEND_SCHOOL_NM", length = 100)
    private String patientCurrAttendSchoolNm;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "POLYSAC_MENINGOC_VACC_IND", length = 50)
    private String polysacMeningocVaccInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "FAMILY_MEDICAL_INSURANCE_TYPE", length = 50)
    private String familyMedicalInsuranceType;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "HIB_CONTACT_IN_LAST_2_MON_IND", length = 50)
    private String hibContactInLast2MonInd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "TYPE_HIB_CONTACT_IN_LAST_2_MON", length = 2000)
    private String typeHibContactInLast2Mon;

    @Column(name = "PRETERM_BIRTH_WK_NBR", precision = 18)
    private BigDecimal pretermBirthWkNbr;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "IMMUNOSUPRESSION_HIV_STATUS", length = 2000)
    private String immunosupressionHivStatus;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "ACUTE_SERUM_AVAILABLE_IND", length = 50)
    private String acuteSerumAvailableInd;

    @Column(name = "ACUTE_SERUM_AVAILABLE_DT")
    private Instant acuteSerumAvailableDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CONVALESNT_SERUM_AVAILABLE_IND", length = 50)
    private String convalesntSerumAvailableInd;

    @Column(name = "CONVALESNT_SERUM_AVAILABLE_DT")
    private Instant convalesntSerumAvailableDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BIRTH_OUTSIDE_HSPTL_IND", length = 50)
    private String birthOutsideHsptlInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BIRTH_OUTSIDE_HSPTL_LOCATION", length = 50)
    private String birthOutsideHsptlLocation;

    @Column(name = "BABY_HSPTL_DISCHARGE_DTTIME")
    private Instant babyHsptlDischargeDttime;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BABY_SAME_HSPTL_READMIT_IND", length = 50)
    private String babySameHsptlReadmitInd;

    @Column(name = "BABY_SAME_HSPTL_READMIT_DTTIME")
    private Instant babySameHsptlReadmitDttime;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "FRM_HOME_TO_DIF_HSPTL_IND", length = 50)
    private String frmHomeToDifHsptlInd;

    @Column(name = "FRM_HOME_TO_DIF_HSPTL_DTTIME")
    private Instant frmHomeToDifHsptlDttime;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "MOTHER_LAST_NM", length = 50)
    private String motherLastNm;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "MOTHER_FIRST_NM", length = 50)
    private String motherFirstNm;

    @Column(name = "MOTHER_HOSPTL_ADMISSION_DTTIME")
    private Instant motherHosptlAdmissionDttime;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MOTHER_PATIENT_CHART_NBR", length = 2000)
    private String motherPatientChartNbr;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "MOTHER_PENICILLIN_ALLERGY_IND", length = 50)
    private String motherPenicillinAllergyInd;

    @Column(name = "MEMBRANE_RUPTURE_DTTIME")
    private Instant membraneRuptureDttime;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "MEMBRANE_RUPTURE_GE_18HRS_IND", length = 50)
    private String membraneRuptureGe18hrsInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RUPTURE_BEFORE_LABOR_ONSET", length = 50)
    private String ruptureBeforeLaborOnset;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "MEMBRANE_RUPTURE_TYPE", length = 50)
    private String membraneRuptureType;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "DELIVERY_TYPE", length = 50)
    private String deliveryType;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "MOTHER_INTRAPARTUM_FEVER_IND", length = 50)
    private String motherIntrapartumFeverInd;

    @Column(name = "FIRST_INTRAPARTUM_FEVER_DTTIME")
    private Instant firstIntrapartumFeverDttime;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RECEIVED_PRENATAL_CARE_IND", length = 50)
    private String receivedPrenatalCareInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PRENATAL_CARE_IN_LABOR_CHART", length = 50)
    private String prenatalCareInLaborChart;

    @Column(name = "PRENATAL_CARE_VISIT_NBR", precision = 18)
    private BigDecimal prenatalCareVisitNbr;

    @Column(name = "FIRST_PRENATAL_CARE_VISIT_DT")
    private Instant firstPrenatalCareVisitDt;

    @Column(name = "LAST_PRENATAL_CARE_VISIT_DT")
    private Instant lastPrenatalCareVisitDt;

    @Column(name = "LAST_PRENATAL_CARE_VISIT_EGA", precision = 18)
    private BigDecimal lastPrenatalCareVisitEga;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GBS_BACTERIURIA_IN_PREGNANCY", length = 50)
    private String gbsBacteriuriaInPregnancy;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PREVIOUS_BIRTH_WITH_GBS_IND", length = 50)
    private String previousBirthWithGbsInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GBS_CULTURED_BEFORE_ADMISSION", length = 50)
    private String gbsCulturedBeforeAdmission;

    @Column(name = "GBS_1ST_CULTURE_DT")
    private Instant gbs1stCultureDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GBS_1ST_CULTURE_POSITIVE_IND", length = 50)
    private String gbs1stCulturePositiveInd;

    @Column(name = "GBS_2ND_CULTURE_DT")
    private Instant gbs2ndCultureDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GBS_2ND_CULTURE_POSITIVE_IND", length = 50)
    private String gbs2ndCulturePositiveInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GBS_AFTER_ADM_BEFORE_DELIVERY", length = 50)
    private String gbsAfterAdmBeforeDelivery;

    @Column(name = "AFTER_ADM_GBS_CULTURE_DT")
    private Instant afterAdmGbsCultureDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GBS_CULTURE_DELIVERY_AVAILABLE", length = 50)
    private String gbsCultureDeliveryAvailable;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "INTRAPARTUM_ANTIBIOTICS_GIVEN", length = 50)
    private String intrapartumAntibioticsGiven;

    @Column(name = "FIRST_ANTIBIOTICS_GIVEN_DTTIME")
    private Instant firstAntibioticsGivenDttime;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "INTRAPARTUMANTIBIOTICSINTERVAL", length = 20)
    private String intrapartumantibioticsinterval;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "INTRAPARTUM_ANTIBIOTICS_REASON", length = 50)
    private String intrapartumAntibioticsReason;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "BABY_BIRTH_TIME", length = 20)
    private String babyBirthTime;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "NEISERRIA_2NDARY_CASE_IND", length = 50)
    private String neiserria2ndaryCaseInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "NEISERRIA_2ND_CASE_CONTRACT", length = 50)
    private String neiserria2ndCaseContract;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_2NDARY_CASE_TYPE", length = 2000)
    private String other2ndaryCaseType;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "NEISERRIA_RESIST_TO_RIFAMPIN", length = 50)
    private String neiserriaResistToRifampin;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "NEISERRIA_RESIST_TO_SULFA", length = 50)
    private String neiserriaResistToSulfa;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "FIRST_HSPTL_DISCHARGE_TIME", length = 2000)
    private String firstHsptlDischargeTime;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "FIRST_HSPTL_READMISSION_TIME", length = 2000)
    private String firstHsptlReadmissionTime;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "SECOND_HSPTL_ADMISSION_TIME", length = 2000)
    private String secondHsptlAdmissionTime;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "ABCCASE", length = 50)
    private String abccase;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "HSPTL_MATERNAL_ADMISSION_TIME", length = 2000)
    private String hsptlMaternalAdmissionTime;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MEMBRANE_RUPTURE_TIME", length = 2000)
    private String membraneRuptureTime;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "INTRAPARTUM_FEVER_RECORD_TIME", length = 2000)
    private String intrapartumFeverRecordTime;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ANTIBIOTICS_1ST_ADMIN_TIME", length = 2000)
    private String antibiotics1stAdminTime;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_PRIOR_ILLNESS", length = 2000)
    private String otherPriorIllness;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "OTHER_MALIGNANCY", length = 2000)
    private String otherMalignancy;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ORGAN_TRANSPLANT", length = 2000)
    private String organTransplant;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "DAYCARE_IND", length = 50)
    private String daycareInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "NURSING_HOME_IND", length = 50)
    private String nursingHomeInd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "TYPES_OF_OTHER_INFECTION", length = 2000)
    private String typesOfOtherInfection;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "BACTERIAL_OTHER_SPECIED", length = 2000)
    private String bacterialOtherSpecied;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "STERILE_SITE_OTHER", length = 2000)
    private String sterileSiteOther;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "UNDERLYING_CONDITIONS_OTHER", length = 2000)
    private String underlyingConditionsOther;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "CULTURE_SEROGROUP_OTHER", length = 2000)
    private String cultureSerogroupOther;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PERSISTENT_DISEASE_IND", length = 50)
    private String persistentDiseaseInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "GBS_CULTURE_POSITIVE_IND", length = 50)
    private String gbsCulturePositiveInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BACTERIAL_OTHER_ISOLATED", length = 50)
    private String bacterialOtherIsolated;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "FAMILY_MED_INSURANCE_TYPE_OTHE", length = 2000)
    private String familyMedInsuranceTypeOthe;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "PRIOR_STATE_CASE_ID", length = 2000)
    private String priorStateCaseId;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "BIRTH_CNTRY_CD", length = 50)
    private String birthCntryCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "INITIAL_HSPTL_NAME", length = 100)
    private String initialHsptlName;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "BIRTH_HSPTL_NAME", length = 100)
    private String birthHsptlName;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "FROM_HOME_HSPTL_NAME", length = 100)
    private String fromHomeHsptlName;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "CULTURE_IDENT_ORG_NAME", length = 100)
    private String cultureIdentOrgName;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "TRANSFER_FRM_HSPTL_NAME", length = 100)
    private String transferFrmHsptlName;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CASE_REPORT_STATUS", length = 50)
    private String caseReportStatus;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "TRANSFER_FRM_HSPTL_ID", length = 100)
    private String transferFrmHsptlId;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "BIRTH_HSPTL_ID", length = 100)
    private String birthHsptlId;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "DIF_HSPTL_ID", length = 100)
    private String difHsptlId;

    @jakarta.validation.constraints.Size(max = 199)
    @Column(name = "ABC_STATE_CASE_ID", length = 199)
    private String abcStateCaseId;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "INV_PATIENT_CHART_NBR", length = 2000)
    private String invPatientChartNbr;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "OTHSPEC1", length = 100)
    private String othspec1;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "OTHSPEC2", length = 100)
    private String othspec2;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "INTBODYSITE", length = 100)
    private String intbodysite;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "OTHILL2", length = 100)
    private String othill2;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "OTHILL3", length = 100)
    private String othill3;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "OTHNONSTER", length = 100)
    private String othnonster;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "OTHSEROTYPE", length = 100)
    private String othserotype;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "HINFAGE", length = 100)
    private String hinfage;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "ABCSINVLN", length = 100)
    private String abcsinvln;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "ABCSINVFN", length = 100)
    private String abcsinvfn;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "ABCSINVEMAIL", length = 100)
    private String abcsinvemail;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "ABCSINVTELE", length = 100)
    private String abcsinvtele;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "ABCSINVEXT", length = 100)
    private String abcsinvext;

    @Column(name = "GEOCODING_LOCATION_KEY")
    private Long geocodingLocationKey;

}