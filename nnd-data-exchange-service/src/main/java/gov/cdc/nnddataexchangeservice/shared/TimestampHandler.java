package gov.cdc.nnddataexchangeservice.shared;

import java.sql.Date;
import java.sql.Timestamp;

public class TimestampHandler {
    public static Timestamp getCurrentTimeStamp() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        return new Timestamp(currentDate.getTime());
    }
}
