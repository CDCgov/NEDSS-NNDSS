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
@Table(name = "Public_health_case")
public class PublicHealthCase {
    @Id
    @Column(name = "public_health_case_uid", nullable = false)
    private Long id;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "activity_duration_amt", length = 20)
    private String activityDurationAmt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "activity_duration_unit_cd", length = 20)
    private String activityDurationUnitCd;

    @Column(name = "activity_from_time")
    private Instant activityFromTime;

    @Column(name = "activity_to_time")
    private Instant activityToTime;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "add_reason_cd", length = 20)
    private String addReasonCd;

    @Column(name = "add_time")
    private Instant addTime;

    @Column(name = "add_user_id")
    private Long addUserId;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "case_class_cd", length = 20)
    private String caseClassCd;

    @Column(name = "case_type_cd")
    private Character caseTypeCd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "cd", length = 50)
    private String cd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "cd_desc_txt", length = 100)
    private String cdDescTxt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "cd_system_cd", length = 20)
    private String cdSystemCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "cd_system_desc_txt", length = 100)
    private String cdSystemDescTxt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "confidentiality_cd", length = 20)
    private String confidentialityCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "confidentiality_desc_txt", length = 100)
    private String confidentialityDescTxt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "detection_method_cd", length = 20)
    private String detectionMethodCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "detection_method_desc_txt", length = 100)
    private String detectionMethodDescTxt;

    @Column(name = "diagnosis_time")
    private Instant diagnosisTime;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "disease_imported_cd", length = 20)
    private String diseaseImportedCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "disease_imported_desc_txt", length = 100)
    private String diseaseImportedDescTxt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "effective_duration_amt", length = 20)
    private String effectiveDurationAmt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "effective_duration_unit_cd", length = 20)
    private String effectiveDurationUnitCd;

    @Column(name = "effective_from_time")
    private Instant effectiveFromTime;

    @Column(name = "effective_to_time")
    private Instant effectiveToTime;

    @Column(name = "group_case_cnt")
    private Short groupCaseCnt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "investigation_status_cd", length = 20)
    private String investigationStatusCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "jurisdiction_cd", length = 20)
    private String jurisdictionCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "last_chg_reason_cd", length = 20)
    private String lastChgReasonCd;

    @Column(name = "last_chg_time")
    private Instant lastChgTime;

    @Column(name = "last_chg_user_id")
    private Long lastChgUserId;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "local_id", length = 50)
    private String localId;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "mmwr_week", length = 10)
    private String mmwrWeek;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "mmwr_year", length = 10)
    private String mmwrYear;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "outbreak_ind", length = 20)
    private String outbreakInd;

    @Column(name = "outbreak_from_time")
    private Instant outbreakFromTime;

    @Column(name = "outbreak_to_time")
    private Instant outbreakToTime;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "outbreak_name", length = 100)
    private String outbreakName;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "outcome_cd", length = 20)
    private String outcomeCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "pat_age_at_onset", length = 20)
    private String patAgeAtOnset;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "pat_age_at_onset_unit_cd", length = 20)
    private String patAgeAtOnsetUnitCd;

    @Column(name = "patient_group_id")
    private Long patientGroupId;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "prog_area_cd", length = 20)
    private String progAreaCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "record_status_cd", length = 20)
    private String recordStatusCd;

    @Column(name = "record_status_time")
    private Instant recordStatusTime;

    @Column(name = "repeat_nbr")
    private Short repeatNbr;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "rpt_cnty_cd", length = 20)
    private String rptCntyCd;

    @Column(name = "rpt_form_cmplt_time")
    private Instant rptFormCmpltTime;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "rpt_source_cd", length = 20)
    private String rptSourceCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "rpt_source_cd_desc_txt", length = 100)
    private String rptSourceCdDescTxt;

    @Column(name = "rpt_to_county_time")
    private Instant rptToCountyTime;

    @Column(name = "rpt_to_state_time")
    private Instant rptToStateTime;

    @Column(name = "status_cd")
    private Character statusCd;

    @Column(name = "status_time")
    private Instant statusTime;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "transmission_mode_cd", length = 20)
    private String transmissionModeCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "transmission_mode_desc_txt", length = 100)
    private String transmissionModeDescTxt;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "txt", length = 2000)
    private String txt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "user_affiliation_txt", length = 20)
    private String userAffiliationTxt;

    @Column(name = "program_jurisdiction_oid")
    private Long programJurisdictionOid;

    @jakarta.validation.constraints.NotNull
    @Column(name = "shared_ind", nullable = false)
    private Character sharedInd;

    @jakarta.validation.constraints.NotNull
    @Column(name = "version_ctrl_nbr", nullable = false)
    private Short versionCtrlNbr;

    @Column(name = "investigator_assigned_time")
    private Instant investigatorAssignedTime;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "hospitalized_ind_cd", length = 20)
    private String hospitalizedIndCd;

    @Column(name = "hospitalized_admin_time")
    private Instant hospitalizedAdminTime;

    @Column(name = "hospitalized_discharge_time")
    private Instant hospitalizedDischargeTime;

    @Column(name = "hospitalized_duration_amt", precision = 18)
    private BigDecimal hospitalizedDurationAmt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "pregnant_ind_cd", length = 20)
    private String pregnantIndCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "day_care_ind_cd", length = 20)
    private String dayCareIndCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "food_handler_ind_cd", length = 20)
    private String foodHandlerIndCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "imported_country_cd", length = 20)
    private String importedCountryCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "imported_state_cd", length = 20)
    private String importedStateCd;

    @jakarta.validation.constraints.Size(max = 250)
    @Column(name = "imported_city_desc_txt", length = 250)
    private String importedCityDescTxt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "imported_county_cd", length = 20)
    private String importedCountyCd;

    @Column(name = "deceased_time")
    private Instant deceasedTime;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "count_interval_cd", length = 20)
    private String countIntervalCd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "priority_cd", length = 50)
    private String priorityCd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "contact_inv_txt", length = 2000)
    private String contactInvTxt;

    @Column(name = "infectious_from_date")
    private Instant infectiousFromDate;

    @Column(name = "infectious_to_date")
    private Instant infectiousToDate;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "contact_inv_status_cd", length = 50)
    private String contactInvStatusCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "referral_basis_cd", length = 20)
    private String referralBasisCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "curr_process_state_cd", length = 20)
    private String currProcessStateCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "inv_priority_cd", length = 20)
    private String invPriorityCd;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "coinfection_id", length = 50)
    private String coinfectionId;

}