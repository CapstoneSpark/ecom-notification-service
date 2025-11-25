package com.example.notification_service.service;

public interface SmsSender {
    void sendSms(String phoneNumber, String message);
}
