package gov.cdc.nnddatapollservice.json_config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Base64;

public class Base64Adapter extends TypeAdapter<byte[]> {
    @Override
    public void write(JsonWriter out, byte[] value) throws IOException {
        if (value != null) {
            out.value(Base64.getEncoder().encodeToString(value));
        } else {
            out.nullValue();  // Handle null case
        }
    }

    @Override
    public byte[] read(JsonReader in) throws IOException {
        String base64 = in.nextString();
        try {
            return Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException e) {
            // If the value is not properly Base64 encoded, return the byte[] directly
            return base64.getBytes();
        }
    }
}
