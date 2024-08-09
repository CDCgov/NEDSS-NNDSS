package gov.cdc.nndmessageprocessor.repository.model;


import gov.cdc.nndmessageprocessor.service.model.dto.NETSSTransportQOutDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "NETSS_TransportQ_out")
@Data
public class NETSSTransportQOut {

    @Id
    @Column(name = "NETSS_TransportQ_out_uid", nullable = false)
    private Long netssTransportQOutUid;

    @Column(name = "record_type_cd", nullable = false)
    private String recordTypeCd;

    @Column(name = "mmwr_year", nullable = false)
    private Integer mmwrYear;

    @Column(name = "mmwr_week", nullable = false)
    private Integer mmwrWeek;

    @Column(name = "netss_case_id", nullable = false)
    private String netssCaseId;

    @Column(name = "phc_local_id", nullable = false)
    private String phcLocalId;

    @Column(name = "notification_local_id", nullable = false)
    private String notificationLocalId;

    @Column(name = "add_time")
    private Timestamp addTime;

    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "record_status_cd")
    private String recordStatusCd;

    public NETSSTransportQOut(NETSSTransportQOutDto dto) {
        this.netssTransportQOutUid = dto.getNetssTransportQOutUid();
        this.recordTypeCd = dto.getRecordTypeCd();
        this.mmwrYear = dto.getMmwrYear();
        this.mmwrWeek = dto.getMmwrWeek();
        this.netssCaseId = dto.getNetssCaseId();
        this.phcLocalId = dto.getPhcLocalId();
        this.notificationLocalId = dto.getNotificationLocalId();
        this.addTime = dto.getAddTime();
        this.payload = dto.getPayload();
        this.recordStatusCd = dto.getRecordStatusCd();
    }

    public NETSSTransportQOut() {
        // Default constructor
    }

    // For Unit Test
    public NETSSTransportQOut(String payload    ) {
        this.payload = payload;
    }
}