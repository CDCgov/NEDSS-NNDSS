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
public class TreatmentEventId implements Serializable {
    private static final long serialVersionUID = -3251202553775088647L;
    @jakarta.validation.constraints.NotNull
    @Column(name = "TREATMENT_DT_KEY", nullable = false)
    private Long treatmentDtKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "TREATMENT_PROVIDING_ORG_KEY", nullable = false)
    private Long treatmentProvidingOrgKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "PATIENT_KEY", nullable = false)
    private Long patientKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "TREATMENT_KEY", nullable = false)
    private Long treatmentKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "MORB_RPT_KEY", nullable = false)
    private Long morbRptKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "TREATMENT_PHYSICIAN_KEY", nullable = false)
    private Long treatmentPhysicianKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "INVESTIGATION_KEY", nullable = false)
    private Long investigationKey;

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
        TreatmentEventId entity = (TreatmentEventId) o;
        return Objects.equals(this.treatmentPhysicianKey, entity.treatmentPhysicianKey) &&
                Objects.equals(this.investigationKey, entity.investigationKey) &&
                Objects.equals(this.conditionKey, entity.conditionKey) &&
                Objects.equals(this.ldfGroupKey, entity.ldfGroupKey) &&
                Objects.equals(this.treatmentProvidingOrgKey, entity.treatmentProvidingOrgKey) &&
                Objects.equals(this.patientKey, entity.patientKey) &&
                Objects.equals(this.treatmentDtKey, entity.treatmentDtKey) &&
                Objects.equals(this.treatmentKey, entity.treatmentKey) &&
                Objects.equals(this.morbRptKey, entity.morbRptKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(treatmentPhysicianKey, investigationKey, conditionKey, ldfGroupKey, treatmentProvidingOrgKey, patientKey, treatmentDtKey, treatmentKey, morbRptKey);
    }

}