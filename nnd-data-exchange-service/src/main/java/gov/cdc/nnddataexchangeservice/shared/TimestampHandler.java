package gov.cdc.nnddataexchangeservice.shared;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimestampHandler {
    private TimestampHandler() {
    }
    public static Timestamp getCurrentTimeStamp() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        return new Timestamp(currentDate.getTime());
    }

    public static String convertTimestampFromString(String timestamp) {
        if (timestamp.length() > 19) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n");
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, inputFormatter);
            LocalDateTime truncatedDateTime = dateTime.truncatedTo(ChronoUnit.MILLIS);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            return truncatedDateTime.format(outputFormatter);
        }
        else {
            return timestamp;
        }

    }
}
