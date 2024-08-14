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
public class SummaryReportCaseId implements Serializable {
    private static final long serialVersionUID = -6862851906368199677L;
    @jakarta.validation.constraints.NotNull
    @Column(name = "INVESTIGATION_KEY", nullable = false)
    private Long investigationKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "SUMMARY_CASE_SRC_KEY", nullable = false)
    private Long summaryCaseSrcKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "NOTIFICATION_SEND_DT_KEY", nullable = false)
    private Long notificationSendDtKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "CONDITION_KEY", nullable = false)
    private Long conditionKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "LDF_GROUP_KEY", nullable = false)
    private Long ldfGroupKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SummaryReportCaseId entity = (SummaryReportCaseId) o;
        return Objects.equals(this.investigationKey, entity.investigationKey) &&
                Objects.equals(this.conditionKey, entity.conditionKey) &&
                Objects.equals(this.summaryCaseSrcKey, entity.summaryCaseSrcKey) &&
                Objects.equals(this.ldfGroupKey, entity.ldfGroupKey) &&
                Objects.equals(this.notificationSendDtKey, entity.notificationSendDtKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(investigationKey, conditionKey, summaryCaseSrcKey, ldfGroupKey, notificationSendDtKey);
    }

}