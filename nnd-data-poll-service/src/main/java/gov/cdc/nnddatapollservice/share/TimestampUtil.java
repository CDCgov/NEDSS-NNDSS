package gov.cdc.nnddatapollservice.share;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimestampUtil {
    private TimestampUtil() {
        //SONARQ
    }
    public static Timestamp getCurrentTimestamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return Timestamp.valueOf(localDateTime);
    }
}
