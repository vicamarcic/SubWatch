package vica.SubWatch.listener;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import vica.SubWatch.records.NotificationCreatedEvent;

@Component
public class NotificationWebSocketListener {
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onNotificationCreated(NotificationCreatedEvent event) {
        messagingTemplate.convertAndSendToUser(
                event.username(),
                "/queue/notifications",
                event.payload()
        );
    }
}
