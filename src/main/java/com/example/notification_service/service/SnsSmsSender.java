package com.example.notification_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SnsException;

@Service
public class SnsSmsSender implements SmsSender {

    private static final Logger log = LoggerFactory.getLogger(SnsSmsSender.class);

    private final SnsClient snsClient;

    public SnsSmsSender(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        PublishRequest request = PublishRequest.builder()
                .phoneNumber(phoneNumber)
                .message(message)
                .build();

        try {
            snsClient.publish(request);
            log.info("SMS sent via SNS to {}", phoneNumber);
        } catch (SnsException e) {
            log.error("Failed to send SMS via SNS: {}", e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
}
