package gov.cdc.nnddatapollservice.service.rdb.service;

import gov.cdc.nnddatapollservice.service.rdb.service.interfaces.IS3DataService;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class S3DataService implements IS3DataService {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;
    private final S3Client s3Client;

    public S3DataService() {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    public void persistToS3(List<String> records, String fileName) {
        try {
            // Convert records to JSON string
            String jsonData = String.join("\n", records);

            // Create an InputStream from the JSON string
            InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8));

            // Create PutObjectRequest
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Upload the file to S3
            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, jsonData.length()));

            System.out.println("Successfully persisted to S3: " + fileName);

        } catch (S3Exception e) {
            System.err.println("S3 error: " + e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            System.err.println("Error persisting data: " + e.getMessage());
        }
    }
}
