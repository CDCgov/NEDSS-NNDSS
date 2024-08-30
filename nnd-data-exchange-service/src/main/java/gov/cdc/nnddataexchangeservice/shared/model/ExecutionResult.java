package gov.cdc.nnddataexchangeservice.shared.model;

import java.sql.Timestamp;

public class ExecutionResult<V> {
    private final V result;
    private final String executionTime;

    public ExecutionResult(V result, String executionTime) {
        this.result = result;
        this.executionTime = executionTime;
    }

    public V getResult() {
        return result;
    }

    public String getExecutionTime() {
        return executionTime;
    }
}