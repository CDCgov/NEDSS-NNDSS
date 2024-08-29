package gov.cdc.nnddatapollservice.share;

import gov.cdc.nnddatapollservice.exception.DataPollException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

public class DataSimplification {
    private DataSimplification() {

    }

    @SuppressWarnings("java:S1141")
    public static String decodeAndDecompress(String base64EncodedData) {
        try {
            byte[] compressedData = Base64.getDecoder().decode(base64EncodedData);

            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
                 GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = gzipInputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }

                return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new DataPollException(e.getMessage());
            }
        } catch (Exception e) {
            // Return raw string - un decoded if error occur
            return base64EncodedData;
        }
    }
}
