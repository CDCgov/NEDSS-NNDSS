package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "SUMMARY_REPORT_CASE")
public class SummaryReportCase {
    @EmbeddedId
    private SummaryReportCaseId id;

    @MapsId("notificationSendDtKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOTIFICATION_SEND_DT_KEY", nullable = false)
    private RdbDate notificationSendDtKey;

    @MapsId("conditionKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONDITION_KEY", nullable = false)
    private Condition conditionKey;

    @Column(name = "SUM_RPT_CASE_COUNT", precision = 18)
    private BigDecimal sumRptCaseCount;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "SUM_RPT_CASE_COMMENTS", length = 2000)
    private String sumRptCaseComments;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "SUM_RPT_CASE_STATUS", length = 20)
    private String sumRptCaseStatus;

    @jakarta.validation.constraints.Size(max = 50)
    @jakarta.validation.constraints.NotNull
    @Column(name = "COUNTY_CD", nullable = false, length = 50)
    private String countyCd;

    @jakarta.validation.constraints.Size(max = 300)
    @jakarta.validation.constraints.NotNull
    @Column(name = "COUNTY_NAME", nullable = false, length = 300)
    private String countyName;

    @jakarta.validation.constraints.Size(max = 50)
    @jakarta.validation.constraints.NotNull
    @Column(name = "STATE_CD", nullable = false, length = 50)
    private String stateCd;

    @Column(name = "LAST_UPDATE_DT_KEY")
    private Long lastUpdateDtKey;

}