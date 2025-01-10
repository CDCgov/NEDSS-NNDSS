package gov.cdc.nnddatapollservice.repository.nbs_odse.model;

import gov.cdc.nnddatapollservice.nbs_odse.dto.EDXActivityLogDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EDX_activity_log")
public class EDXActivityLog {

    @Id
    @Column(name = "edx_activity_log_uid", nullable = false)
    private Long edxActivityLogUid;

    @Column(name = "source_uid")
    private Long sourceUid;

    @Column(name = "target_uid")
    private Long targetUid;

    @Column(name = "doc_type", length = 50)
    private String docType;

    @Column(name = "record_status_cd", length = 20)
    private String recordStatusCd;

    @Column(name = "record_status_time")
    private Timestamp recordStatusTime;

    @Lob
    @Column(name = "exception_txt")
    private String exceptionTxt;

    @Column(name = "imp_exp_ind_cd", length = 1)
    private String impExpIndCd;

    @Column(name = "source_type_cd", length = 50)
    private String sourceTypeCd;

    @Column(name = "target_type_cd", length = 50)
    private String targetTypeCd;

    @Column(name = "business_obj_localId", length = 50)
    private String businessObjLocalId;

    @Column(name = "doc_nm", length = 250)
    private String docNm;

    @Column(name = "source_nm", length = 250)
    private String sourceNm;

    @Column(name = "algorithm_action", length = 10)
    private String algorithmAction;

    @Column(name = "algorithm_name", length = 250)
    private String algorithmName;

    @Column(name = "Message_id", length = 255)
    private String messageId;

    @Column(name = "Entity_nm", length = 255)
    private String entityNm;

    @Column(name = "Accession_nbr", length = 100)
    private String accessionNbr;

    public EDXActivityLog(EDXActivityLogDto dto) {
        this.edxActivityLogUid = dto.getEdxActivityLogUid();
        this.sourceUid = dto.getSourceUid();
        this.targetUid = dto.getTargetUid();
        this.docType = dto.getDocType();
        this.recordStatusCd = dto.getRecordStatusCd();
        this.recordStatusTime = dto.getRecordStatusTime();
        this.exceptionTxt = dto.getExceptionTxt();
        this.impExpIndCd = dto.getImpExpIndCd();
        this.sourceTypeCd = dto.getSourceTypeCd();
        this.targetTypeCd = dto.getTargetTypeCd();
        this.businessObjLocalId = dto.getBusinessObjLocalId();
        this.docNm = dto.getDocNm();
        this.sourceNm = dto.getSourceNm();
        this.algorithmAction = dto.getAlgorithmAction();
        this.algorithmName = dto.getAlgorithmName();
        this.messageId = dto.getMessageId();
        this.entityNm = dto.getEntityNm();
        this.accessionNbr = dto.getAccessionNbr();
    }
}
