package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "D_INTERVIEW")
public class DInterview {
    @Column(name = "D_INTERVIEW_KEY")
    private Double dInterviewKey;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "IX_STATUS_CD", length = 4000)
    private String ixStatusCd;

    @Column(name = "IX_DATE")
    private Instant ixDate;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "IX_INTERVIEWEE_ROLE_CD", length = 4000)
    private String ixIntervieweeRoleCd;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "IX_TYPE_CD", length = 4000)
    private String ixTypeCd;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "IX_LOCATION_CD", length = 4000)
    private String ixLocationCd;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "LOCAL_ID", length = 4000)
    private String localId;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "RECORD_STATUS_CD", length = 4000)
    private String recordStatusCd;

    @Column(name = "RECORD_STATUS_TIME")
    private Instant recordStatusTime;

    @Column(name = "ADD_TIME")
    private Instant addTime;

    @Column(name = "ADD_USER_ID", precision = 21)
    private BigDecimal addUserId;

    @Column(name = "LAST_CHG_TIME")
    private Instant lastChgTime;

    @Column(name = "LAST_CHG_USER_ID", precision = 21)
    private BigDecimal lastChgUserId;

    @Column(name = "VERSION_CTRL_NBR", precision = 11)
    private BigDecimal versionCtrlNbr;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "IX_STATUS", length = 4000)
    private String ixStatus;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "IX_INTERVIEWEE_ROLE", length = 4000)
    private String ixIntervieweeRole;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "IX_TYPE", length = 4000)
    private String ixType;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "IX_LOCATION", length = 4000)
    private String ixLocation;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "IX_CONTACTS_NAMED_IND", length = 4000)
    private String ixContactsNamedInd;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "IX_900_SITE_TYPE", length = 4000)
    private String ix900SiteType;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "IX_INTERVENTION", length = 4000)
    private String ixIntervention;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "IX_900_SITE_ID", length = 2000)
    private String ix900SiteId;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "IX_900_SITE_ZIP", length = 2000)
    private String ix900SiteZip;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "CLN_CARE_STATUS_IXS", length = 4000)
    private String clnCareStatusIxs;

}