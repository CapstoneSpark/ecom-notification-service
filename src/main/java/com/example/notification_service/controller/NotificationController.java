package com.example.notification_service.controller;

import com.example.notification_service.dto.NotificationRequest;
import com.example.notification_service.dto.NotificationResponse;
import com.example.notification_service.dto.UserNotificationResponse;
import com.example.notification_service.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Internal endpoint: other services call this to send a custom notification
    @PostMapping("/send")
    public NotificationResponse sendNotification(@Valid @RequestBody NotificationRequest request) {
        return notificationService.sendNotification(request);
    }

    @GetMapping("/user/{userId}")
    public UserNotificationResponse getUserNotifications(@PathVariable("userId") Long userId) {
        return notificationService.getUserNotifications(userId);
    }

    @PutMapping("/{id}/read")
    public NotificationResponse markAsRead(@PathVariable("id") Long id) {
        return notificationService.markAsRead(id);
    }
}
