package gov.cdc.nnddatapollservice.json_config;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.util.Base64;

public class ByteArrayDeserializer implements JsonDeserializer<byte[]> {
    @Override
    public byte[] deserialize(JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws JsonParseException {
        return Base64.getDecoder().decode(json.getAsString());
    }
}