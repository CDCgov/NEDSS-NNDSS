package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "D_INV_EPIDEMIOLOGY")
public class DInvEpidemiology {
    @Column(name = "D_INV_EPIDEMIOLOGY_KEY")
    private Double dInvEpidemiologyKey;

    @Column(name = "nbs_case_answer_uid", precision = 21)
    private BigDecimal nbsCaseAnswerUid;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_CONTACTS_PROPHYLAXIS_NBR", length = 2000)
    private String epiContactsProphylaxisNbr;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_FemaleSexPartners", length = 2000)
    private String epiFemalesexpartners;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_MaleSexPartner", length = 2000)
    private String epiMalesexpartner;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_NUM_EXP_CASES", length = 2000)
    private String epiNumExpCases;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_SUSPECTED_SOURCES_NBR", length = 2000)
    private String epiSuspectedSourcesNbr;

    @Column(name = "EPI_DATE_BLOOD_DONATED")
    private LocalDate epiDateBloodDonated;

    @Column(name = "EPI_FIRST_EXPOSURE_DT")
    private LocalDate epiFirstExposureDt;

    @Column(name = "EPI_INCIDENT_DT")
    private LocalDate epiIncidentDt;

    @Column(name = "EPI_LIKELY_EXPOS_DT")
    private LocalDate epiLikelyExposDt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_AGE_SETTING_VERIFIED_IND", length = 1999)
    private String epiAgeSettingVerifiedInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ANATOMIC_SEX_SITE", length = 1999)
    private String epiAnatomicSexSite;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ANATOMIC_SEX_SITE_OTH", length = 1999)
    private String epiAnatomicSexSiteOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ANIMAL_EXPOSURE_IND", length = 1999)
    private String epiAnimalExposureInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ANIMAL_TYPE", length = 1999)
    private String epiAnimalType;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ANIMAL_TYPE_OTH", length = 1999)
    private String epiAnimalTypeOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ChildCareCase", length = 1999)
    private String epiChildcarecase;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CHINA_HC_HISTORY_IND", length = 1999)
    private String epiChinaHcHistoryInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CNT_RCVD_HLTHCRE_OTS", length = 1999)
    private String epiCntRcvdHlthcreOts;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CNTRY_USUAL_RESID", length = 1999)
    private String epiCntryUsualResid;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CO_ALARM_PRESENT", length = 1999)
    private String epiCoAlarmPresent;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CO_ALARM_SOUNDED", length = 1999)
    private String epiCoAlarmSounded;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CO_ELEVATED_EXPOSURE", length = 1999)
    private String epiCoElevatedExposure;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ContactBabysitter", length = 1999)
    private String epiContactbabysitter;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ContactChildcare", length = 1999)
    private String epiContactchildcare;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ContactHousehold", length = 1999)
    private String epiContacthousehold;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ContactOfCase", length = 1999)
    private String epiContactofcase;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ContactOther", length = 1999)
    private String epiContactother;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ContactPlaymate", length = 1999)
    private String epiContactplaymate;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ContactSexPartner", length = 1999)
    private String epiContactsexpartner;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CRCTIONAL_FAC_LNG_TY", length = 1999)
    private String epiCrctionalFacLngTy;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CRCTIONAL_FAC_LNG_TY_OTH", length = 1999)
    private String epiCrctionalFacLngTyOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CRCTIONAL_FAC_TYPE", length = 1999)
    private String epiCrctionalFacType;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CRCTIONAL_FAC_TYPE_OTH", length = 1999)
    private String epiCrctionalFacTypeOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CTT_CONF_CASE_COMM", length = 1999)
    private String epiCttConfCaseComm;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CTT_CONF_CASE_HLTHCR", length = 1999)
    private String epiCttConfCaseHlthcr;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CTT_CONF_CASE_HSHLD", length = 1999)
    private String epiCttConfCaseHshld;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_CTT_CONF_CASE_PAT_IND", length = 1999)
    private String epiCttConfCasePatInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_DaycareContact", length = 1999)
    private String epiDaycarecontact;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EDUCATION", length = 1999)
    private String epiEducation;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EDUCATION_OTH", length = 1999)
    private String epiEducationOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ENGAGE_OUTDR_ACTIVITE", length = 1999)
    private String epiEngageOutdrActivite;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EpiLinked", length = 1999)
    private String epiEpilinked;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EVALTION_CTT_INV", length = 1999)
    private String epiEvaltionCttInv;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EXPOSURE_DATE_UNK", length = 1999)
    private String epiExposureDateUnk;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EXPOSURE_SOURCE", length = 1999)
    private String epiExposureSource;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EXPOSURE_SOURCE_OTH", length = 1999)
    private String epiExposureSourceOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EXPOSURE_TO_COVID_4WK", length = 1999)
    private String epiExposureToCovid4wk;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EXPSR_SITE_CATEGORY", length = 1999)
    private String epiExpsrSiteCategory;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EXPSR_SITE_CATEGORY_OTH", length = 1999)
    private String epiExpsrSiteCategoryOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EXTREME_WEATHER", length = 1999)
    private String epiExtremeWeather;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EXTRM_WEATHR_TYP", length = 1999)
    private String epiExtrmWeathrTyp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_EXTRM_WEATHR_TYP_OTH", length = 1999)
    private String epiExtrmWeathrTypOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_FEM_TRANS_NUM_RANGE", length = 1999)
    private String epiFemTransNumRange;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_FEMALE_NUM_RANGE", length = 1999)
    private String epiFemaleNumRange;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_FIRE_RELATED_EXPSR", length = 1999)
    private String epiFireRelatedExpsr;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_FoodHandler", length = 1999)
    private String epiFoodhandler;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_GENERATOR_DISTANCE", length = 1999)
    private String epiGeneratorDistance;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_GENERATOR_LOCATION", length = 1999)
    private String epiGeneratorLocation;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_GENERATOR_LOCATION_OTH", length = 1999)
    private String epiGeneratorLocationOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_GROUP_SEX_IND", length = 1999)
    private String epiGroupSexInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_HC_CONTACT_TYPE", length = 1999)
    private String epiHcContactType;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_HD_CLCT_CTT_INFO", length = 1999)
    private String epiHdClctCttInfo;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_ID_BY_BLOOD_SCREEN", length = 1999)
    private String epiIdByBloodScreen;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_IDEN_CTT_INV", length = 1999)
    private String epiIdenCttInv;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_IMPORT_STATUS_IND", length = 1999)
    private String epiImportStatusInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_InDayCare", length = 1999)
    private String epiIndaycare;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_INFECTED_IN_UTERO", length = 1999)
    private String epiInfectedInUtero;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_INTENT_OF_EXPOSURE", length = 1999)
    private String epiIntentOfExposure;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_IVDrugUse", length = 1999)
    private String epiIvdruguse;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_LAB_ACQUIRED_ILLNESS", length = 1999)
    private String epiLabAcquiredIllness;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_LAB_CONFIRMED_CASE_IND", length = 1999)
    private String epiLabConfirmedCaseInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_LATER_SYMP_CTT_EXP", length = 1999)
    private String epiLaterSympCttExp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_MALE_NUM_RANGE", length = 1999)
    private String epiMaleNumRange;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_MALE_TRANS_NUM_RANGE", length = 1999)
    private String epiMaleTransNumRange;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_NON_LAB_ACQ_ILLNESS", length = 1999)
    private String epiNonLabAcqIllness;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_OTH_CS_RELTD_CRNT_CS", length = 1999)
    private String epiOthCsReltdCrntCs;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_OTH_EXPOSURE_IND", length = 1999)
    private String epiOthExposureInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_OTH_IDENT_NUM_RANGE", length = 1999)
    private String epiOthIdentNumRange;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_OutbreakAssoc", length = 1999)
    private String epiOutbreakassoc;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_OutbreakFoodHndlr", length = 1999)
    private String epiOutbreakfoodhndlr;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_outbreakNonFoodHndlr", length = 1999)
    private String epiOutbreaknonfoodhndlr;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_OutbreakUnidentified", length = 1999)
    private String epiOutbreakunidentified;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_OutbreakWaterborne", length = 1999)
    private String epiOutbreakwaterborne;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_OUTCOME_OF_PCC_RECORD", length = 1999)
    private String epiOutcomeOfPccRecord;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_OUTCOME_OF_PCC_RECORD_OTH", length = 1999)
    private String epiOutcomeOfPccRecordOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_OUTDOOR_ACTIVITIES", length = 1999)
    private String epiOutdoorActivities;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_OUTDOOR_ACTIVITIES_OTH", length = 1999)
    private String epiOutdoorActivitiesOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_PATIENT_ACQUIRED_PET", length = 1999)
    private String epiPatientAcquiredPet;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_PETS_IN_HOUSEHOLD", length = 1999)
    private String epiPetsInHousehold;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_POWER_OUTAGE_EVENT", length = 1999)
    private String epiPowerOutageEvent;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_PRTNR_GNDR_IDENT_KNOW", length = 1999)
    private String epiPrtnrGndrIdentKnow;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_PT_KNOWS_ILL_PERSONS", length = 1999)
    private String epiPtKnowsIllPersons;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_PUBLIC_SITE_OF_EXPSR", length = 1999)
    private String epiPublicSiteOfExpsr;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_PUBLIC_SITE_OF_EXPSR_OTH", length = 1999)
    private String epiPublicSiteOfExpsrOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_RCVD_HLTHCRE_OTS_STT", length = 1999)
    private String epiRcvdHlthcreOtsStt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_RCVD_HLTHCRE_OTS_USA", length = 1999)
    private String epiRcvdHlthcreOtsUsa;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_RecDrugUse", length = 1999)
    private String epiRecdruguse;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_RESIDENT_SITE_OF_EXP", length = 1999)
    private String epiResidentSiteOfExp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_RESIDENT_SITE_OF_EXP_OTH", length = 1999)
    private String epiResidentSiteOfExpOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SEVERE_ARD_EXP_IND", length = 1999)
    private String epiSevereArdExpInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SEX_PARTNER_GENDER", length = 1999)
    private String epiSexPartnerGender;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SEX_PARTNER_MEET_PLAC", length = 1999)
    private String epiSexPartnerMeetPlac;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SEX_PARTNER_MEET_PLAC_OTH", length = 1999)
    private String epiSexPartnerMeetPlacOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SEX_PARTNER_TRAVEL", length = 1999)
    private String epiSexPartnerTravel;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SPRD_BYD_TRNSMSN_STTNG_IND", length = 1999)
    private String epiSprdBydTrnsmsnSttngInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SUSPECT_FOOD_CONSUMED", length = 1999)
    private String epiSuspectFoodConsumed;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SUSPECT_MEAT_LOC_II", length = 1999)
    private String epiSuspectMeatLocIi;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SUSPECT_MEAT_LOC_II_OTH", length = 1999)
    private String epiSuspectMeatLocIiOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SUSPECT_MEAT_LOCATION", length = 1999)
    private String epiSuspectMeatLocation;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SUSPECT_MEAT_LOCATION_OTH", length = 1999)
    private String epiSuspectMeatLocationOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SUSPECTED_SOURCES_IND", length = 1999)
    private String epiSuspectedSourcesInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_SYMPTOMATIC_CTT_EXP", length = 1999)
    private String epiSymptomaticCttExp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_TOUCH_DEAD_ANIAMALS", length = 1999)
    private String epiTouchDeadAniamals;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_TRANSMISSION_SETTING", length = 1999)
    private String epiTransmissionSetting;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_TRANSMISSION_SETTING_OTH", length = 1999)
    private String epiTransmissionSettingOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_TRNS_STG_OUTSIDE_SPRD", length = 1999)
    private String epiTrnsStgOutsideSprd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_TRNS_STG_OUTSIDE_SPRD_OTH", length = 1999)
    private String epiTrnsStgOutsideSprdOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_TYP_OF_WC_CLAIM", length = 1999)
    private String epiTypOfWcClaim;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_TYP_OF_WC_CLAIM_OTH", length = 1999)
    private String epiTypOfWcClaimOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_UNK_EXPOSURE_SOURCE", length = 1999)
    private String epiUnkExposureSource;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_UNSUPERVISED_PETS_IND", length = 1999)
    private String epiUnsupervisedPetsInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_US_ACQUIRED", length = 1999)
    private String epiUsAcquired;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_US_ACQUIRED_OTH", length = 1999)
    private String epiUsAcquiredOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_US_COVID_CASE_EXP_IND", length = 1999)
    private String epiUsCovidCaseExpInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_US_HC_WORKER_IND", length = 1999)
    private String epiUsHcWorkerInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_USUAL_OCCUP_INDSTRY", length = 1999)
    private String epiUsualOccupIndstry;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_WARNING_ANNOUNCEMENT", length = 1999)
    private String epiWarningAnnouncement;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "EPI_WOODED_BRUSHY_AREAS", length = 1999)
    private String epiWoodedBrushyAreas;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "SOURCE_SPREAD", length = 1999)
    private String sourceSpread;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_ADD_ESTABLS_EXP_OCCRD", length = 2000)
    private String epiAddEstablsExpOccrd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_ANIMAL_TYPE_TXT", length = 2000)
    private String epiAnimalTypeTxt;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_ContactOthSpecify", length = 2000)
    private String epiContactothspecify;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_DEAD_ANIMAL_TOUCH_TYP", length = 2000)
    private String epiDeadAnimalTouchTyp;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_EPI_LINKED_CASE_ID2", length = 2000)
    private String epiEpiLinkedCaseId2;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_EPI_LINKED_CASE_ID3", length = 2000)
    private String epiEpiLinkedCaseId3;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_EPI_LINKED_TO_CASE_ID", length = 2000)
    private String epiEpiLinkedToCaseId;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_GROUP_SEX_DETAILS", length = 2000)
    private String epiGroupSexDetails;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_INCUBATION_PERIOD", length = 2000)
    private String epiIncubationPeriod;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_INFECTION_SOURCE", length = 2000)
    private String epiInfectionSource;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_LIKELY_EXPOS_SRC", length = 2000)
    private String epiLikelyExposSrc;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_LIVE_ANIMAL_TOUCH_TYP", length = 2000)
    private String epiLiveAnimalTouchTyp;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_OTH_EXPOSURE_SPECIFY", length = 2000)
    private String epiOthExposureSpecify;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_OutbreakFoodItem", length = 2000)
    private String epiOutbreakfooditem;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_SEX_PARTNER_DATE", length = 2000)
    private String epiSexPartnerDate;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_SEX_PARTNER_TRAVEL_DE", length = 2000)
    private String epiSexPartnerTravelDe;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_TRV_INCUBATION_PERIOD", length = 2000)
    private String epiTrvIncubationPeriod;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_TYPE_OF_AMPHIBIAN_OTH", length = 2000)
    private String epiTypeOfAmphibianOth;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_TYPE_OF_ANIMAL_OTH", length = 2000)
    private String epiTypeOfAnimalOth;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_TYPE_OF_MAMMAL_OTH", length = 2000)
    private String epiTypeOfMammalOth;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_TYPE_OF_REPTILE_OTH", length = 2000)
    private String epiTypeOfReptileOth;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_WORK_SITE_EXPOSURE", length = 2000)
    private String epiWorkSiteExposure;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "EPI_ZIP_ESTABLS_EXP_OCCRD", length = 2000)
    private String epiZipEstablsExpOccrd;

}