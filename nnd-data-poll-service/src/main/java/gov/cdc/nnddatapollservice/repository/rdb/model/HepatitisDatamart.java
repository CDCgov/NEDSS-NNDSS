package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "HEPATITIS_DATAMART")
public class HepatitisDatamart {
    @Column(name = "INIT_NND_NOT_DT")
    private Instant initNndNotDt;

    @Column(name = "CASE_RPT_MMWR_WEEK", precision = 18)
    private BigDecimal caseRptMmwrWeek;

    @Column(name = "CASE_RPT_MMWR_YEAR", precision = 18)
    private BigDecimal caseRptMmwrYear;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_D_INFECTION_IND", length = 300)
    private String hepDInfectionInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_MEDS_RECVD_IND", length = 300)
    private String hepMedsRecvdInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_C_TOTAL_ANTIBODY", length = 300)
    private String hepCTotalAntibody;

    @Column(name = "DIAGNOSIS_DT")
    private Instant diagnosisDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "DIE_FRM_THIS_ILLNESS_IND", length = 300)
    private String dieFrmThisIllnessInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "DISEASE_IMPORTED_IND", length = 300)
    private String diseaseImportedInd;

    @Column(name = "EARLIEST_RPT_TO_CNTY")
    private Instant earliestRptToCnty;

    @Column(name = "EARLIEST_RPT_TO_STATE_DT")
    private Instant earliestRptToStateDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "BINATIONAL_RPTNG_CRIT", length = 300)
    private String binationalRptngCrit;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "CHILDCARE_CASE_IND", length = 300)
    private String childcareCaseInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "CNTRY_USUAL_RESIDENCE", length = 300)
    private String cntryUsualResidence;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "CT_BABYSITTER_IND", length = 300)
    private String ctBabysitterInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "CT_CHILDCARE_IND", length = 300)
    private String ctChildcareInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "CT_HOUSEHOLD_IND", length = 300)
    private String ctHouseholdInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_CONTACT_IND", length = 300)
    private String hepContactInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_CONTACT_EVER_IND", length = 300)
    private String hepContactEverInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "OTHER_CONTACT_IND", length = 300)
    private String otherContactInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "COM_SRC_OUTBREAK_IND", length = 300)
    private String comSrcOutbreakInd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "CONTACT_TYPE_OTH", length = 100)
    private String contactTypeOth;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "CT_PLAYMATE_IND", length = 300)
    private String ctPlaymateInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "SEXUAL_PARTNER_IND", length = 300)
    private String sexualPartnerInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "DNP_HOUSEHOLD_CT_IND", length = 300)
    private String dnpHouseholdCtInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_A_EPLINK_IND", length = 300)
    private String hepAEplinkInd;

    @Column(name = "FEMALE_SEX_PRTNR_NBR", precision = 18)
    private BigDecimal femaleSexPrtnrNbr;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "FOODHNDLR_PRIOR_IND", length = 300)
    private String foodhndlrPriorInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "DNP_EMPLOYEE_IND", length = 300)
    private String dnpEmployeeInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "STREET_DRUG_INJECTED", length = 300)
    private String streetDrugInjected;

    @Column(name = "MALE_SEX_PRTNR_NBR", precision = 18)
    private BigDecimal maleSexPrtnrNbr;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "OUTBREAK_IND", length = 300)
    private String outbreakInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "OBRK_FOODHNDLR_IND", length = 300)
    private String obrkFoodhndlrInd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "FOOD_OBRK_FOOD_ITEM", length = 100)
    private String foodObrkFoodItem;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "OBRK_NOFOODHNDLR_IND", length = 300)
    private String obrkNofoodhndlrInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "OBRK_UNIDENTIFIED_IND", length = 300)
    private String obrkUnidentifiedInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "OBRK_WATERBORNE_IND", length = 300)
    private String obrkWaterborneInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "STREET_DRUG_USED", length = 300)
    private String streetDrugUsed;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "SEX_PREF", length = 300)
    private String sexPref;

    @Column(name = "HSPTL_ADMISSION_DT")
    private Instant hsptlAdmissionDt;

    @Column(name = "HSPTL_DISCHARGE_DT")
    private Instant hsptlDischargeDt;

    @Column(name = "HSPTL_DURATION_DAYS", precision = 18)
    private BigDecimal hsptlDurationDays;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HSPTLIZD_IND", length = 300)
    private String hsptlizdInd;

    @Column(name = "ILLNESS_ONSET_DT")
    private Instant illnessOnsetDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "INV_CASE_STATUS", length = 300)
    private String invCaseStatus;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "INV_COMMENTS", length = 2000)
    private String invComments;

    @jakarta.validation.constraints.Size(max = 25)
    @Column(name = "INV_LOCAL_ID", length = 25)
    private String invLocalId;

    @Column(name = "INV_RPT_DT")
    private Instant invRptDt;

    @Column(name = "INV_START_DT")
    private Instant invStartDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "INVESTIGATION_STATUS", length = 300)
    private String investigationStatus;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "JURISDICTION_NM", length = 300)
    private String jurisdictionNm;

    @Column(name = "ALT_SGPT_RESULT", precision = 18)
    private BigDecimal altSgptResult;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "ANTI_HBS_POS_REAC_IND", length = 300)
    private String antiHbsPosReacInd;

    @Column(name = "AST_SGOT_RESULT", precision = 18)
    private BigDecimal astSgotResult;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_E_ANTIGEN", length = 300)
    private String hepEAntigen;

    @Column(name = "HBE_AG_DT")
    private LocalDate hbeAgDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_B_SURFACE_ANTIGEN", length = 300)
    private String hepBSurfaceAntigen;

    @Column(name = "HBS_AG_DT")
    private LocalDate hbsAgDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_B_DNA", length = 300)
    private String hepBDna;

    @Column(name = "HBV_NAT_DT")
    private LocalDate hbvNatDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HCV_RNA", length = 300)
    private String hcvRna;

    @Column(name = "HCV_RNA_DT")
    private LocalDate hcvRnaDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_D_TEST_IND", length = 300)
    private String hepDTestInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_A_IGM_ANTIBODY", length = 300)
    private String hepAIgmAntibody;

    @Column(name = "IGM_ANTI_HAV_DT")
    private LocalDate igmAntiHavDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_B_IGM_ANTIBODY", length = 300)
    private String hepBIgmAntibody;

    @Column(name = "IGM_ANTI_HBC_DT")
    private LocalDate igmAntiHbcDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PREV_NEG_HEP_TEST_IND", length = 300)
    private String prevNegHepTestInd;

    @jakarta.validation.constraints.Size(max = 25)
    @Column(name = "ANTIHCV_SIGCUT_RATIO", length = 25)
    private String antihcvSigcutRatio;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "ANTIHCV_SUPP_ASSAY", length = 300)
    private String antihcvSuppAssay;

    @Column(name = "SUPP_ANTI_HCV_DT")
    private LocalDate suppAntiHcvDt;

    @Column(name = "ALT_RESULT_DT")
    private LocalDate altResultDt;

    @Column(name = "AST_RESULT_DT")
    private LocalDate astResultDt;

    @Column(name = "ALT_SGPT_RSLT_UP_LMT", precision = 18)
    private BigDecimal altSgptRsltUpLmt;

    @Column(name = "AST_SGOT_RSLT_UP_LMT", precision = 18)
    private BigDecimal astSgotRsltUpLmt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_A_TOTAL_ANTIBODY", length = 300)
    private String hepATotalAntibody;

    @Column(name = "TOTAL_ANTI_HAV_DT")
    private LocalDate totalAntiHavDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_B_TOTAL_ANTIBODY", length = 300)
    private String hepBTotalAntibody;

    @Column(name = "TOTAL_ANTI_HBC_DT")
    private LocalDate totalAntiHbcDt;

    @Column(name = "TOTAL_ANTI_HCV_DT")
    private LocalDate totalAntiHcvDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_D_TOTAL_ANTIBODY", length = 300)
    private String hepDTotalAntibody;

    @Column(name = "TOTAL_ANTI_HDV_DT")
    private LocalDate totalAntiHdvDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_E_TOTAL_ANTIBODY", length = 300)
    private String hepETotalAntibody;

    @Column(name = "TOTAL_ANTI_HEV_DT")
    private LocalDate totalAntiHevDt;

    @Column(name = "VERIFIED_TEST_DT")
    private LocalDate verifiedTestDt;

    @jakarta.validation.constraints.Size(max = 15)
    @Column(name = "LEGACY_CASE_ID", length = 15)
    private String legacyCaseId;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "DIABETES_IND", length = 300)
    private String diabetesInd;

    @Column(name = "DIABETES_DX_DT")
    private LocalDate diabetesDxDt;

    @Column(name = "PREGNANCY_DUE_DT")
    private LocalDate pregnancyDueDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PAT_JUNDICED_IND", length = 300)
    private String patJundicedInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PAT_PREV_AWARE_IND", length = 300)
    private String patPrevAwareInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEP_CARE_PROVIDER", length = 300)
    private String hepCareProvider;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "TEST_REASON", length = 300)
    private String testReason;

    @jakarta.validation.constraints.Size(max = 150)
    @Column(name = "TEST_REASON_OTH", length = 150)
    private String testReasonOth;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "SYMPTOMATIC_IND", length = 300)
    private String symptomaticInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "MTH_BORN_OUTSIDE_US", length = 300)
    private String mthBornOutsideUs;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "MTH_ETHNICITY", length = 300)
    private String mthEthnicity;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "MTH_HBS_AG_PRIOR_POS", length = 300)
    private String mthHbsAgPriorPos;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "MTH_POS_AFTER", length = 300)
    private String mthPosAfter;

    @Column(name = "MTH_POS_TEST_DT")
    private LocalDate mthPosTestDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "MTH_RACE", length = 300)
    private String mthRace;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "MTH_BIRTH_COUNTRY", length = 300)
    private String mthBirthCountry;

    @Column(name = "NOT_SUBMIT_DT")
    private Instant notSubmitDt;

    @Column(name = "PAT_REPORTED_AGE", precision = 18)
    private BigDecimal patReportedAge;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PAT_REPORTED_AGE_UNIT", length = 300)
    private String patReportedAgeUnit;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PAT_CITY", length = 50)
    private String patCity;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PAT_COUNTRY", length = 300)
    private String patCountry;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PAT_COUNTY", length = 300)
    private String patCounty;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PAT_CURR_GENDER", length = 300)
    private String patCurrGender;

    @Column(name = "PAT_DOB")
    private Instant patDob;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PAT_ETHNICITY", length = 300)
    private String patEthnicity;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PAT_FIRST_NM", length = 50)
    private String patFirstNm;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PAT_LAST_NM", length = 50)
    private String patLastNm;

    @jakarta.validation.constraints.Size(max = 25)
    @Column(name = "PAT_LOCAL_ID", length = 25)
    private String patLocalId;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PAT_MIDDLE_NM", length = 50)
    private String patMiddleNm;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PAT_PREGNANT_IND", length = 300)
    private String patPregnantInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PAT_RACE", length = 300)
    private String patRace;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PAT_STATE", length = 300)
    private String patState;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PAT_STREET_ADDR_1", length = 50)
    private String patStreetAddr1;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PAT_STREET_ADDR_2", length = 50)
    private String patStreetAddr2;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "PAT_ZIP_CODE", length = 10)
    private String patZipCode;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "RPT_SRC_SOURCE_NM", length = 300)
    private String rptSrcSourceNm;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "RPT_SRC_STATE", length = 300)
    private String rptSrcState;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "RPT_SRC_CD_DESC", length = 300)
    private String rptSrcCdDesc;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "BLD_EXPOSURE_IND", length = 300)
    private String bldExposureInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "BLD_RECVD_IND", length = 300)
    private String bldRecvdInd;

    @Column(name = "BLD_RECVD_DT")
    private LocalDate bldRecvdDt;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "MED_DEN_BLD_CT_FRQ", length = 300)
    private String medDenBldCtFrq;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "MED_DEN_EMPLOYEE_IND", length = 300)
    private String medDenEmployeeInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "MED_DEN_EMP_EVER_IND", length = 300)
    private String medDenEmpEverInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "CLOTFACTOR_PRIOR_1987", length = 300)
    private String clotfactorPrior1987;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "BLD_CONTAM_IND", length = 300)
    private String bldContamInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "DEN_WORK_OR_SURG_IND", length = 300)
    private String denWorkOrSurgInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HEMODIALYSIS_IND", length = 300)
    private String hemodialysisInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "LT_HEMODIALYSIS_IND", length = 300)
    private String ltHemodialysisInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HSPTL_PRIOR_ONSET_IND", length = 300)
    private String hsptlPriorOnsetInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "EVER_INJCT_NOPRSC_DRG", length = 300)
    private String everInjctNoprscDrg;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "INCAR_24PLUSHRS_IND", length = 300)
    private String incar24plushrsInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "INCAR_6PLUS_MO_IND", length = 300)
    private String incar6plusMoInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "EVER_INCAR_IND", length = 300)
    private String everIncarInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "INCAR_TYPE_JAIL_IND", length = 300)
    private String incarTypeJailInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "INCAR_TYPE_PRISON_IND", length = 300)
    private String incarTypePrisonInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "INCAR_TYPE_JUV_IND", length = 300)
    private String incarTypeJuvInd;

    @Column(name = "LAST6PLUSMO_INCAR_PER", precision = 18)
    private BigDecimal last6plusmoIncarPer;

    @Column(name = "LAST6PLUSMO_INCAR_YR", precision = 18)
    private BigDecimal last6plusmoIncarYr;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "OUTPAT_IV_INF_IND", length = 300)
    private String outpatIvInfInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "LTCARE_RESIDENT_IND", length = 300)
    private String ltcareResidentInd;

    @Column(name = "LIFE_SEX_PRTNR_NBR", precision = 18)
    private BigDecimal lifeSexPrtnrNbr;

    @jakarta.validation.constraints.Size(max = 200)
    @Column(name = "BLD_EXPOSURE_OTH", length = 200)
    private String bldExposureOth;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PIERC_PRIOR_ONSET_IND", length = 300)
    private String piercPriorOnsetInd;

    @jakarta.validation.constraints.Size(max = 150)
    @Column(name = "PIERC_PERF_LOC_OTH", length = 150)
    private String piercPerfLocOth;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PIERC_PERF_LOC", length = 300)
    private String piercPerfLoc;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PUB_SAFETY_BLD_CT_FRQ", length = 300)
    private String pubSafetyBldCtFrq;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PUB_SAFETY_WORKER_IND", length = 300)
    private String pubSafetyWorkerInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "STD_TREATED_IND", length = 300)
    private String stdTreatedInd;

    @Column(name = "STD_LAST_TREATMENT_YR", precision = 18)
    private BigDecimal stdLastTreatmentYr;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "NON_ORAL_SURGERY_IND", length = 300)
    private String nonOralSurgeryInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "TATT_PRIOR_ONSET_IND", length = 300)
    private String tattPriorOnsetInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "TATTOO_PERF_LOC", length = 300)
    private String tattooPerfLoc;

    @jakarta.validation.constraints.Size(max = 150)
    @Column(name = "TATT_PRIOR_LOC_OTH", length = 150)
    private String tattPriorLocOth;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "BLD_TRANSF_PRIOR_1992", length = 300)
    private String bldTransfPrior1992;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "ORGN_TRNSP_PRIOR_1992", length = 300)
    private String orgnTrnspPrior1992;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "TRANSMISSION_MODE", length = 300)
    private String transmissionMode;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HOUSEHOLD_TRAVEL_IND", length = 300)
    private String householdTravelInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "TRAVEL_OUT_USACAN_IND", length = 300)
    private String travelOutUsacanInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "TRAVEL_OUT_USACAN_LOC", length = 300)
    private String travelOutUsacanLoc;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "HOUSEHOLD_TRAVEL_LOC", length = 300)
    private String householdTravelLoc;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "TRAVEL_REASON", length = 300)
    private String travelReason;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "IMM_GLOB_RECVD_IND", length = 300)
    private String immGlobRecvdInd;

    @Column(name = "GLOB_LAST_RECVD_YR")
    private LocalDate globLastRecvdYr;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "VACC_RECVD_IND", length = 10)
    private String vaccRecvdInd;

    @Column(name = "VACC_DOSE_NBR_1", precision = 18)
    private BigDecimal vaccDoseNbr1;

    @Column(name = "VACC_RECVD_DT_1")
    private LocalDate vaccRecvdDt1;

    @Column(name = "VACC_DOSE_NBR_2", precision = 18)
    private BigDecimal vaccDoseNbr2;

    @Column(name = "VACC_RECVD_DT_2")
    private LocalDate vaccRecvdDt2;

    @Column(name = "VACC_DOSE_NBR_3", precision = 18)
    private BigDecimal vaccDoseNbr3;

    @Column(name = "VACC_RECVD_DT_3")
    private LocalDate vaccRecvdDt3;

    @Column(name = "VACC_DOSE_NBR_4", precision = 18)
    private BigDecimal vaccDoseNbr4;

    @Column(name = "VACC_RECVD_DT_4")
    private LocalDate vaccRecvdDt4;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "VACC_GT_4_IND", length = 300)
    private String vaccGt4Ind;

    @Column(name = "VACC_DOSE_RECVD_NBR", precision = 18)
    private BigDecimal vaccDoseRecvdNbr;

    @Column(name = "VACC_LAST_RECVD_YR", precision = 18)
    private BigDecimal vaccLastRecvdYr;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "ANTI_HBSAG_TESTED_IND", length = 300)
    private String antiHbsagTestedInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "CONDITION_CD", length = 300)
    private String conditionCd;

    @Column(name = "EVENT_DATE")
    private Instant eventDate;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "IMPORT_FROM_CITY", length = 300)
    private String importFromCity;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "IMPORT_FROM_COUNTRY", length = 300)
    private String importFromCountry;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "IMPORT_FROM_COUNTY", length = 300)
    private String importFromCounty;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "IMPORT_FROM_STATE", length = 300)
    private String importFromState;

    @jakarta.validation.constraints.NotNull
    @Column(name = "INVESTIGATION_KEY", nullable = false)
    private Long investigationKey;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "INVESTIGATOR_NAME", length = 300)
    private String investigatorName;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PAT_ELECTRONIC_IND", length = 300)
    private String patElectronicInd;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PHYS_CITY", length = 300)
    private String physCity;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PHYS_COUNTY", length = 300)
    private String physCounty;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PHYS_NAME", length = 300)
    private String physName;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "PHYS_STATE", length = 300)
    private String physState;

    @Column(name = "PROGRAM_JURISDICTION_OID", precision = 20)
    private BigDecimal programJurisdictionOid;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "RPT_SRC_CITY", length = 300)
    private String rptSrcCity;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "RPT_SRC_COUNTY", length = 300)
    private String rptSrcCounty;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "RPT_SRC_COUNTY_CD", length = 300)
    private String rptSrcCountyCd;

    @Column(name = "PHYSICIAN_UID", precision = 18)
    private BigDecimal physicianUid;

    @jakarta.validation.constraints.NotNull
    @Column(name = "PATIENT_UID", nullable = false, precision = 18)
    private BigDecimal patientUid;

    @jakarta.validation.constraints.NotNull
    @Column(name = "CASE_UID", nullable = false, precision = 18)
    private BigDecimal caseUid;

    @Column(name = "INVESTIGATOR_UID", precision = 18)
    private BigDecimal investigatorUid;

    @Column(name = "REPORTING_SOURCE_UID", precision = 18)
    private BigDecimal reportingSourceUid;

    @jakarta.validation.constraints.NotNull
    @Column(name = "REFRESH_DATETIME", nullable = false)
    private Instant refreshDatetime;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PAT_BIRTH_COUNTRY", length = 50)
    private String patBirthCountry;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "EVENT_DATE_TYPE", length = 100)
    private String eventDateType;

    @Column(name = "INNC_NOTIFICATION_DT")
    private Instant inncNotificationDt;

}