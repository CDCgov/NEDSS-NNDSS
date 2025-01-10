package gov.cdc.nnddatapollservice.repository.rdb_modern.model;

import gov.cdc.nnddatapollservice.rdbmodern.dto.NrtObservationDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "nrt_observation", schema = "dbo")
public class NrtObservation {

    @Id
    @Column(name = "observation_uid", nullable = false)
    private Long observationUid;

    @Column(name = "class_cd", length = 10)
    private String classCd;

    @Column(name = "mood_cd", length = 10)
    private String moodCd;

    @Column(name = "act_uid")
    private Long actUid;

    @Column(name = "cd_desc_txt", length = 1000)
    private String cdDescTxt;

    @Column(name = "record_status_cd", length = 20)
    private String recordStatusCd;

    @Column(name = "jurisdiction_cd", length = 20)
    private String jurisdictionCd;

    @Column(name = "program_jurisdiction_oid")
    private Long programJurisdictionOid;

    @Column(name = "prog_area_cd", length = 20)
    private String progAreaCd;

    @Column(name = "pregnant_ind_cd", length = 20)
    private String pregnantIndCd;

    @Column(name = "local_id", length = 50)
    private String localId;

    @Column(name = "activity_to_time")
    private Timestamp activityToTime;

    @Column(name = "effective_from_time")
    private Timestamp effectiveFromTime;

    @Column(name = "rpt_to_state_time")
    private Timestamp rptToStateTime;

    @Column(name = "electronic_ind", length = 1)
    private String electronicInd;

    @Column(name = "version_ctrl_nbr", nullable = false)
    private Short versionCtrlNbr;

    @Column(name = "ordering_person_id")
    private Long orderingPersonId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "result_observation_uid", columnDefinition = "nvarchar(max)")
    private String resultObservationUid;

    @Column(name = "author_organization_id")
    private Long authorOrganizationId;

    @Column(name = "ordering_organization_id")
    private Long orderingOrganizationId;

    @Column(name = "performing_organization_id")
    private Long performingOrganizationId;

    @Column(name = "material_id")
    private Long materialId;

    @Column(name = "obs_domain_cd_st_1", length = 20)
    private String obsDomainCdSt1;

    @Column(name = "processing_decision_cd", length = 20)
    private String processingDecisionCd;

    @Column(name = "cd", length = 50)
    private String cd;

    @Column(name = "shared_ind", length = 1)
    private String sharedInd;

    @Column(name = "add_user_id")
    private Long addUserId;

    @Column(name = "add_user_name", length = 50)
    private String addUserName;

    @Column(name = "add_time")
    private Timestamp addTime;

    @Column(name = "last_chg_user_id")
    private Long lastChgUserId;

    @Column(name = "last_chg_user_name", length = 50)
    private String lastChgUserName;

    @Column(name = "last_chg_time")
    private Timestamp lastChgTime;

    @Column(name = "ctrl_cd_display_form", length = 20)
    private String ctrlCdDisplayForm;

    @Column(name = "status_cd", length = 1)
    private String statusCd;

    @Column(name = "cd_system_cd", length = 50)
    private String cdSystemCd;

    @Column(name = "cd_system_desc_txt", length = 100)
    private String cdSystemDescTxt;

    @Column(name = "ctrl_cd_user_defined_1", length = 20)
    private String ctrlCdUserDefined1;

    @Column(name = "alt_cd", length = 50)
    private String altCd;

    @Column(name = "alt_cd_desc_txt", length = 1000)
    private String altCdDescTxt;

    @Column(name = "alt_cd_system_cd", length = 1000)
    private String altCdSystemCd;

    @Column(name = "alt_cd_system_desc_txt", length = 100)
    private String altCdSystemDescTxt;

    @Column(name = "method_cd", length = 2000)
    private String methodCd;

    @Column(name = "method_desc_txt", length = 2000)
    private String methodDescTxt;

    @Column(name = "target_site_cd", length = 20)
    private String targetSiteCd;

    @Column(name = "target_site_desc_txt", length = 100)
    private String targetSiteDescTxt;

    @Column(name = "txt", length = 1000)
    private String txt;

    @Column(name = "interpretation_cd", length = 20)
    private String interpretationCd;

    @Column(name = "interpretation_desc_txt", length = 100)
    private String interpretationDescTxt;

    @Column(name = "report_observation_uid")
    private Long reportObservationUid;

    @Column(name = "followup_observation_uid", columnDefinition = "nvarchar(max)")
    private String followupObservationUid;

    @Column(name = "report_refr_uid")
    private Long reportRefrUid;

    @Column(name = "report_sprt_uid")
    private Long reportSprtUid;

    @Column(name = "morb_physician_id")
    private Long morbPhysicianId;

    @Column(name = "morb_reporter_id")
    private Long morbReporterId;

    @Column(name = "transcriptionist_id")
    private Long transcriptionistId;

    @Column(name = "transcriptionist_val", length = 20)
    private String transcriptionistVal;

    @Column(name = "transcriptionist_first_nm", length = 50)
    private String transcriptionistFirstNm;

    @Column(name = "transcriptionist_last_nm", length = 50)
    private String transcriptionistLastNm;

    @Column(name = "assistant_interpreter_id")
    private Long assistantInterpreterId;

    @Column(name = "assistant_interpreter_val", length = 20)
    private String assistantInterpreterVal;

    @Column(name = "assistant_interpreter_first_nm", length = 50)
    private String assistantInterpreterFirstNm;

    @Column(name = "assistant_interpreter_last_nm", length = 50)
    private String assistantInterpreterLastNm;

    @Column(name = "result_interpreter_id")
    private Long resultInterpreterId;

    @Column(name = "specimen_collector_id")
    private Long specimenCollectorId;

    @Column(name = "copy_to_provider_id")
    private Long copyToProviderId;

    @Column(name = "lab_test_technician_id")
    private Long labTestTechnicianId;

    @Column(name = "health_care_id")
    private Long healthCareId;

    @Column(name = "morb_hosp_reporter_id")
    private Long morbHospReporterId;

    @Column(name = "priority_cd", length = 20)
    private String priorityCd;

    @Column(name = "transcriptionist_auth_type")
    private String transcriptionistAuthType;

    @Column(name = "transcriptionist_id_assign_auth")
    private String transcriptionistIdAssignAuth;

    @Column(name = "assistant_interpreter_auth_type")
    private String assistantInterpreterAuthType;

    @Column(name = "assistant_interpreter_id_assign_auth")
    private String assistantInterpreterIdAssignAuth;

    @Column(name = "accession_number")
    private String accessionNumber;

    @Column(name = "morb_hosp_id")
    private Long morbHospId;

//    @Column(name = "refresh_datetime", columnDefinition = "datetime2(7) GENERATED ALWAYS AS ROW START NOT NULL", updatable = false)
//    private Timestamp refreshDatetime;
//
//    @Column(name = "max_datetime", columnDefinition = "datetime2(7) GENERATED ALWAYS AS ROW END HIDDEN NOT NULL", updatable = false)
//    private Timestamp maxDatetime;

    public NrtObservation() {

    }
    public NrtObservation(NrtObservationDto dto) throws ParseException {
        this.observationUid = dto.getObservationUid();
        this.classCd = dto.getClassCd();
        this.moodCd = dto.getMoodCd();
        this.actUid = dto.getActUid();
        this.cdDescTxt = dto.getCdDescTxt();
        this.recordStatusCd = dto.getRecordStatusCd();
        this.jurisdictionCd = dto.getJurisdictionCd();
        this.programJurisdictionOid = dto.getProgramJurisdictionOid();
        this.progAreaCd = dto.getProgAreaCd();
        this.pregnantIndCd = dto.getPregnantIndCd();
        this.localId = dto.getLocalId();
        this.activityToTime = parseTimestamp(dto.getActivityToTime());
        this.effectiveFromTime = parseTimestamp(dto.getEffectiveFromTime());
        this.rptToStateTime = parseTimestamp(dto.getRptToStateTime());
        this.electronicInd = dto.getElectronicInd();
        this.versionCtrlNbr = dto.getVersionCtrlNbr();
        this.orderingPersonId = dto.getOrderingPersonId();
        this.patientId = dto.getPatientId();
        this.resultObservationUid = dto.getResultObservationUid();
        this.authorOrganizationId = dto.getAuthorOrganizationId();
        this.orderingOrganizationId = dto.getOrderingOrganizationId();
        this.performingOrganizationId = dto.getPerformingOrganizationId();
        this.materialId = dto.getMaterialId();
        this.obsDomainCdSt1 = dto.getObsDomainCdSt1();
        this.processingDecisionCd = dto.getProcessingDecisionCd();
        this.cd = dto.getCd();
        this.sharedInd = dto.getSharedInd();
        this.addUserId = dto.getAddUserId();
        this.addUserName = dto.getAddUserName();
        this.addTime = parseTimestamp(dto.getAddTime());
        this.lastChgUserId = dto.getLastChgUserId();
        this.lastChgUserName = dto.getLastChgUserName();
        this.lastChgTime = parseTimestamp(dto.getLastChgTime());
        this.ctrlCdDisplayForm = dto.getCtrlCdDisplayForm();
        this.statusCd = dto.getStatusCd();
        this.cdSystemCd = dto.getCdSystemCd();
        this.cdSystemDescTxt = dto.getCdSystemDescTxt();
        this.ctrlCdUserDefined1 = dto.getCtrlCdUserDefined1();
        this.altCd = dto.getAltCd();
        this.altCdDescTxt = dto.getAltCdDescTxt();
        this.altCdSystemCd = dto.getAltCdSystemCd();
        this.altCdSystemDescTxt = dto.getAltCdSystemDescTxt();
        this.methodCd = dto.getMethodCd();
        this.methodDescTxt = dto.getMethodDescTxt();
        this.targetSiteCd = dto.getTargetSiteCd();
        this.targetSiteDescTxt = dto.getTargetSiteDescTxt();
        this.txt = dto.getTxt();
        this.interpretationCd = dto.getInterpretationCd();
        this.interpretationDescTxt = dto.getInterpretationDescTxt();
        this.transcriptionistId = dto.getTranscriptionistId();
        this.transcriptionistVal = dto.getTranscriptionistVal(); // Added this field
        this.transcriptionistFirstNm = dto.getTranscriptionistFirstNm();
        this.transcriptionistLastNm = dto.getTranscriptionistLastNm();
        this.assistantInterpreterId = dto.getAssistantInterpreterId();
        this.assistantInterpreterVal = dto.getAssistantInterpreterVal(); // Added this field
        this.assistantInterpreterFirstNm = dto.getAssistantInterpreterFirstNm();
        this.assistantInterpreterLastNm = dto.getAssistantInterpreterLastNm();
        this.resultInterpreterId = dto.getResultInterpreterId();
        this.specimenCollectorId = dto.getSpecimenCollectorId();
        this.copyToProviderId = dto.getCopyToProviderId();
        this.labTestTechnicianId = dto.getLabTestTechnicianId();
        this.healthCareId = dto.getHealthCareId(); // Added this field
        this.morbHospReporterId = dto.getMorbHospReporterId(); // Added this field
        this.priorityCd = dto.getPriorityCd(); // Added this field
        this.transcriptionistAuthType= dto.getTranscriptionistAuthType();
        this.transcriptionistIdAssignAuth= dto.getTranscriptionistIdAssignAuth();
        this.assistantInterpreterAuthType= dto.getAssistantInterpreterAuthType();
        this.assistantInterpreterIdAssignAuth= dto.getAssistantInterpreterIdAssignAuth();
        this.accessionNumber= dto.getAccessionNumber();
        this.morbHospId= dto.getMorbHospId();
        this.reportObservationUid=dto.getReportObservationUid();
        this.followupObservationUid=dto.getFollowupObservationUid();
        this.reportRefrUid=dto.getReportRefrUid();
        this.reportSprtUid=dto.getReportSprtUid();
        this.morbPhysicianId=dto.getMorbPhysicianId();
        this.morbReporterId=dto.getMorbReporterId();
    }

    private Timestamp parseTimestamp(String dateString) throws ParseException {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return new Timestamp(dateFormat.parse(dateString).getTime());
    }

}
