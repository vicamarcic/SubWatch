package vica.SubWatch.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;
import vica.SubWatch.events.SubscriptionCreatedEvent;
import vica.SubWatch.events.SubscriptionDeletedEvent;
import vica.SubWatch.events.SubscriptionUpdatedEvent;
import vica.SubWatch.service.EmailService;

@Component
public class SubscriptionEventsListener {

    private final EmailService emailService;

    public SubscriptionEventsListener(EmailService emailService) {
        this.emailService = emailService;
    }
    @Async("mailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreated(SubscriptionCreatedEvent event) {
        emailService.sendSubscriptionCreatedEmail(
                event.userEmail(),
                event.userName(),
                event.subscriptionName()
        );
    }
    @Async("mailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUpdated(SubscriptionUpdatedEvent event) {
        emailService.sendSubscriptionUpdatedEmail(
                event.userEmail(),
                event.userName(),
                event.subscriptionName()
        );
    }
    @Async("mailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDeleted(SubscriptionDeletedEvent event) {
        emailService.sendSubscriptionDeletedEmail(
                event.userEmail(),
                event.userName(),
                event.subscriptionName()
        );
    }
}
