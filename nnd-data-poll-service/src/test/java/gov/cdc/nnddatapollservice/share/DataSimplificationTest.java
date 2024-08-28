package gov.cdc.nnddatapollservice.share;


import gov.cdc.nnddatapollservice.exception.DataPollException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataSimplificationTest {

    @Test
    void testDecodeAndDecompressSuccess() throws DataPollException {
        String originalString = "Test string for compression";
        String encodedCompressedString = compressAndEncode(originalString);

        String result = DataSimplification.decodeAndDecompress(encodedCompressedString);

        assertEquals(originalString, result);
    }

    @Test
    void testDecodeAndDecompressInvalidBase64() throws DataPollException {
        String invalidBase64 = "ThisIsNotABase64EncodedString";
        String result = DataSimplification.decodeAndDecompress(invalidBase64);
        assertEquals(invalidBase64, result);
    }



    private String compressAndEncode(String str) throws DataPollException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {

            gzipOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
            gzipOutputStream.finish();
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new DataPollException("Failed to compress and encode test string");
        }
    }
}