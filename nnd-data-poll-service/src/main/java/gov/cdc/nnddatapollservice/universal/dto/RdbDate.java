package gov.cdc.nnddatapollservice.universal.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RdbDate {
    private Long dateKey;
    private String dateMmDdYyyy;//Timestamp
    private String dayOfWeek;
    private BigDecimal dayNbrInClndrMon;
    private BigDecimal dayNbrInClndrYr;
    private BigDecimal wkNbrInClndrMon;
    private BigDecimal wkNbrInClndrYr;
    private String clndrMonName;
    private BigDecimal clndrMonInYr;
    private BigDecimal clndrQrtr;
    private BigDecimal clndrYr;
}