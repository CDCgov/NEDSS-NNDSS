package gov.cdc.nnddatapollservice.repository.srte.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
public class CodeToConditionId implements Serializable {

    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "condition_cd", length = 20)
    private String conditionCd;
}
