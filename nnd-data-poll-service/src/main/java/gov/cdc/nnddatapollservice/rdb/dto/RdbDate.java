package gov.cdc.nnddatapollservice.rdb.dto;

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

    public Long getDateKey() {
        return dateKey;
    }

    public String getDateMmDdYyyy() {
        return dateMmDdYyyy;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public BigDecimal getDayNbrInClndrMon() {
        return dayNbrInClndrMon;
    }

    public BigDecimal getDayNbrInClndrYr() {
        return dayNbrInClndrYr;
    }

    public BigDecimal getWkNbrInClndrMon() {
        return wkNbrInClndrMon;
    }

    public BigDecimal getWkNbrInClndrYr() {
        return wkNbrInClndrYr;
    }

    public String getClndrMonName() {
        return clndrMonName;
    }

    public BigDecimal getClndrMonInYr() {
        return clndrMonInYr;
    }

    public BigDecimal getClndrQrtr() {
        return clndrQrtr;
    }

    public BigDecimal getClndrYr() {
        return clndrYr;
    }
}