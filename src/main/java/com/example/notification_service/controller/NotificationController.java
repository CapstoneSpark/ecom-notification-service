package com.example.notification_service.controller;

import com.example.notification_service.dto.NotificationRequest;
import com.example.notification_service.dto.NotificationResponse;
import com.example.notification_service.dto.UserNotificationResponse;
import com.example.notification_service.service.NotificationService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification API", description = "APIs for sending and managing notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ------------------------------
    // Send Notification
    // ------------------------------
    @Operation(
        summary = "Send Notification",
        description = "Used internally by other microservices to send a notification to a user"
    )
    @PostMapping("/send")
    public NotificationResponse sendNotification(
            @Valid @RequestBody NotificationRequest request) {

        return notificationService.sendNotification(request);
    }

    // ------------------------------
    // Get All Notifications for a User
    // ------------------------------
    @Operation(
        summary = "Get User Notifications",
        description = "Returns all notifications for the given user"
    )
    @GetMapping("/user/{userId}")
    public UserNotificationResponse getUserNotifications(
            @Parameter(description = "ID of the user") 
            @PathVariable("userId") Long userId) {

        return notificationService.getUserNotifications(userId);
    }

    // ------------------------------
    // Mark Notification as Read
    // ------------------------------
    @Operation(
        summary = "Mark Notification as Read",
        description = "Updates a notification's status to READ"
    )
    @PutMapping("/{id}/read")
    public NotificationResponse markAsRead(
            @Parameter(description = "Notification ID") 
            @PathVariable("id") Long id) {

        return notificationService.markAsRead(id);
    }
}
