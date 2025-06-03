package gov.cdc.nnddataexchangeservice.security.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int statusCode;
    private String message;
    private String details;
}
