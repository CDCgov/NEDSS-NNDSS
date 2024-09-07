package gov.cdc.nnddatapollservice.rdb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmationMethod {
    private static final long serialVersionUID = 1L;
    private Long confirmationMethodKey;
    private String confirmationMethodCd;
    private String confirmationMethodDesc;
}