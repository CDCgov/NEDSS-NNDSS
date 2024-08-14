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
@Table(name = "VAR_PAM_LDF")
public class VarPamLdf {
    @Column(name = "INVESTIGATION_KEY", precision = 20)
    private BigDecimal investigationKey;

    @Column(name = "VAR_PAM_UID", precision = 20)
    private BigDecimal varPamUid;

    @Column(name = "add_time")
    private Instant addTime;

}