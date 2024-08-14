package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "D_INV_HIV")
public class DInvHiv {
    @Column(name = "D_INV_HIV_KEY")
    private Double dInvHivKey;

    @Column(name = "nbs_case_answer_uid", precision = 21)
    private BigDecimal nbsCaseAnswerUid;

    @Column(name = "HIV_900_TEST_REFERRAL_DT")
    private LocalDate hiv900TestReferralDt;

    @Column(name = "HIV_LAST_900_TEST_DT")
    private LocalDate hivLast900TestDt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_900_RESULT", length = 1999)
    private String hiv900Result;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_900_TEST_IND", length = 1999)
    private String hiv900TestInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_AV_THERAPY_EVER_IND", length = 1999)
    private String hivAvTherapyEverInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_AV_THERAPY_LAST_12MO_IND", length = 1999)
    private String hivAvTherapyLast12moInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_CA_900_REASON_NOT_LOC", length = 1999)
    private String hivCa900ReasonNotLoc;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_EHARS_TRNSMSN_CTGRY", length = 1999)
    private String hivEharsTrnsmsnCtgry;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_ENROLL_PRTNR_SRVCS_IND", length = 1999)
    private String hivEnrollPrtnrSrvcsInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_HIV_STAT_INV_IN_EHARS", length = 1999)
    private String hivHivStatInvInEhars;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_HIV_STATUS_CD_MTH", length = 1999)
    private String hivHivStatusCdMth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_KEEP_900_CARE_APPT_IND", length = 1999)
    private String hivKeep900CareApptInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_POST_TEST_900_COUNSELING", length = 1999)
    private String hivPostTest900Counseling;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_PREVIOUS_900_TEST_IND", length = 1999)
    private String hivPrevious900TestInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_REFER_FOR_900_CARE_IND", length = 1999)
    private String hivReferFor900CareInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_REFER_FOR_900_TEST", length = 1999)
    private String hivReferFor900Test;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_RST_PROVIDED_900_RSLT_IND", length = 1999)
    private String hivRstProvided900RsltInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "HIV_SELF_REPORTED_RSLT_900", length = 1999)
    private String hivSelfReportedRslt900;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "HIV_CA_900_OTH_RSN_NOT_LO", length = 2000)
    private String hivCa900OthRsnNotLo;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "HIV_STATE_CASE_ID", length = 2000)
    private String hivStateCaseId;

}