package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "NOTIFICATION_EVENT")
public class NotificationEvent {
    @EmbeddedId
    private NotificationEventId id;

    @MapsId("notificationSentDtKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOTIFICATION_SENT_DT_KEY", nullable = false)
    private RdbDate notificationSentDtKey;

    @MapsId("notificationSubmitDtKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOTIFICATION_SUBMIT_DT_KEY", nullable = false)
    private RdbDate notificationSubmitDtKey;

    @MapsId("conditionKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONDITION_KEY", nullable = false)
    private Condition conditionKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "NOTIFICATION_UPD_DT_KEY", nullable = false)
    private Long notificationUpdDtKey;

    @Column(name = "\"COUNT\"", precision = 18)
    private BigDecimal count;

}