package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "D_CASE_MANAGEMENT")
public class DCaseManagement {
    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "ACT_REF_TYPE_CD", length = 20)
    private String actRefTypeCd;

    @Column(name = "ADD_USER_ID", precision = 20)
    private BigDecimal addUserId;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "ADI_900_STATUS_CD", length = 20)
    private String adi900StatusCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "ADI_COMPLEXION", length = 20)
    private String adiComplexion;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "ADI_EHARS_ID", length = 10)
    private String adiEharsId;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "ADI_HAIR", length = 20)
    private String adiHair;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "ADI_HEIGHT", length = 20)
    private String adiHeight;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "ADI_HEIGHT_LEGACY_CASE", length = 20)
    private String adiHeightLegacyCase;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADI_OTHER_IDENTIFYING_INFO", length = 2000)
    private String adiOtherIdentifyingInfo;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "ADI_SIZE_BUILD", length = 20)
    private String adiSizeBuild;

    @Column(name = "CA_INIT_INTVWR_ASSGN_DT")
    private Instant caInitIntvwrAssgnDt;

    @Column(name = "CA_INTERVIEWER_ASSIGN_DT")
    private Instant caInterviewerAssignDt;

    @jakarta.validation.constraints.Size(max = 29)
    @Column(name = "CA_PATIENT_INTV_STATUS", length = 29)
    private String caPatientIntvStatus;

    @Column(name = "CASE_OID", precision = 20)
    private BigDecimal caseOid;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "CASE_REVIEW_STATUS", length = 20)
    private String caseReviewStatus;

    @Column(name = "CASE_REVIEW_STATUS_DATE")
    private Instant caseReviewStatusDate;

    @Column(name = "CC_CLOSED_DT")
    private Instant ccClosedDt;

    @Column(name = "D_CASE_MANAGEMENT_KEY")
    private Double dCaseManagementKey;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "EPI_LINK_ID", length = 20)
    private String epiLinkId;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "FIELD_FOLL_UP_OOJ_OUTCOME", length = 20)
    private String fieldFollUpOojOutcome;

    @jakarta.validation.constraints.Size(max = 15)
    @Column(name = "FL_FUP_ACTUAL_REF_TYPE", length = 15)
    private String flFupActualRefType;

    @Column(name = "FL_FUP_DISPO_DT")
    private Instant flFupDispoDt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "FL_FUP_DISPOSITION_CD", length = 20)
    private String flFupDispositionCd;

    @jakarta.validation.constraints.Size(max = 44)
    @Column(name = "FL_FUP_DISPOSITION_DESC", length = 44)
    private String flFupDispositionDesc;

    @Column(name = "FL_FUP_EXAM_DT")
    private Instant flFupExamDt;

    @Column(name = "FL_FUP_EXPECTED_DT")
    private Instant flFupExpectedDt;

    @jakarta.validation.constraints.Size(max = 3)
    @Column(name = "FL_FUP_EXPECTED_IN_IND", length = 3)
    private String flFupExpectedInInd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "FL_FUP_FIELD_RECORD_NUM", length = 20)
    private String flFupFieldRecordNum;

    @Column(name = "FL_FUP_INIT_ASSGN_DT")
    private Instant flFupInitAssgnDt;

    @jakarta.validation.constraints.Size(max = 41)
    @Column(name = "FL_FUP_INTERNET_OUTCOME", length = 41)
    private String flFupInternetOutcome;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "FL_FUP_INTERNET_OUTCOME_CD", length = 10)
    private String flFupInternetOutcomeCd;

    @Column(name = "FL_FUP_INVESTIGATOR_ASSGN_DT")
    private Instant flFupInvestigatorAssgnDt;

    @jakarta.validation.constraints.Size(max = 15)
    @Column(name = "FL_FUP_NOTIFICATION_PLAN_CD", length = 15)
    private String flFupNotificationPlanCd;

    @jakarta.validation.constraints.Size(max = 44)
    @Column(name = "FL_FUP_OOJ_OUTCOME", length = 44)
    private String flFupOojOutcome;

    @jakarta.validation.constraints.Size(max = 3)
    @Column(name = "FL_FUP_PROV_DIAGNOSIS", length = 3)
    private String flFupProvDiagnosis;

    @jakarta.validation.constraints.Size(max = 43)
    @Column(name = "FL_FUP_PROV_EXM_REASON", length = 43)
    private String flFupProvExmReason;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "FLD_FOLL_UP_EXPECTED_IN", length = 20)
    private String fldFollUpExpectedIn;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "FLD_FOLL_UP_NOTIFICATION_PLAN", length = 20)
    private String fldFollUpNotificationPlan;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "FLD_FOLL_UP_PROV_DIAGNOSIS", length = 20)
    private String fldFollUpProvDiagnosis;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "FLD_FOLL_UP_PROV_EXM_REASON", length = 20)
    private String fldFollUpProvExmReason;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "INIT_FUP_CLINIC_CODE", length = 50)
    private String initFupClinicCode;

    @Column(name = "INIT_FUP_CLOSED_DT")
    private Instant initFupClosedDt;

    @jakarta.validation.constraints.Size(max = 22)
    @Column(name = "INIT_FUP_INITIAL_FOLL_UP", length = 22)
    private String initFupInitialFollUp;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "INIT_FUP_INITIAL_FOLL_UP_CD", length = 20)
    private String initFupInitialFollUpCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "INIT_FUP_INTERNET_FOLL_UP_CD", length = 20)
    private String initFupInternetFollUpCd;

    @jakarta.validation.constraints.Size(max = 36)
    @Column(name = "INIT_FOLL_UP_NOTIFIABLE", length = 36)
    private String initFollUpNotifiable;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "INIT_FUP_NOTIFIABLE_CD", length = 20)
    private String initFupNotifiableCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "INITIATING_AGNCY", length = 20)
    private String initiatingAgncy;

    @jakarta.validation.constraints.Size(max = 3)
    @Column(name = "INTERNET_FOLL_UP", length = 3)
    private String internetFollUp;

    @Column(name = "INVESTIGATION_KEY", precision = 20)
    private BigDecimal investigationKey;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "OOJ_AGENCY", length = 20)
    private String oojAgency;

    @Column(name = "OOJ_DUE_DATE")
    private Instant oojDueDate;

    @Column(name = "OOJ_INITG_AGNCY_OUTC_DUE_DATE")
    private Instant oojInitgAgncyOutcDueDate;

    @Column(name = "OOJ_INITG_AGNCY_OUTC_SNT_DATE")
    private Instant oojInitgAgncyOutcSntDate;

    @Column(name = "OOJ_INITG_AGNCY_RECD_DATE")
    private Instant oojInitgAgncyRecdDate;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "OOJ_NUMBER", length = 20)
    private String oojNumber;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "PAT_INTV_STATUS_CD", length = 20)
    private String patIntvStatusCd;

    @jakarta.validation.constraints.Size(max = 44)
    @Column(name = "STATUS_900", length = 44)
    private String status900;

    @Column(name = "SURV_CLOSED_DT")
    private Instant survClosedDt;

    @Column(name = "SURV_INVESTIGATOR_ASSGN_DT")
    private Instant survInvestigatorAssgnDt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "SURV_PATIENT_FOLL_UP", length = 20)
    private String survPatientFollUp;

    @jakarta.validation.constraints.Size(max = 22)
    @Column(name = "SURV_PATIENT_FOLL_UP_CD", length = 22)
    private String survPatientFollUpCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "SURV_PROV_EXM_REASON", length = 20)
    private String survProvExmReason;

    @jakarta.validation.constraints.Size(max = 27)
    @Column(name = "SURV_PROVIDER_CONTACT", length = 27)
    private String survProviderContact;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "SURV_PROVIDER_CONTACT_CD", length = 20)
    private String survProviderContactCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "SURV_PROVIDER_DIAGNOSIS", length = 20)
    private String survProviderDiagnosis;

    @jakarta.validation.constraints.Size(max = 43)
    @Column(name = "SURV_PROVIDER_EXAM_REASON", length = 43)
    private String survProviderExamReason;

}