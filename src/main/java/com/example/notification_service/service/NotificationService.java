package com.example.notification_service.service;

import com.example.notification_service.dto.NotificationRequest;
import com.example.notification_service.dto.NotificationResponse;
import com.example.notification_service.dto.UserNotificationResponse;

public interface NotificationService {

    NotificationResponse sendNotification(NotificationRequest request);

    UserNotificationResponse getUserNotifications(Long userId);

    NotificationResponse markAsRead(Long id);
}
