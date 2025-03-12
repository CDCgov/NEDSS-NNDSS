package gov.cdc.nnddatapollservice.exception;

public class DataPollException extends Exception{
    public DataPollException(String message) {
        super(message);
    }

    public DataPollException(String message, Throwable cause) {
        super(message, cause);
    }
}
