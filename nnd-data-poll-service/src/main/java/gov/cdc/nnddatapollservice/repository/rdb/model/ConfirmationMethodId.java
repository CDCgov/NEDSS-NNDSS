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
public class ConfirmationMethodId implements Serializable {
    private static final long serialVersionUID = 9063660432197913152L;
    @jakarta.validation.constraints.NotNull
    @Column(name = "public_health_case_uid", nullable = false)
    private Long publicHealthCaseUid;

    @jakarta.validation.constraints.Size(max = 20)
    @jakarta.validation.constraints.NotNull
    @Column(name = "confirmation_method_cd", nullable = false, length = 20)
    private String confirmationMethodCd;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ConfirmationMethodId entity = (ConfirmationMethodId) o;
        return Objects.equals(this.confirmationMethodCd, entity.confirmationMethodCd) &&
                Objects.equals(this.publicHealthCaseUid, entity.publicHealthCaseUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmationMethodCd, publicHealthCaseUid);
    }

}