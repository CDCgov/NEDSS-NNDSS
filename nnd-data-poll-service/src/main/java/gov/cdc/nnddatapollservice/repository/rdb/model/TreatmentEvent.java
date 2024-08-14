package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "TREATMENT_EVENT")
public class TreatmentEvent {
    @EmbeddedId
    private TreatmentEventId id;

    @Column(name = "TREATMENT_COUNT", precision = 18)
    private BigDecimal treatmentCount;

    @jakarta.validation.constraints.Size(max = 8)
    @jakarta.validation.constraints.NotNull
    @Column(name = "RECORD_STATUS_CD", nullable = false, length = 8)
    private String recordStatusCd;

}