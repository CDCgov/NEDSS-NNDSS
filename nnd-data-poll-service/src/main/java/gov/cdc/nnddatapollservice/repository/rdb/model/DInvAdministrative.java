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
@Table(name = "D_INV_ADMINISTRATIVE")
public class DInvAdministrative {
    @Column(name = "D_INV_ADMINISTRATIVE_KEY")
    private Double dInvAdministrativeKey;

    @Column(name = "nbs_case_answer_uid", precision = 21)
    private BigDecimal nbsCaseAnswerUid;

    @Column(name = "ADM_ABSTRACTION_DT")
    private LocalDate admAbstractionDt;

    @Column(name = "ADM_CASE_REPORT_TO_CDC_DT")
    private LocalDate admCaseReportToCdcDt;

    @Column(name = "ADM_FINAL_INTERVIEW_DT")
    private LocalDate admFinalInterviewDt;

    @Column(name = "ADM_FIRST_RPT_TO_PHD_DT")
    private LocalDate admFirstRptToPhdDt;

    @Column(name = "ADM_INNC_NOTIFICATION_DT")
    private LocalDate admInncNotificationDt;

    @Column(name = "ADM_LST_DT_AS_FDHDNLR")
    private LocalDate admLstDtAsFdhdnlr;

    @Column(name = "ADM_PUI_REPORT_TO_CDC_DT")
    private LocalDate admPuiReportToCdcDt;

    @Column(name = "ADM_US_ARRIVAL_DT")
    private LocalDate admUsArrivalDt;

    @Column(name = "IX_DATE")
    private LocalDate ixDate;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_ADV_EVE_IND", length = 1999)
    private String admAdvEveInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_BINATIONAL_RPTNG_CRIT", length = 1999)
    private String admBinationalRptngCrit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_CASE_IDENTIFY_PROCESS", length = 1999)
    private String admCaseIdentifyProcess;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_CASE_IDENTIFY_PROCESS_OTH", length = 1999)
    private String admCaseIdentifyProcessOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_CASE_STATUS_REASON", length = 1999)
    private String admCaseStatusReason;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_CASE_VERIFCTON_CAT", length = 1999)
    private String admCaseVerifctonCat;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_CNTRY_VERFD_CASE", length = 1999)
    private String admCntryVerfdCase;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_CRF_COMPLETION_STATUS", length = 1999)
    private String admCrfCompletionStatus;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_CSE_BINATINL_REP_CRIT", length = 1999)
    private String admCseBinatinlRepCrit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_DISSEMINATED_IND", length = 1999)
    private String admDisseminatedInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_FDHNDLR_AFTR_ILLNESS", length = 1999)
    private String admFdhndlrAftrIllness;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_IMM_NTNL_NTFBL_CNDTN", length = 1999)
    private String admImmNtnlNtfblCndtn;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_INFO_SOURCE", length = 1999)
    private String admInfoSource;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_INFO_SOURCE_OTH", length = 1999)
    private String admInfoSourceOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_INSIDE_CITY_LIMIT", length = 1999)
    private String admInsideCityLimit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_MOVED_DIFF_RPT_AREA", length = 1999)
    private String admMovedDiffRptArea;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_MOVED_OUT_OF_CNTRY", length = 1999)
    private String admMovedOutOfCntry;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_MOVED_OUT_OF_STATE", length = 1999)
    private String admMovedOutOfState;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_NK1_RELATIONSHIP", length = 1999)
    private String admNk1Relationship;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_PAT_PROCESS_STATUS", length = 1999)
    private String admPatProcessStatus;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_PAT_TRT_MDR_CASE", length = 1999)
    private String admPatTrtMdrCase;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_PREV_COUNT_CASE", length = 1999)
    private String admPrevCountCase;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_PUBLISHED_INDICATOR", length = 1999)
    private String admPublishedIndicator;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_REASON_NO_DISPO", length = 1999)
    private String admReasonNoDispo;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_REFERRAL_BASIS_OOJ", length = 1999)
    private String admReferralBasisOoj;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_REMAIN_US_AFTR_RPT", length = 1999)
    private String admRemainUsAftrRpt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_RPTNG_CNTY", length = 1999)
    private String admRptngCnty;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_SAMPLED_FOR_ENHC_INV", length = 1999)
    private String admSampledForEnhcInv;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_TB_DIAG_STATUS", length = 1999)
    private String admTbDiagStatus;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_TRANSLATOR_REQ_IND", length = 1999)
    private String admTranslatorReqInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_TRANSNATIONAL_REF", length = 1999)
    private String admTransnationalRef;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_US_BORN", length = 1999)
    private String admUsBorn;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_ABSTRACTOR_NM", length = 2000)
    private String admAbstractorNm;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_CDC_ASSIGNED_ID", length = 2000)
    private String admCdcAssignedId;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_CITY_CNTY_CASE_NBR", length = 2000)
    private String admCityCntyCaseNbr;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_DGMQ_ID", length = 2000)
    private String admDgmqId;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_FDHNDLR_WORKING_LOCTN", length = 2000)
    private String admFdhndlrWorkingLoctn;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_IMMEDIATE_NND_DESC", length = 2000)
    private String admImmediateNndDesc;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_NOTIF_COMMENT", length = 2000)
    private String admNotifComment;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_NTSS_STATE_CASE_NBR", length = 2000)
    private String admNtssStateCaseNbr;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_PREFERRED_LANGUAGE", length = 2000)
    private String admPreferredLanguage;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_PREV_REPRT_ST_CSE_NBR", length = 2000)
    private String admPrevReprtStCseNbr;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_RPTD_STT_CSE_NBR1", length = 2000)
    private String admRptdSttCseNbr1;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_RPTD_STT_CSE_NBR2", length = 2000)
    private String admRptdSttCseNbr2;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_RPTD_STT_CSE_NBR3", length = 2000)
    private String admRptdSttCseNbr3;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_RPTD_STT_CSE_NBR4", length = 2000)
    private String admRptdSttCseNbr4;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "ADM_RPTD_STT_CSE_NBR5", length = 2000)
    private String admRptdSttCseNbr5;

}