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
            // Convert byte[] to Base64 and write it as a string
            out.value(Base64.getEncoder().encodeToString(value));
        } else {
            out.nullValue();  // Handle null case
        }
    }

    @Override
    public byte[] read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.STRING) {
            String base64 = in.nextString();
            try {
                return Base64.getDecoder().decode(base64);
            } catch (IllegalArgumentException e) {
                return base64.getBytes();
            }
        } else if (in.peek() == com.google.gson.stream.JsonToken.BEGIN_ARRAY) {
            in.beginArray();
            java.util.List<Byte> byteList = new java.util.ArrayList<>();
            while (in.hasNext()) {
                byteList.add((byte) in.nextInt()); // Read each number as a byte
            }
            in.endArray();

            byte[] byteArray = new byte[byteList.size()];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = byteList.get(i);
            }
            return byteArray;
        } else {
            return null;
        }
    }
}
