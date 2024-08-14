package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class NotificationEventId implements Serializable {
    private static final long serialVersionUID = 8107073767474249230L;
    @jakarta.validation.constraints.NotNull
    @Column(name = "PATIENT_KEY", nullable = false)
    private Long patientKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "NOTIFICATION_SENT_DT_KEY", nullable = false)
    private Long notificationSentDtKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "NOTIFICATION_SUBMIT_DT_KEY", nullable = false)
    private Long notificationSubmitDtKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "INVESTIGATION_KEY", nullable = false)
    private Long investigationKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "CONDITION_KEY", nullable = false)
    private Long conditionKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "NOTIFICATION_KEY", nullable = false)
    private Long notificationKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        NotificationEventId entity = (NotificationEventId) o;
        return Objects.equals(this.notificationKey, entity.notificationKey) &&
                Objects.equals(this.investigationKey, entity.investigationKey) &&
                Objects.equals(this.conditionKey, entity.conditionKey) &&
                Objects.equals(this.patientKey, entity.patientKey) &&
                Objects.equals(this.notificationSubmitDtKey, entity.notificationSubmitDtKey) &&
                Objects.equals(this.notificationSentDtKey, entity.notificationSentDtKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationKey, investigationKey, conditionKey, patientKey, notificationSubmitDtKey, notificationSentDtKey);
    }

}