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

    public String getEvent() {
        String thisPayload = getPayload();
        String conditionStr = "";
        if (thisPayload.startsWith("M")) {
            conditionStr =  payload.substring(17, 22);
            try {
                //make sure condition is 5 digits
                Integer conditionInt = Integer.valueOf(conditionStr);
                //per CHristi 10/26/15 allow any condition in table to go
                //if (conditionInt == 10273 ||
                //		conditionInt == 10274 ||
                //		conditionInt == 10280 ||
                //		conditionInt == 10311 ||
                //		conditionInt == 10312 ||
                //		conditionInt == 10313 ||
                //		conditionInt == 10314 ||
                //		conditionInt == 10319)
                return conditionStr;

            } catch (Exception donothing) {
                // System.out.println("Condition not numeric is: " + conditionStr);
            }
        }
        return null;
    }
}
