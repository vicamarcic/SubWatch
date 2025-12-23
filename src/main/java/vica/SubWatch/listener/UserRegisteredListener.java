package vica.SubWatch.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;
import vica.SubWatch.events.UserRegisteredEvent;
import vica.SubWatch.service.EmailService;

@Component
public class UserRegisteredListener {

    private final EmailService emailService;

    public UserRegisteredListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserRegistered(UserRegisteredEvent event) {
        emailService.sendRegistrationSuccessEmail(event.email(), event.displayName());
    }
}
