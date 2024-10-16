package gov.cdc.nnddatapollservice.repository.srte.model;

import gov.cdc.nnddatapollservice.srte.dto.CodeToConditionDto;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

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
    private String statusCd;

    @Column(name = "status_time")
    private Timestamp statusTime;

    @Column(name = "nbs_uid")
    private Integer nbsUid;

    @Column(name = "effective_from_time")
    private Timestamp effectiveFromTime;

    @Column(name = "effective_to_time")
    private Timestamp effectiveToTime;

    public CodeToCondition() {

    }


    public CodeToCondition(CodeToConditionDto dto) {
        this.id = new CodeToConditionId(dto.getCode(), dto.getConditionCd());
        this.codeSystemCd = dto.getCodeSystemCd();
        this.codeSystemDescTxt = dto.getCodeSystemDescTxt();
        this.codeDescTxt = dto.getCodeDescTxt();
        this.codeSystemVersionId = dto.getCodeSystemVersionId();
        this.diseaseNm = dto.getDiseaseNm();
        this.statusCd = dto.getStatusCd();
        this.statusTime = dto.getStatusTime() != null ? Timestamp.valueOf(dto.getStatusTime()) : null;
        this.nbsUid = dto.getNbsUid();
        this.effectiveFromTime = dto.getEffectiveFromTime() != null ? Timestamp.valueOf(dto.getEffectiveFromTime()) : null;
        this.effectiveToTime = dto.getEffectiveToTime() != null ? Timestamp.valueOf(dto.getEffectiveToTime()) : null;
    }

}

