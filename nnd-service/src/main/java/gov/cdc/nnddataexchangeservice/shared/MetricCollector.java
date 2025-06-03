package gov.cdc.nnddataexchangeservice.shared;

import gov.cdc.nnddataexchangeservice.shared.model.ExecutionResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class MetricCollector {
    private MetricCollector() {

    }
    public static <V> ExecutionResult<V> measureExecutionTime(Callable<V> function) throws Exception {
        LocalDateTime start = LocalDateTime.now();

        // Execute the passed function and capture the result
        V result = function.call();

        LocalDateTime  end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);

        // Convert the duration to a human-readable format
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        long milliseconds = duration.toMillis() % 1000;

        // Convert the duration to a timestamp
        String executionTime = "Execution time: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds, " + milliseconds + " milliseconds";

        return new ExecutionResult<>(result, executionTime);
    }
}
