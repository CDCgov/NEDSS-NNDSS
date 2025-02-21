package gov.cdc.nnddatapollservice.repository.nbs_odse.model;

import gov.cdc.nnddatapollservice.universal.dto.EDXActivityDetailLogDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EDX_activity_detail_log")
public class EDXActivityDetailLog {

    @Id
    @Column(name = "edx_activity_detail_log_uid", nullable = false)
    private Long edxActivityDetailLogUid;

    @Column(name = "edx_activity_log_uid", nullable = false)
    private Long edxActivityLogUid;

    @Column(name = "record_id", length = 256)
    private String recordId;

    @Column(name = "record_type", length = 50)
    private String recordType;

    @Column(name = "record_nm", length = 250)
    private String recordName;

    @Column(name = "log_type", length = 50)
    private String logType;

    @Column(name = "log_comment", length = 2000)
    private String logComment;

    public EDXActivityDetailLog(EDXActivityDetailLogDto dto) {
        this.edxActivityDetailLogUid = dto.getEdxActivityDetailLogUid();
        this.edxActivityLogUid = dto.getEdxActivityLogUid();
        this.recordId = dto.getRecordId();
        this.recordType = dto.getRecordType();
        this.recordName = dto.getRecordName();
        this.logType = dto.getLogType();
        this.logComment = dto.getLogComment();
    }
}
