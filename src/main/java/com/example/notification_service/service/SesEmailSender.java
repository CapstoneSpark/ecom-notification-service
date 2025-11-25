package com.example.notification_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class SesEmailSender implements EmailSender {

    private static final Logger log = LoggerFactory.getLogger(SesEmailSender.class);

    private final SesClient sesClient;

    @Value("${aws.ses.from-email}")
    private String fromEmail;

    public SesEmailSender(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        Destination destination = Destination.builder()
                .toAddresses(to)
                .build();

        Message message = Message.builder()
                .subject(Content.builder().data(subject).build())
                .body(Body.builder()
                        .text(Content.builder().data(body).build())
                        .build())
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .destination(destination)
                .message(message)
                .source(fromEmail)
                .build();

        try {
            sesClient.sendEmail(request);
            log.info("Email sent via SES to {}", to);
        } catch (SesException e) {
            log.error("Failed to send email via SES: {}", e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
}
