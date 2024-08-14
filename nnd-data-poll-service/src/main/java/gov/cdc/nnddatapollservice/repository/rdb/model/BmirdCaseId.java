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
public class BmirdCaseId implements Serializable {
    private static final long serialVersionUID = 5207866463299233281L;
    @jakarta.validation.constraints.NotNull
    @Column(name = "INVESTIGATOR_KEY", nullable = false)
    private Long investigatorKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "PHYSICIAN_KEY", nullable = false)
    private Long physicianKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "PATIENT_KEY", nullable = false)
    private Long patientKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "REPORTER_KEY", nullable = false)
    private Long reporterKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "NURSING_HOME_KEY", nullable = false)
    private Long nursingHomeKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "DAYCARE_FACILITY_KEY", nullable = false)
    private Long daycareFacilityKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "INV_ASSIGNED_DT_KEY", nullable = false)
    private Long invAssignedDtKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "BMIRD_MULTI_VAL_GRP_KEY", nullable = false)
    private Long bmirdMultiValGrpKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "ANTIMICROBIAL_GRP_KEY", nullable = false)
    private Long antimicrobialGrpKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "INVESTIGATION_KEY", nullable = false)
    private Long investigationKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "ADT_HSPTL_KEY", nullable = false)
    private Long adtHsptlKey;

    @jakarta.validation.constraints.NotNull
    @Column(name = "RPT_SRC_ORG_KEY", nullable = false)
    private Long rptSrcOrgKey;

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
        BmirdCaseId entity = (BmirdCaseId) o;
        return Objects.equals(this.investigatorKey, entity.investigatorKey) &&
                Objects.equals(this.investigationKey, entity.investigationKey) &&
                Objects.equals(this.physicianKey, entity.physicianKey) &&
                Objects.equals(this.nursingHomeKey, entity.nursingHomeKey) &&
                Objects.equals(this.invAssignedDtKey, entity.invAssignedDtKey) &&
                Objects.equals(this.antimicrobialGrpKey, entity.antimicrobialGrpKey) &&
                Objects.equals(this.conditionKey, entity.conditionKey) &&
                Objects.equals(this.ldfGroupKey, entity.ldfGroupKey) &&
                Objects.equals(this.patientKey, entity.patientKey) &&
                Objects.equals(this.daycareFacilityKey, entity.daycareFacilityKey) &&
                Objects.equals(this.bmirdMultiValGrpKey, entity.bmirdMultiValGrpKey) &&
                Objects.equals(this.adtHsptlKey, entity.adtHsptlKey) &&
                Objects.equals(this.rptSrcOrgKey, entity.rptSrcOrgKey) &&
                Objects.equals(this.reporterKey, entity.reporterKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(investigatorKey, investigationKey, physicianKey, nursingHomeKey, invAssignedDtKey, antimicrobialGrpKey, conditionKey, ldfGroupKey, patientKey, daycareFacilityKey, bmirdMultiValGrpKey, adtHsptlKey, rptSrcOrgKey, reporterKey);
    }

}