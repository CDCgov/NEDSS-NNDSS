package gov.cdc.nnddataexchangeservice.repository.msg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "NETSS_TransportQ_out")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

}
