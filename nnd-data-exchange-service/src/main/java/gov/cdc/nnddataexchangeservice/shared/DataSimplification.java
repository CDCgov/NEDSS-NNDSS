package gov.cdc.nnddataexchangeservice.shared;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static gov.cdc.nnddataexchangeservice.constant.DataSyncConstant.BYTE_SIZE;
import static gov.cdc.nnddataexchangeservice.constant.DataSyncConstant.UTF8;

public class DataSimplification {
    public static String dataCompressionAndEncode(String jsonData) throws IOException {
        // Compress the JSON data using GZIP and return the Base64 encoded result
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {

            gzipOutputStream.write(jsonData.getBytes());
            gzipOutputStream.finish();

            byte[] compressedData = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(compressedData);
        }
    }
    // DECODE TEST METHOD
    public static String decodeAndDecompress(String base64EncodedData) {
        byte[] compressedData = Base64.getDecoder().decode(base64EncodedData);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BYTE_SIZE];
            int len;
            while ((len = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            String decompressedJson = byteArrayOutputStream.toString(UTF8);

            return decompressedJson;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
