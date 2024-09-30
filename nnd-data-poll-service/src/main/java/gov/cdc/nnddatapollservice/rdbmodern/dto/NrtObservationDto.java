package gov.cdc.nnddatapollservice.rdbmodern.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NrtObservationDto {

    private Integer observationUid;
    private String classCd;
    private String moodCd;
    private Integer actUid;
    private String cdDescText;
    private String recordStatusCd;
    private String jurisdictionCd;
    private Integer programJurisdictionOid;
    private String progAreaCd;
    private String pregnantIndCd;
    private String localId;
    private Timestamp activityToTime;
    private Timestamp effectiveFromTime;
    private Timestamp rptToStateTime;
    private String electronicInd;
    private Short versionCtrlNbr;
    private Integer orderingPersonId;
    private Integer patientId;
    private String resultObservationUid;
    private Integer authorOrganizationId;
    private Integer orderingOrganizationId;
    private Integer performingOrganizationId;
    private Integer materialId;
    private Integer addUserId;
    private String addUserName;
    private Timestamp addTime;
    private Integer lastChgUserId;
    private String lastChgUserName;
    private Timestamp lastChgTime;
    private Timestamp refreshDatetime;
    private Timestamp maxDatetime;
    private String cd;
    private String sharedInd;
    private String ctrlCdDisplayForm;
    private String obsDomainCdSt1;
    private String processingDecisionCd;
    private String statusCd;
    private String cdSystemCd;
    private String cdSystemDescTxt;
    private String ctrlCdUserDefined1;
    private String altCd;
    private String altCdDescTxt;
    private String altCdSystemCd;
    private String altCdSystemDescTxt;
    private String methodCd;
    private String methodDescTxt;
    private String targetSiteCd;
    private String targetSiteDescTxt;
    private String txt;
    private String interpretationCd;
    private String interpretationDescTxt;
    private Integer reportObservationUid;
    private String followupObservationUid;
    private Integer reportRefrUid;
    private Integer reportSprtUid;
    private Integer morbPhysicianId;
    private Integer morbReporterId;
    private Integer transcriptionistId;
    private String transcriptionistVal;
    private String transcriptionistFirstNm;
    private String transcriptionistLastNm;
    private Integer assistantInterpreterId;
    private String assistantInterpreterVal;
    private String assistantInterpreterFirstNm;
    private String assistantInterpreterLastNm;
    private Integer resultInterpreterId;
    private Integer specimenCollectorId;
    private Integer copyToProviderId;
    private Integer labTestTechnicianId;
    private Integer healthCareId;
    private Integer morbHospReporterId;
}
