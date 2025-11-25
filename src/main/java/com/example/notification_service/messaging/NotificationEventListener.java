package com.example.notification_service.messaging;

import com.example.notification_service.domain.NotificationChannel;
import com.example.notification_service.domain.NotificationType;
import com.example.notification_service.dto.NotificationRequest;
import com.example.notification_service.dto.events.*;
import com.example.notification_service.service.NotificationService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private final NotificationService notificationService;

    public NotificationEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @SqsListener("${aws.sqs.queues.userRegistered}")
    public void handleUserRegistered(UserRegisteredEvent event) {
        NotificationRequest request = new NotificationRequest();
        request.setUserId(event.getUserId());
        request.setEmail(event.getEmail());
        request.setType(NotificationType.ACCOUNT_VERIFICATION);
        request.setChannel(NotificationChannel.EMAIL);
        request.setTitle("Welcome to our store!");
        request.setMessage("Hi " + event.getFullName() + ", your account has been created.");
        notificationService.sendNotification(request);
    }

    @SqsListener("${aws.sqs.queues.orderPlaced}")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        NotificationRequest request = new NotificationRequest();
        request.setUserId(event.getUserId());
        request.setEmail(event.getEmail());
        request.setType(NotificationType.ORDER_PLACED);
        request.setChannel(NotificationChannel.EMAIL);
        request.setTitle("Order Placed: #" + event.getOrderId());
        request.setMessage("Your order has been placed successfully. Total: " + event.getTotalAmount());
        notificationService.sendNotification(request);
    }

    // Similar handlers for OrderShippedEvent, PaymentCompletedEvent, PaymentFailedEvent
}
