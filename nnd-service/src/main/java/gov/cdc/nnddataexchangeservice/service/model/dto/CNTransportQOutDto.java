package gov.cdc.nnddataexchangeservice.service.model.dto;

import gov.cdc.nnddataexchangeservice.repository.odse.model.CNTransportQOut;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CNTransportQOutDto {

    private Long cnTransportqOutUid;
    private String addReasonCd;
    private Timestamp addTime;
    private Long addUserId;
    private String lastChgReasonCd;
    private Timestamp lastChgTime;
    private Long lastChgUserId;
//    private byte[] messagePayload;
    private String messagePayload;
    private Long notificationUid;
    private String notificationLocalId;
    private String publicHealthCaseLocalId;
    private String reportStatusCd;
    private String recordStatusCd;
    private Timestamp recordStatusTime;
    private Integer versionCtrlNbr;

    public CNTransportQOutDto() {

    }
    // Constructor to convert domain model to DTO
    public CNTransportQOutDto(CNTransportQOut cnTransportQOut) {
        this.cnTransportqOutUid = cnTransportQOut.getCnTransportqOutUid();
        this.addReasonCd = cnTransportQOut.getAddReasonCd();
        this.addTime = cnTransportQOut.getAddTime();
        this.addUserId = cnTransportQOut.getAddUserId();
        this.lastChgReasonCd = cnTransportQOut.getLastChgReasonCd();
        this.lastChgTime = cnTransportQOut.getLastChgTime();
        this.lastChgUserId = cnTransportQOut.getLastChgUserId();
        this.messagePayload = cnTransportQOut.getMessagePayload();
        this.notificationUid = cnTransportQOut.getNotificationUid();
        this.notificationLocalId = cnTransportQOut.getNotificationLocalId();
        this.publicHealthCaseLocalId = cnTransportQOut.getPublicHealthCaseLocalId();
        this.reportStatusCd = cnTransportQOut.getReportStatusCd();
        this.recordStatusCd = cnTransportQOut.getRecordStatusCd();
        this.recordStatusTime = cnTransportQOut.getRecordStatusTime();
        this.versionCtrlNbr = cnTransportQOut.getVersionCtrlNbr();
    }
}
