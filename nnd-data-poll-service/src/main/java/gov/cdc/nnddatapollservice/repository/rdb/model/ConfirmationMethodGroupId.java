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
public class ConfirmationMethodGroupId implements Serializable {
    private static final long serialVersionUID = 6253209779511090823L;
    @jakarta.validation.constraints.NotNull
    @Column(name = "INVESTIGATION_KEY", nullable = false)
    private Long investigationKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "CONFIRMATION_METHOD_KEY", nullable = false)
    private Long confirmationMethodKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ConfirmationMethodGroupId entity = (ConfirmationMethodGroupId) o;
        return Objects.equals(this.investigationKey, entity.investigationKey) &&
                Objects.equals(this.confirmationMethodKey, entity.confirmationMethodKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(investigationKey, confirmationMethodKey);
    }

}