package com.example.notification_service.service;

import com.example.notification_service.domain.Notification;
import com.example.notification_service.domain.NotificationChannel;
import com.example.notification_service.domain.NotificationStatus;
import com.example.notification_service.dto.NotificationRequest;
import com.example.notification_service.dto.NotificationResponse;
import com.example.notification_service.dto.UserNotificationResponse;
import com.example.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailSender emailSender;
    private final SmsSender smsSender;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   EmailSender emailSender,
                                   SmsSender smsSender) {
        this.notificationRepository = notificationRepository;
        this.emailSender = emailSender;
        this.smsSender = smsSender;
    }

    @Override
    public NotificationResponse sendNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setEmail(request.getEmail());
        notification.setPhoneNumber(request.getPhoneNumber());
        notification.setType(request.getType());
        notification.setChannel(request.getChannel());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setStatus(NotificationStatus.PENDING);

        notification = notificationRepository.save(notification);

        try {
            if (request.getChannel() == NotificationChannel.EMAIL) {
                emailSender.sendEmail(request.getEmail(), request.getTitle(), request.getMessage());
            } else if (request.getChannel() == NotificationChannel.SMS) {
                smsSender.sendSms(request.getPhoneNumber(), request.getMessage());
            }
            notification.setStatus(NotificationStatus.SENT);
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
        }

        notification = notificationRepository.save(notification);
        return mapToResponse(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public UserNotificationResponse getUserNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<NotificationResponse> responseList = notifications.stream()
                .map(this::mapToResponse)
                .toList();
        return new UserNotificationResponse(userId, responseList);
    }

    @Override
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(Instant.now());

        notification = notificationRepository.save(notification);
        return mapToResponse(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        NotificationResponse res = new NotificationResponse();
        res.setId(notification.getId());
        res.setUserId(notification.getUserId());
        res.setEmail(notification.getEmail());
        res.setPhoneNumber(notification.getPhoneNumber());
        res.setType(notification.getType());
        res.setChannel(notification.getChannel());
        res.setStatus(notification.getStatus());
        res.setTitle(notification.getTitle());
        res.setMessage(notification.getMessage());
        res.setErrorMessage(notification.getErrorMessage());
        res.setCreatedAt(notification.getCreatedAt());
        res.setUpdatedAt(notification.getUpdatedAt());
        res.setReadAt(notification.getReadAt());
        return res;
    }
}
