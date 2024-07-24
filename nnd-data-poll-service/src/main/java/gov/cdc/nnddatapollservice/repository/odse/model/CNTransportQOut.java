package gov.cdc.nnddatapollservice.repository.odse.model;


import gov.cdc.nnddatapollservice.service.model.dto.CNTransportQOutDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;


@Entity
@Table(name = "CN_transportq_out")
@Data
public class CNTransportQOut {

    @Id
    @Column(name = "cn_transportq_out_uid", nullable = false)
    private Long cnTransportqOutUid;

    @Column(name = "add_reason_cd")
    private String addReasonCd;

    @Column(name = "add_time")
    private Timestamp addTime;

    @Column(name = "add_user_id")
    private Long addUserId;

    @Column(name = "last_chg_reason_cd")
    private String lastChgReasonCd;

    @Column(name = "last_chg_time")
    private Timestamp lastChgTime;

    @Column(name = "last_chg_user_id")
    private Long lastChgUserId;

    @Lob
    @Column(name = "message_payload")
    private byte[] messagePayload;

    @Column(name = "notification_uid", nullable = false)
    private Long notificationUid;

    @Column(name = "notification_local_id")
    private String notificationLocalId;

    @Column(name = "public_health_case_local_id")
    private String publicHealthCaseLocalId;

    @Column(name = "report_status_cd")
    private String reportStatusCd;

    @Column(name = "record_status_cd")
    private String recordStatusCd;

    @Column(name = "record_status_time")
    private Timestamp recordStatusTime;

    @Column(name = "version_ctrl_nbr")
    private Integer versionCtrlNbr;

//    @ManyToOne
//    @JoinColumn(name = "notification_uid", insertable = false, updatable = false)
//    private Notification notification;

    public CNTransportQOut() {

    }

    public CNTransportQOut(CNTransportQOutDto dto) {
        this.cnTransportqOutUid = dto.getCnTransportqOutUid();
        this.addReasonCd = dto.getAddReasonCd();
        this.addTime = dto.getAddTime();
        this.addUserId = dto.getAddUserId();
        this.lastChgReasonCd = dto.getLastChgReasonCd();
        this.lastChgTime = dto.getLastChgTime();
        this.lastChgUserId = dto.getLastChgUserId();
        this.messagePayload = dto.getMessagePayload();
        this.notificationUid = dto.getNotificationUid();
        this.notificationLocalId = dto.getNotificationLocalId();
        this.publicHealthCaseLocalId = dto.getPublicHealthCaseLocalId();
        this.reportStatusCd = dto.getReportStatusCd();
        this.recordStatusCd = dto.getRecordStatusCd();
        this.recordStatusTime = dto.getRecordStatusTime();
        this.versionCtrlNbr = dto.getVersionCtrlNbr();
    }
}
