package gov.cdc.nnddatapollservice.repository.srte.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Code_to_condition", schema = "dbo")
@Data
public class CodeToCondition {

    @EmbeddedId
    private CodeToConditionId id;

    @Column(name = "code_system_cd", length = 256)
    private String codeSystemCd;

    @Column(name = "code_system_desc_txt", length = 100)
    private String codeSystemDescTxt;


    @Column(name = "code_desc_txt", length = 256)
    private String codeDescTxt;

    @Column(name = "code_system_version_id", length = 256)
    private String codeSystemVersionId;

    @Column(name = "disease_nm", length = 200)
    private String diseaseNm;

    @Column(name = "status_cd", length = 1)
    private Character statusCd;

    @Column(name = "status_time")
    private LocalDateTime statusTime;

    @Column(name = "nbs_uid")
    private Integer nbsUid;

    @Column(name = "effective_from_time")
    private LocalDateTime effectiveFromTime;

    @Column(name = "effective_to_time")
    private LocalDateTime effectiveToTime;
}

