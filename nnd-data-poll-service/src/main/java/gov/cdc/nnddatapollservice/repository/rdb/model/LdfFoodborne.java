package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "LDF_FOODBORNE")
public class LdfFoodborne {
    @Column(name = "INVESTIGATION_KEY", precision = 20)
    private BigDecimal investigationKey;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "INVESTIGATION_LOCAL_ID", length = 50)
    private String investigationLocalId;

    @Column(name = "PROGRAM_JURISDICTION_OID", precision = 20)
    private BigDecimal programJurisdictionOid;

    @Column(name = "PATIENT_KEY", precision = 20)
    private BigDecimal patientKey;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_LOCAL_ID", length = 50)
    private String patientLocalId;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "DISEASE_NAME", length = 50)
    private String diseaseName;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "DISEASE_CD", length = 10)
    private String diseaseCd;

}