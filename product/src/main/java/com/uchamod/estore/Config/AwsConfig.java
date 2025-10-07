package com.uchamod.estore.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {
    @Value("${aws.access.key}")
    private String awsAccessKey;
    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public S3Client s3Client(){
        // Print values to check if they're being loaded (remove in production)
        System.out.println("AWS Access Key: " + (awsAccessKey != null ? awsAccessKey.substring(0, 4) + "..." : "NULL"));
        System.out.println("AWS Region: " + awsRegion);

        if (awsAccessKey == null || awsSecretKey == null) {
            throw new RuntimeException("AWS credentials not found in application.properties");
        }
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }
}
