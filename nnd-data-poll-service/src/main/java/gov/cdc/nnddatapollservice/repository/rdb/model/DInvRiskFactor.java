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
@Table(name = "D_INV_RISK_FACTOR")
public class DInvRiskFactor {
    @Column(name = "D_INV_RISK_FACTOR_KEY")
    private Double dInvRiskFactorKey;

    @Column(name = "nbs_case_answer_uid", precision = 21)
    private BigDecimal nbsCaseAnswerUid;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_IncarcTimeMonths", length = 2000)
    private String rskIncarctimemonths;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_IncarcYear6Mos", length = 2000)
    private String rskIncarcyear6mos;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_NUM_SEX_PARTNER_12MO", length = 2000)
    private String rskNumSexPartner12mo;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_NumSexPrtners", length = 2000)
    private String rskNumsexprtners;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_STDTxYr", length = 2000)
    private String rskStdtxyr;

    @Column(name = "RSK_BloodTransfusionDate")
    private LocalDate rskBloodtransfusiondate;

    @Column(name = "RSK_DT_OF_BLD_TRANSFUSION")
    private LocalDate rskDtOfBldTransfusion;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_ADULT_CONG_LIVING_EXP", length = 1999)
    private String rskAdultCongLivingExp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_ALCOHOLISM", length = 1999)
    private String rskAlcoholism;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_ANIMAL_CONTACT_IND", length = 1999)
    private String rskAnimalContactInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_ANS_REFUSED_SEX_PARTNER", length = 1999)
    private String rskAnsRefusedSexPartner;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_ATTEND_EVENTS", length = 1999)
    private String rskAttendEvents;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_BEEN_INCARCERATD_12MO_IND", length = 1999)
    private String rskBeenIncarceratd12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_BLOOD_BANK_NOTIFIED", length = 1999)
    private String rskBloodBankNotified;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_BLOOD_DONOR", length = 1999)
    private String rskBloodDonor;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_BLOOD_TRANSFUSION", length = 1999)
    private String rskBloodTransfusion;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_BloodExpOther", length = 1999)
    private String rskBloodexpother;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_BloodTransfusion", length = 1999)
    private String rskBloodtransfusion;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_BloodWorkerCnctFreq", length = 1999)
    private String rskBloodworkercnctfreq;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_BloodWorkerEver", length = 1999)
    private String rskBloodworkerever;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_BloodWorkerOnset", length = 1999)
    private String rskBloodworkeronset;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_BREAST_FED_INFANT", length = 1999)
    private String rskBreastFedInfant;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_CHILD_CARE_FACILITY", length = 1999)
    private String rskChildCareFacility;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_ClottingPrior87", length = 1999)
    private String rskClottingprior87;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_COCAINE_USE_12MO_IND", length = 1999)
    private String rskCocaineUse12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_CONGEST_HEART_FAIL", length = 1999)
    private String rskCongestHeartFail;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_CONSUMED_SUSPECT_MEAT", length = 1999)
    private String rskConsumedSuspectMeat;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_ContaminatedStick", length = 1999)
    private String rskContaminatedstick;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_COPD", length = 1999)
    private String rskCopd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_CORRECTIONAL_EXP", length = 1999)
    private String rskCorrectionalExp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_CRACK_USE_12MO_IND", length = 1999)
    private String rskCrackUse12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_DentalOralSx", length = 1999)
    private String rskDentaloralsx;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_DIABETES", length = 1999)
    private String rskDiabetes;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_DONATED_PRODUCT", length = 1999)
    private String rskDonatedProduct;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_DONATED_PRODUCT_OTH", length = 1999)
    private String rskDonatedProductOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_ED_MEDS_USE_12MO_IND", length = 1999)
    private String rskEdMedsUse12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_EDUCATIONAL_EXP", length = 1999)
    private String rskEducationalExp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_HCW_WORK_EXPOSED", length = 1999)
    private String rskHcwWorkExposed;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_HEMODIALYSIS_BEFORE_ONSET", length = 1999)
    private String rskHemodialysisBeforeOnset;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_HemodialysisLongTerm", length = 1999)
    private String rskHemodialysislongterm;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_HepContactEver", length = 1999)
    private String rskHepcontactever;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_HEROIN_USE_12MO_IND", length = 1999)
    private String rskHeroinUse12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_HomelessWithinPastYr", length = 1999)
    private String rskHomelesswithinpastyr;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_HOSPITALIZATION_REASON", length = 1999)
    private String rskHospitalizationReason;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_HOSPITALIZATION_REASON_OTH", length = 1999)
    private String rskHospitalizationReasonOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_HospitalizedPrior", length = 1999)
    private String rskHospitalizedprior;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_HYPERTENSION", length = 1999)
    private String rskHypertension;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_IDU", length = 1999)
    private String rskIdu;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_IMMUNE_SUPPRESS_MED", length = 1999)
    private String rskImmuneSuppressMed;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_Incarcerated24Hrs", length = 1999)
    private String rskIncarcerated24hrs;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_Incarcerated6months", length = 1999)
    private String rskIncarcerated6months;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_IncarceratedEver", length = 1999)
    private String rskIncarceratedever;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_IncarceratedJail", length = 1999)
    private String rskIncarceratedjail;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_IncarcerationPrison", length = 1999)
    private String rskIncarcerationprison;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_IncarcJuvenileFacilit", length = 1999)
    private String rskIncarcjuvenilefacilit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_INJ_DRUG_USE_12MO_IND", length = 1999)
    private String rskInjDrugUse12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_IVInjectInfuseOutpt", length = 1999)
    private String rskIvinjectinfuseoutpt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_KIDNEY_DISEASE", length = 1999)
    private String rskKidneyDisease;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_LongTermCareRes", length = 1999)
    private String rskLongtermcareres;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_METH_USE_12MO_IND", length = 1999)
    private String rskMethUse12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_NITR_POP_USE_12MO_IND", length = 1999)
    private String rskNitrPopUse12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_NO_DRUG_USE_12MO_IND", length = 1999)
    private String rskNoDrugUse12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_OCCUPATIONAL_EXP", length = 1999)
    private String rskOccupationalExp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_ORGAN_DONOR", length = 1999)
    private String rskOrganDonor;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_ORGAN_TRANSPLANT", length = 1999)
    private String rskOrganTransplant;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_OTHER_DRUG_USE_12MO_IND", length = 1999)
    private String rskOtherDrugUse12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_PATIENT_HEALTHCARE_WORKER", length = 1999)
    private String rskPatientHealthcareWorker;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_PATIENT_HOMELESS", length = 1999)
    private String rskPatientHomeless;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_Piercing", length = 1999)
    private String rskPiercing;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_PiercingRcvdFrom", length = 1999)
    private String rskPiercingrcvdfrom;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_PSWrkrBldCnctFreq", length = 1999)
    private String rskPswrkrbldcnctfreq;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_PublicSafetyWorker", length = 1999)
    private String rskPublicsafetyworker;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_RECEIVED_BLOOD_TRANSF", length = 1999)
    private String rskReceivedBloodTransf;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_RISK_FACTORS_ASSESS_IND", length = 1999)
    private String rskRiskFactorsAssessInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SCHOOL_UNIVERSITY_EXP", length = 1999)
    private String rskSchoolUniversityExp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SEX_EXCH_DRGS_MNY_12MO_IND", length = 1999)
    private String rskSexExchDrgsMny12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SEX_INTOXCTED_HGH_12MO_IND", length = 1999)
    private String rskSexIntoxctedHgh12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SEX_W_ANON_PTRNR_12MO_IND", length = 1999)
    private String rskSexWAnonPtrnr12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SEX_W_FEMALE_12MO_IND", length = 1999)
    private String rskSexWFemale12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SEX_W_KNOWN_IDU_12MO_IND", length = 1999)
    private String rskSexWKnownIdu12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SEX_W_KNWN_MSM_12M_FML_IND", length = 1999)
    private String rskSexWKnwnMsm12mFmlInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SEX_W_MALE_12MO_IND", length = 1999)
    private String rskSexWMale12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SEX_W_TRANSGNDR_12MO_IND", length = 1999)
    private String rskSexWTransgndr12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SEX_WOUT_CONDOM_12MO_IND", length = 1999)
    private String rskSexWoutCondom12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SEXUAL_CONTACT_IND", length = 1999)
    private String rskSexualContactInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SHARED_INJ_EQUIP_12MO_IND", length = 1999)
    private String rskSharedInjEquip12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SMOKING_STATUS", length = 1999)
    private String rskSmokingStatus;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_STDTxEver", length = 1999)
    private String rskStdtxever;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_STEROIDS", length = 1999)
    private String rskSteroids;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_STROKE", length = 1999)
    private String rskStroke;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_SurgeryOther", length = 1999)
    private String rskSurgeryother;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_TARGET_POPULATIONS", length = 1999)
    private String rskTargetPopulations;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_Tattoo", length = 1999)
    private String rskTattoo;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_TattooLocation", length = 1999)
    private String rskTattoolocation;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_TICK_BITE_IND", length = 1999)
    private String rskTickBiteInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_TRANSFUSED_PRODUCT", length = 1999)
    private String rskTransfusedProduct;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_TRANSFUSED_PRODUCT_OTH", length = 1999)
    private String rskTransfusedProductOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_TRANSFUSION_ASSOCIATE", length = 1999)
    private String rskTransfusionAssociate;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_TransfusionPrior92", length = 1999)
    private String rskTransfusionprior92;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_TransplantPrior92", length = 1999)
    private String rskTransplantprior92;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_UNK_SEX_PARTNERS", length = 1999)
    private String rskUnkSexPartners;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_WKPLC_CRITICAL_INFRA", length = 1999)
    private String rskWkplcCriticalInfra;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_WKPLC_SETTING_CODED", length = 1999)
    private String rskWkplcSettingCoded;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_WKPLC_SETTING_CODED_OTH", length = 1999)
    private String rskWkplcSettingCodedOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "RSK_WORKPLACE_EXP", length = 1999)
    private String rskWorkplaceExp;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_ANIMAL_CONTACT_LOCATION", length = 2000)
    private String rskAnimalContactLocation;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_EXP_LOCATION_DETAILS", length = 2000)
    private String rskExpLocationDetails;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_EXPOSRE_EVENT_NOTES", length = 2000)
    private String rskExposreEventNotes;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_OTHER_DRUG_SPEC", length = 2000)
    private String rskOtherDrugSpec;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_OtherBldExpSpec", length = 2000)
    private String rskOtherbldexpspec;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_PiercingOthLocSpec", length = 2000)
    private String rskPiercingothlocspec;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_TattooLocOthSpec", length = 2000)
    private String rskTattoolocothspec;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "RSK_WKPLC_SETTING", length = 2000)
    private String rskWkplcSetting;

}