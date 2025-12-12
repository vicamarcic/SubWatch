package vica.SubWatch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vica.SubWatch.domain.Notification;
import vica.SubWatch.domain.User;
import vica.SubWatch.records.NotificationResponse;
import vica.SubWatch.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @AuthenticationPrincipal User currentUser
    ) {
        List<Notification> notifications =
                notificationService.getUnreadNotificationsForUser(currentUser);

        List<NotificationResponse> body = notifications.stream()
                .map(NotificationResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(body);
    }

    @PostMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id
    ) {
        notificationService.markAsRead(id, currentUser);
        return ResponseEntity.noContent().build();
    }

}

