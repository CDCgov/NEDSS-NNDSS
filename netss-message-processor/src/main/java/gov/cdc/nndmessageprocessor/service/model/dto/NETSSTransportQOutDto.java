package gov.cdc.nndmessageprocessor.service.model.dto;


import gov.cdc.nndmessageprocessor.repository.model.NETSSTransportQOut;
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

    private boolean 	itNew;
    private boolean 	itDelete;
    private boolean 	itDirty;

    // Constructor to convert domain model to DTO

    // For Unit Testing
    public NETSSTransportQOutDto( String payload) {
        this.payload = payload;
    }
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

    public NETSSTransportQOutDto() {

    }

    public String getEvent() {
        String thisPayload = getPayload();
        String conditionStr = "";
        if (thisPayload.startsWith("M")) {
            conditionStr =  payload.substring(17, 22);
            return conditionStr;

        }
        return null;
    }
}
