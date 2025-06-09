package gov.cdc.nnddataexchangeservice.exception;

import org.springframework.security.core.AuthenticationException;

public class DataExchangeSecurityException extends AuthenticationException {
    public DataExchangeSecurityException(String message) {
        super(message);
    }
}