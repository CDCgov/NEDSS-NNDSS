package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "RDB_DATE")
public class RdbDate {
    @Id
    @Column(name = "DATE_KEY", nullable = false)
    private Long id;

    @Column(name = "DATE_MM_DD_YYYY")
    private Instant dateMmDdYyyy;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "DAY_OF_WEEK", length = 10)
    private String dayOfWeek;

    @Column(name = "DAY_NBR_IN_CLNDR_MON", precision = 4)
    private BigDecimal dayNbrInClndrMon;

    @Column(name = "DAY_NBR_IN_CLNDR_YR", precision = 4)
    private BigDecimal dayNbrInClndrYr;

    @Column(name = "WK_NBR_IN_CLNDR_MON", precision = 4)
    private BigDecimal wkNbrInClndrMon;

    @Column(name = "WK_NBR_IN_CLNDR_YR", precision = 4)
    private BigDecimal wkNbrInClndrYr;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "CLNDR_MON_NAME", length = 20)
    private String clndrMonName;

    @Column(name = "CLNDR_MON_IN_YR", precision = 4)
    private BigDecimal clndrMonInYr;

    @Column(name = "CLNDR_QRTR", precision = 4)
    private BigDecimal clndrQrtr;

    @Column(name = "CLNDR_YR", precision = 18)
    private BigDecimal clndrYr;

}