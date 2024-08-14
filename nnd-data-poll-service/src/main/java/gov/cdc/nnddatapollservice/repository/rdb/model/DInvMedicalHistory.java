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
@Table(name = "D_INV_MEDICAL_HISTORY")
public class DInvMedicalHistory {
    @Column(name = "D_INV_MEDICAL_HISTORY_KEY")
    private Double dInvMedicalHistoryKey;

    @Column(name = "nbs_case_answer_uid", precision = 21)
    private BigDecimal nbsCaseAnswerUid;

    @Column(name = "MDH_DiabetesDxDate")
    private LocalDate mdhDiabetesdxdate;

    @Column(name = "MDH_DueDate")
    private LocalDate mdhDuedate;

    @Column(name = "MDH_PROVIDER_REASON_VISIT_DT")
    private LocalDate mdhProviderReasonVisitDt;

    @Column(name = "MDH_SPLENECTOMY_DT")
    private LocalDate mdhSplenectomyDt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_ASPLENIC_IND", length = 1999)
    private String mdhAsplenicInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_AUTOIMMUNE_DISEASE", length = 1999)
    private String mdhAutoimmuneDisease;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_CHRONIC_LIVER_DIS_IND", length = 1999)
    private String mdhChronicLiverDisInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_CHRONIC_LUNG_DIS_IND", length = 1999)
    private String mdhChronicLungDisInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_CHRONIC_RENAL_DIS_IND", length = 1999)
    private String mdhChronicRenalDisInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_CONG_MALFORMATION_IND", length = 1999)
    private String mdhCongMalformationInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_CURR_BREAST_FEEDING", length = 1999)
    private String mdhCurrBreastFeeding;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_CV_DISEASE_IND", length = 1999)
    private String mdhCvDiseaseInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_Diabetes", length = 1999)
    private String mdhDiabetes;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_DIABETES_MELLITUS_IND", length = 1999)
    private String mdhDiabetesMellitusInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_DIABTS_MELITS_INSULIN", length = 1999)
    private String mdhDiabtsMelitsInsulin;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_HIV_VIRAL_LD_UNDETECT", length = 1999)
    private String mdhHivViralLdUndetect;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_IMMUNO_CONDITION_IND", length = 1999)
    private String mdhImmunoConditionInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_ISCHEMIC_HEART_DISEAS", length = 1999)
    private String mdhIschemicHeartDiseas;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_Jaundiced", length = 1999)
    private String mdhJaundiced;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_NEURO_DISABLITY_IND", length = 1999)
    private String mdhNeuroDisablityInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_OBESITY_IND", length = 1999)
    private String mdhObesityInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_OTH_CHRONIC_DIS_IND", length = 1999)
    private String mdhOthChronicDisInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_PAT_UNDRLYNG_COND", length = 1999)
    private String mdhPatUndrlyngCond;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_PREEXISTING_COND_IND", length = 1999)
    private String mdhPreexistingCondInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_PREV_STD_HIST", length = 1999)
    private String mdhPrevStdHist;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_PrevAwareInfection", length = 1999)
    private String mdhPrevawareinfection;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_ProviderOfCare", length = 1999)
    private String mdhProviderofcare;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_PSYCHIATRIC_CONDITION", length = 1999)
    private String mdhPsychiatricCondition;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_ReasonForTest", length = 1999)
    private String mdhReasonfortest;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_ReasonForTest_OTH", length = 1999)
    private String mdhReasonfortestOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_SICKLE_CELL_DIS_IND", length = 1999)
    private String mdhSickleCellDisInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_SUBSTANCE_ABUSE", length = 1999)
    private String mdhSubstanceAbuse;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_Symptomatic", length = 1999)
    private String mdhSymptomatic;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_TISSUE_ORGAN_TRNSPLNT", length = 1999)
    private String mdhTissueOrganTrnsplnt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_TNF_ANTAGONIST_TX", length = 1999)
    private String mdhTnfAntagonistTx;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_UNDERLYING_COND_OTH", length = 1999)
    private String mdhUnderlyingCondOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "MDH_VIRAL_HEP_B_C_INF", length = 1999)
    private String mdhViralHepBCInf;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_CONG_MALFORMATION_OTH", length = 2000)
    private String mdhCongMalformationOth;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_GASTRIC_SURGERY_IND", length = 2000)
    private String mdhGastricSurgeryInd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_HEMATOLOGIC_DISEASE_", length = 2000)
    private String mdhHematologicDisease;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_IMMUNODEFICIENCY_TYPE", length = 2000)
    private String mdhImmunodeficiencyType;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_MEDICAL_RECORD_NBR", length = 2000)
    private String mdhMedicalRecordNbr;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_NEURO_DISABLITY_INFO", length = 2000)
    private String mdhNeuroDisablityInfo;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_NEURO_DISABLITY_INFO2", length = 2000)
    private String mdhNeuroDisablityInfo2;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_NEURO_DISABLITY_INFO3", length = 2000)
    private String mdhNeuroDisablityInfo3;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_OTH_CHRONIC_DIS_TXT", length = 2000)
    private String mdhOthChronicDisTxt;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_PRIOR_ILLNESS_OTH", length = 2000)
    private String mdhPriorIllnessOth;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_PSYCH_CONDITION_SPEC", length = 2000)
    private String mdhPsychConditionSpec;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_PSYCH_CONDITION_SPEC2", length = 2000)
    private String mdhPsychConditionSpec2;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_PSYCH_CONDITION_SPEC3", length = 2000)
    private String mdhPsychConditionSpec3;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_ReasonForTestingOth", length = 2000)
    private String mdhReasonfortestingoth;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_SPLENECTOMY_REASON", length = 2000)
    private String mdhSplenectomyReason;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_TYPE_LIVER_DZ_OTH", length = 2000)
    private String mdhTypeLiverDzOth;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_TYPE_MALIGNANCY_OTH", length = 2000)
    private String mdhTypeMalignancyOth;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_TYPE_ORGAN_TRANSPLANT", length = 2000)
    private String mdhTypeOrganTransplant;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_TYPE_RENAL_DZ_OTH", length = 2000)
    private String mdhTypeRenalDzOth;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "MDH_UNDRLYNG_COND_SPECIFY", length = 2000)
    private String mdhUndrlyngCondSpecify;

}