package com.example.notification_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class AwsConfig {

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    // -------------------------------
    // TRUST-ALL TrustManager (same style as S3 example)
    // -------------------------------
    private TrustManager[] trustAllManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
        };
    }

    private StaticCredentialsProvider creds() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );
    }

    // -------------------------------
    // SES (Apache Http Client)
    // -------------------------------
    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .region(Region.of(region))
                .credentialsProvider(creds())
                .httpClientBuilder(
                        ApacheHttpClient.builder()
                                .tlsTrustManagersProvider(this::trustAllManagers)
                )
                .build();
    }

    // -------------------------------
    // SNS (Apache Http Client)
    // -------------------------------
    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(creds())
                .httpClientBuilder(
                        ApacheHttpClient.builder()
                                .tlsTrustManagersProvider(this::trustAllManagers)
                )
                .build();
    }

    
    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(creds())
                .httpClientBuilder(
                        NettyNioAsyncHttpClient.builder()
                                .tlsTrustManagersProvider(this::trustAllManagers)
                )
                .build();
    }
}
