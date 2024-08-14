package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "INVESTIGATION")
public class Investigation {
    @Id
    @Column(name = "INVESTIGATION_KEY", nullable = false)
    private Long id;

    @Column(name = "CASE_OID")
    private Long caseOid;

    @Column(name = "CASE_UID")
    private Long caseUid;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "INV_LOCAL_ID", length = 50)
    private String invLocalId;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "INV_SHARE_IND", length = 50)
    private String invShareInd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "OUTBREAK_NAME", length = 100)
    private String outbreakName;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "INVESTIGATION_STATUS", length = 50)
    private String investigationStatus;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "INV_CASE_STATUS", length = 50)
    private String invCaseStatus;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "CASE_TYPE", length = 50)
    private String caseType;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "INV_COMMENTS", length = 2000)
    private String invComments;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "JURISDICTION_CD", length = 20)
    private String jurisdictionCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "JURISDICTION_NM", length = 100)
    private String jurisdictionNm;

    @Column(name = "EARLIEST_RPT_TO_PHD_DT")
    private Instant earliestRptToPhdDt;

    @Column(name = "ILLNESS_ONSET_DT")
    private Instant illnessOnsetDt;

    @Column(name = "ILLNESS_END_DT")
    private Instant illnessEndDt;

    @Column(name = "INV_RPT_DT")
    private Instant invRptDt;

    @Column(name = "INV_START_DT")
    private Instant invStartDt;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "RPT_SRC_CD_DESC", length = 100)
    private String rptSrcCdDesc;

    @Column(name = "EARLIEST_RPT_TO_CNTY_DT")
    private Instant earliestRptToCntyDt;

    @Column(name = "EARLIEST_RPT_TO_STATE_DT")
    private Instant earliestRptToStateDt;

    @Column(name = "CASE_RPT_MMWR_WK", precision = 18)
    private BigDecimal caseRptMmwrWk;

    @Column(name = "CASE_RPT_MMWR_YR", precision = 18)
    private BigDecimal caseRptMmwrYr;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "DISEASE_IMPORTED_IND", length = 100)
    private String diseaseImportedInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IMPORT_FRM_CNTRY", length = 50)
    private String importFrmCntry;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IMPORT_FRM_STATE", length = 50)
    private String importFrmState;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IMPORT_FRM_CNTY", length = 50)
    private String importFrmCnty;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "IMPORT_FRM_CITY", length = 2000)
    private String importFrmCity;

    @Column(name = "EARLIEST_RPT_TO_CDC_DT")
    private Instant earliestRptToCdcDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "RPT_SRC_CD", length = 50)
    private String rptSrcCd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IMPORT_FRM_CNTRY_CD", length = 50)
    private String importFrmCntryCd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IMPORT_FRM_STATE_CD", length = 50)
    private String importFrmStateCd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IMPORT_FRM_CNTY_CD", length = 50)
    private String importFrmCntyCd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "IMPORT_FRM_CITY_CD", length = 50)
    private String importFrmCityCd;

    @Column(name = "DIAGNOSIS_DT")
    private Instant diagnosisDt;

    @Column(name = "HSPTL_ADMISSION_DT")
    private Instant hsptlAdmissionDt;

    @Column(name = "HSPTL_DISCHARGE_DT")
    private Instant hsptlDischargeDt;

    @Column(name = "HSPTL_DURATION_DAYS", precision = 18)
    private BigDecimal hsptlDurationDays;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "OUTBREAK_IND", length = 50)
    private String outbreakInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "HSPTLIZD_IND", length = 50)
    private String hsptlizdInd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "INV_STATE_CASE_ID", length = 100)
    private String invStateCaseId;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "CITY_COUNTY_CASE_NBR", length = 100)
    private String cityCountyCaseNbr;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "TRANSMISSION_MODE", length = 50)
    private String transmissionMode;

    @jakarta.validation.constraints.Size(max = 8)
    @jakarta.validation.constraints.NotNull
    @Column(name = "RECORD_STATUS_CD", nullable = false, length = 8)
    private String recordStatusCd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_PREGNANT_IND", length = 50)
    private String patientPregnantInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "DIE_FRM_THIS_ILLNESS_IND", length = 50)
    private String dieFrmThisIllnessInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "DAYCARE_ASSOCIATION_IND", length = 50)
    private String daycareAssociationInd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "FOOD_HANDLR_IND", length = 50)
    private String foodHandlrInd;

    @Column(name = "INVESTIGATION_DEATH_DATE")
    private Instant investigationDeathDate;

    @Column(name = "PATIENT_AGE_AT_ONSET", precision = 18)
    private BigDecimal patientAgeAtOnset;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "PATIENT_AGE_AT_ONSET_UNIT", length = 20)
    private String patientAgeAtOnsetUnit;

    @Column(name = "INV_ASSIGNED_DT")
    private Instant invAssignedDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "DETECTION_METHOD_DESC_TXT", length = 50)
    private String detectionMethodDescTxt;

    @Column(name = "ILLNESS_DURATION", precision = 18)
    private BigDecimal illnessDuration;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "ILLNESS_DURATION_UNIT", length = 50)
    private String illnessDurationUnit;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "CONTACT_INV_COMMENTS", length = 2000)
    private String contactInvComments;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "CONTACT_INV_PRIORITY", length = 20)
    private String contactInvPriority;

    @Column(name = "CONTACT_INFECTIOUS_FROM_DATE")
    private Instant contactInfectiousFromDate;

    @Column(name = "CONTACT_INFECTIOUS_TO_DATE")
    private Instant contactInfectiousToDate;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "CONTACT_INV_STATUS", length = 20)
    private String contactInvStatus;

    @Column(name = "INV_CLOSE_DT")
    private Instant invCloseDt;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PROGRAM_AREA_DESCRIPTION", length = 50)
    private String programAreaDescription;

    @Column(name = "ADD_TIME")
    private Instant addTime;

    @Column(name = "LAST_CHG_TIME")
    private Instant lastChgTime;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "INVESTIGATION_ADDED_BY", length = 50)
    private String investigationAddedBy;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "INVESTIGATION_LAST_UPDATED_BY", length = 50)
    private String investigationLastUpdatedBy;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "REFERRAL_BASIS", length = 100)
    private String referralBasis;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "CURR_PROCESS_STATE", length = 100)
    private String currProcessState;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "INV_PRIORITY_CD", length = 100)
    private String invPriorityCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "COINFECTION_ID", length = 100)
    private String coinfectionId;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "LEGACY_CASE_ID", length = 100)
    private String legacyCaseId;

    @jakarta.validation.constraints.Size(max = 300)
    @Column(name = "OUTBREAK_NAME_DESC", length = 300)
    private String outbreakNameDesc;

}