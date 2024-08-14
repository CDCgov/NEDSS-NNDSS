package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
public class Treatment {
    @Id
    @Column(name = "treatment_uid", nullable = false)
    private Long id;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "activity_duration_amt", length = 20)
    private String activityDurationAmt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "activity_duration_unit_cd", length = 20)
    private String activityDurationUnitCd;

    @Column(name = "activity_from_time")
    private Instant activityFromTime;

    @Column(name = "activity_to_time")
    private Instant activityToTime;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "add_reason_cd", length = 20)
    private String addReasonCd;

    @Column(name = "add_time")
    private Instant addTime;

    @Column(name = "add_user_id")
    private Long addUserId;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "cd", length = 50)
    private String cd;

    @jakarta.validation.constraints.Size(max = 150)
    @Column(name = "cd_desc_txt", length = 150)
    private String cdDescTxt;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "cd_system_cd", length = 20)
    private String cdSystemCd;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "cd_system_desc_txt", length = 100)
    private String cdSystemDescTxt;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "cd_version", length = 10)
    private String cdVersion;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "class_cd", length = 10)
    private String classCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "jurisdiction_cd", length = 20)
    private String jurisdictionCd;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "last_chg_reason_cd", length = 20)
    private String lastChgReasonCd;

    @Column(name = "last_chg_time")
    private Instant lastChgTime;

    @Column(name = "last_chg_user_id")
    private Long lastChgUserId;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "local_id", length = 50)
    private String localId;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "prog_area_cd", length = 20)
    private String progAreaCd;

    @Column(name = "program_jurisdiction_oid")
    private Long programJurisdictionOid;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "record_status_cd", length = 20)
    private String recordStatusCd;

    @Column(name = "record_status_time")
    private Instant recordStatusTime;

    @Column(name = "shared_ind")
    private Character sharedInd;

    @Column(name = "status_cd")
    private Character statusCd;

    @Column(name = "status_time")
    private Instant statusTime;

    @jakarta.validation.constraints.Size(max = 1000)
    @Column(name = "txt", length = 1000)
    private String txt;

    @Column(name = "version_ctrl_nbr")
    private Short versionCtrlNbr;

}