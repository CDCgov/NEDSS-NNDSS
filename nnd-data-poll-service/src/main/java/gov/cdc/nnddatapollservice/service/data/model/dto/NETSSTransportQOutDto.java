package gov.cdc.nnddatapollservice.service.data.model.dto;

import gov.cdc.nnddatapollservice.repository.msg.model.NETSSTransportQOut;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class NETSSTransportQOutDto {

    private Long netssTransportQOutUid;
    private String recordTypeCd;
    private Integer mmwrYear;
    private Integer mmwrWeek;
    private String netssCaseId;
    private String phcLocalId;
    private String notificationLocalId;
    private Timestamp addTime;
    private String payload;
    private String recordStatusCd;

    public NETSSTransportQOutDto(){

    }
    // Constructor to convert domain model to DTO
    public NETSSTransportQOutDto(NETSSTransportQOut netssTransportQOut) {
        this.netssTransportQOutUid = netssTransportQOut.getNetssTransportQOutUid();
        this.recordTypeCd = netssTransportQOut.getRecordTypeCd();
        this.mmwrYear = netssTransportQOut.getMmwrYear();
        this.mmwrWeek = netssTransportQOut.getMmwrWeek();
        this.netssCaseId = netssTransportQOut.getNetssCaseId();
        this.phcLocalId = netssTransportQOut.getPhcLocalId();
        this.notificationLocalId = netssTransportQOut.getNotificationLocalId();
        this.addTime = netssTransportQOut.getAddTime();
        this.payload = netssTransportQOut.getPayload();
        this.recordStatusCd = netssTransportQOut.getRecordStatusCd();
    }
}
