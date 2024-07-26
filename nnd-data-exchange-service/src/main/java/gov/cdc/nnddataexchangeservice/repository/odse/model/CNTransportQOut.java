package gov.cdc.nnddataexchangeservice.repository.odse.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Entity
@Table(name = "CN_transportq_out")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

//    @Lob
    @Column(name = "message_payload")
//    private byte[] messagePayload;
    private String messagePayload;

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
}
