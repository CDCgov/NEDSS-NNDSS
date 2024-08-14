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
@Table(name = "F_INTERVIEW_CASE")
public class FInterviewCase {
    @jakarta.validation.constraints.NotNull
    @Column(name = "D_INTERVIEW_KEY", nullable = false)
    private Double dInterviewKey;

    @Column(name = "PATIENT_KEY", precision = 18)
    private BigDecimal patientKey;

    @Column(name = "IX_INTERVIEWER_KEY", precision = 18)
    private BigDecimal ixInterviewerKey;

    @Column(name = "INVESTIGATION_KEY", precision = 18)
    private BigDecimal investigationKey;

    @Column(name = "INTERPRETER_KEY")
    private Double interpreterKey;

    @Column(name = "NURSE_KEY")
    private Double nurseKey;

    @Column(name = "PHYSICIAN_KEY")
    private Double physicianKey;

    @Column(name = "PROXY_KEY")
    private Double proxyKey;

    @Column(name = "IX_INTERVIEWEE_KEY")
    private Double ixIntervieweeKey;

    @Column(name = "INTERVENTION_SITE_KEY", precision = 20)
    private BigDecimal interventionSiteKey;

}