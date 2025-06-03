package gov.cdc.nnddataexchangeservice.configuration;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
@SuppressWarnings("java:S1118")
public class TimestampAdapter {

    @SuppressWarnings("java:S2885")
    private static final ThreadLocal<DateFormat> dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static JsonSerializer<Timestamp> getTimestampSerializer() {
        return (src, typeOfSrc, context) -> new JsonPrimitive(dateFormat.get().format(src));
    }

    public static JsonDeserializer<Timestamp> getTimestampDeserializer() {
        return (json, typeOfT, context) -> {
            try {
                return new Timestamp(dateFormat.get().parse(json.getAsJsonPrimitive().getAsString()).getTime());
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        };
    }
}