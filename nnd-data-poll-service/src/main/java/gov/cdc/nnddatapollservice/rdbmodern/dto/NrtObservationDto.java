package gov.cdc.nnddatapollservice.rdbmodern.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class NrtObservationDto {

    @SerializedName("observation_uid")
    private Long observationUid;

    @SerializedName("class_cd")
    private String classCd;

    @SerializedName("mood_cd")
    private String moodCd;

    @SerializedName("act_uid")
    private Long actUid;

    @SerializedName("cd_desc_text")
    private String cdDescText;

    @SerializedName("record_status_cd")
    private String recordStatusCd;

    @SerializedName("jurisdiction_cd")
    private String jurisdictionCd;

    @SerializedName("program_jurisdiction_oid")
    private Long programJurisdictionOid;

    @SerializedName("prog_area_cd")
    private String progAreaCd;

    @SerializedName("pregnant_ind_cd")
    private String pregnantIndCd;

    @SerializedName("local_id")
    private String localId;

    @SerializedName("activity_to_time")
    private String activityToTime;

    @SerializedName("effective_from_time")
    private String effectiveFromTime;

    @SerializedName("rpt_to_state_time")
    private String rptToStateTime;

    @SerializedName("electronic_ind")
    private String electronicInd;

    @SerializedName("version_ctrl_nbr")
    private Short versionCtrlNbr;

    @SerializedName("ordering_person_id")
    private Long orderingPersonId;

    @SerializedName("patient_id")
    private Long patientId;

    @SerializedName("result_observation_uid")
    private String resultObservationUid;

    @SerializedName("author_organization_id")
    private Long authorOrganizationId;

    @SerializedName("ordering_organization_id")
    private Long orderingOrganizationId;

    @SerializedName("performing_organization_id")
    private Long performingOrganizationId;

    @SerializedName("material_id")
    private Long materialId;

    @SerializedName("obs_domain_cd_st_1")
    private String obsDomainCdSt1;

    @SerializedName("processing_decision_cd")
    private String processingDecisionCd;

    @SerializedName("cd")
    private String cd;

    @SerializedName("shared_ind")
    private String sharedInd;

    @SerializedName("add_user_id")
    private Long addUserId;

    @SerializedName("add_user_name")
    private String addUserName;

    @SerializedName("add_time")
    private String addTime;

    @SerializedName("last_chg_user_id")
    private Long lastChgUserId;

    @SerializedName("last_chg_user_name")
    private String lastChgUserName;

    @SerializedName("last_chg_time")
    private String lastChgTime;

    @SerializedName("ctrl_cd_display_form")
    private String ctrlCdDisplayForm;

    @SerializedName("status_cd")
    private String statusCd;

    @SerializedName("cd_system_cd")
    private String cdSystemCd;

    @SerializedName("cd_system_desc_txt")
    private String cdSystemDescTxt;

    @SerializedName("ctrl_cd_user_defined_1")
    private String ctrlCdUserDefined1;

    @SerializedName("alt_cd")
    private String altCd;

    @SerializedName("alt_cd_desc_txt")
    private String altCdDescTxt;

    @SerializedName("alt_cd_system_cd")
    private String altCdSystemCd;

    @SerializedName("alt_cd_system_desc_txt")
    private String altCdSystemDescTxt;

    @SerializedName("method_cd")
    private String methodCd;

    @SerializedName("method_desc_txt")
    private String methodDescTxt;

    @SerializedName("target_site_cd")
    private String targetSiteCd;

    @SerializedName("target_site_desc_txt")
    private String targetSiteDescTxt;

    @SerializedName("txt")
    private String txt;

    @SerializedName("interpretation_cd")
    private String interpretationCd;

    @SerializedName("interpretation_desc_txt")
    private String interpretationDescTxt;

    @SerializedName("report_observation_id")
    private Long reportObservationUid;

    @SerializedName("followup_observation_id")
    private String followupObservationUid;

    @SerializedName("report_refr_id")
    private Long reportRefrUid;

    @SerializedName("report_sprt_id")
    private Long reportSprtUid;

    @SerializedName("morb_physician_id")
    private Long morbPhysicianId;

    @SerializedName("morb_reporter_id")
    private Long morbReporterId;

    @SerializedName("transcriptionist_id")
    private Long transcriptionistId;

    @SerializedName("transcriptionist_val")
    private String transcriptionistVal;

    @SerializedName("transcriptionist_first_nm")
    private String transcriptionistFirstNm;

    @SerializedName("transcriptionist_last_nm")
    private String transcriptionistLastNm;

    @SerializedName("assistant_interpreter_id")
    private Long assistantInterpreterId;

    @SerializedName("assistant_interpreter_val")
    private String assistantInterpreterVal;

    @SerializedName("assistant_interpreter_first_nm")
    private String assistantInterpreterFirstNm;

    @SerializedName("assistant_interpreter_last_nm")
    private String assistantInterpreterLastNm;

    @SerializedName("result_interpreter_id")
    private Long resultInterpreterId;

    @SerializedName("specimen_collector_id")
    private Long specimenCollectorId;

    @SerializedName("copy_to_provider_id")
    private Long copyToProviderId;

    @SerializedName("lab_test_technician_id")
    private Long labTestTechnicianId;

    @SerializedName("health_care_id")
    private Long healthCareId;

    @SerializedName("morb_hosp_reporter_id")
    private Long morbHospReporterId;

    @SerializedName("refresh_datetime")
    private String refreshDatetime;

    @SerializedName("max_datetime")
    private String maxDatetime;
}
