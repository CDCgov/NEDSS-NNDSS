package gov.cdc.nnddataexchangeservice.shared;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimestampHandler {
    private TimestampHandler() {
    }
    public static Timestamp getCurrentTimeStamp(String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone);
        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        ZonedDateTime gmt = zdt.withZoneSameInstant(zoneId);
        return Timestamp.valueOf(gmt.toLocalDateTime());
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
