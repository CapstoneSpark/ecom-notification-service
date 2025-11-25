package com.example.notification_service.dto;

import java.util.List;

public class UserNotificationResponse {

    private Long userId;
    private List<NotificationResponse> notifications;

    public UserNotificationResponse(Long userId, List<NotificationResponse> notifications) {
        this.userId = userId;
        this.notifications = notifications;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<NotificationResponse> getNotifications() { return notifications; }
    public void setNotifications(List<NotificationResponse> notifications) { this.notifications = notifications; }
}
