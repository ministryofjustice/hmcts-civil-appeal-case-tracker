package uk.gov.moj.cact.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.InputStream;

@Component
public class S3BucketClient {

    private static final String S3_BASE_URL = "https://%s.s3.eu-west-2.amazonaws.com/data.csv";

    private final RestClient restClient;
    private final String bucketName;



    public S3BucketClient(RestClient restClient, @Value("${app.s3-bucket-name}") String bucketName) {
        this.restClient = restClient;
        this.bucketName = bucketName;
    }

    public InputStream downloadCsv() {
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalArgumentException("S3_BUCKET_NAME environment variable is not set");
        }

        return restClient
                .get()
                .uri(String.format(S3_BASE_URL, bucketName))
                .retrieve()
                .body(InputStream.class);
    }
}
