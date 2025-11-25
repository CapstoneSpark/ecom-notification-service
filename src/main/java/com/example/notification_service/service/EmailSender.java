package com.example.notification_service.service;

public interface EmailSender {
    void sendEmail(String to, String subject, String body);
}
