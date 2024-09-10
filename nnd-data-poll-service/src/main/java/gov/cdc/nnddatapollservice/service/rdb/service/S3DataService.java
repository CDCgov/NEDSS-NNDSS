package gov.cdc.nnddatapollservice.service.rdb.service;

import gov.cdc.nnddatapollservice.service.rdb.service.interfaces.IS3DataService;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    public void persistToS3(String records, String fileName) {
        try {
            // Create an InputStream from the JSON string
            InputStream inputStream = new ByteArrayInputStream(records.getBytes(StandardCharsets.UTF_8));

            // Create PutObjectRequest
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Upload the file to S3
            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, records.length()));

            System.out.println("Successfully persisted to S3: " + fileName);

        } catch (S3Exception e) {
            System.err.println("S3 error: " + e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            System.err.println("Error persisting data: " + e.getMessage());
        }
    }

    public void persistToS3MultiPart(String records, String fileName) {
        try {
            // Create an InputStream from the data (we'll assume large data for this case)
            InputStream inputStream = new ByteArrayInputStream(records.getBytes(StandardCharsets.UTF_8));
            long contentLength = records.length();

            // Use multipart upload for large files
            String uploadId = initiateMultipartUpload(fileName);

            List<CompletedPart> completedParts = new ArrayList<>();
            byte[] buffer = new byte[5 * 1024 * 1024]; // 5MB buffer size
            int partNumber = 1;
            int bytesRead;

            // Split data into chunks and upload
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // Create a new byte array that represents the valid portion of the buffer
                byte[] validBytes = new byte[bytesRead];
                System.arraycopy(buffer, 0, validBytes, 0, bytesRead);

                UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .uploadId(uploadId)
                        .partNumber(partNumber)
                        .build();

                // Use the validBytes array which contains only the read bytes
                CompletedPart completedPart = CompletedPart.builder()
                        .partNumber(partNumber)
                        .eTag(s3Client.uploadPart(uploadPartRequest,
                                software.amazon.awssdk.core.sync.RequestBody.fromBytes(validBytes)).eTag())
                        .build();

                completedParts.add(completedPart);
                partNumber++;
            }

            // Complete the multipart upload
            completeMultipartUpload(uploadId, fileName, completedParts);
            System.out.println("Successfully persisted to S3: " + fileName);

        } catch (Exception e) {
            System.err.println("Error persisting data: " + e.getMessage());
        }
    }

    private String initiateMultipartUpload(String fileName) {
        CreateMultipartUploadRequest multipartUploadRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        CreateMultipartUploadResponse response = s3Client.createMultipartUpload(multipartUploadRequest);
        return response.uploadId();
    }

    private void completeMultipartUpload(String uploadId, String fileName, List<CompletedPart> completedParts) {
        CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
                .parts(completedParts)
                .build();

        CompleteMultipartUploadRequest completeMultipartUploadRequest = CompleteMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .uploadId(uploadId)
                .multipartUpload(completedMultipartUpload)
                .build();

        s3Client.completeMultipartUpload(completeMultipartUploadRequest);
    }
}
