package gov.cdc.nnddatapollservice.configuration;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("java:S1118")
public class TimestampAdapter {

    @SuppressWarnings("java:S2885")
    private static final List<SimpleDateFormat> dateFormats = Arrays.asList(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"),  // Existing case
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ,
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),    // ISO format without milliseconds
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS") // ISO format with milliseconds
    );
    // Serializer: Convert Timestamp to JSON
    public static JsonSerializer<Timestamp> getTimestampSerializer() {
        return (src, typeOfSrc, context) -> new JsonPrimitive(dateFormats.get(0).format(src)); // Default format
    }

    // Deserializer: Convert JSON to Timestamp
    public static JsonDeserializer<Timestamp> getTimestampDeserializer() {
        return (json, typeOfT, context) -> {
            String dateStr = json.getAsJsonPrimitive().getAsString();
            for (SimpleDateFormat format : dateFormats) {
                try {
                    return new Timestamp(format.parse(dateStr).getTime());
                } catch (ParseException ignored) {
                    // Try next format
                }
            }
            throw new JsonParseException("Failed to parse Timestamp: " + dateStr);
        };
    }
}