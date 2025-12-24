package vica.SubWatch.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vica.SubWatch.properties.AppMailProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SmtpEmailService implements EmailService {


    private static final Logger log = LoggerFactory.getLogger(SmtpEmailService.class);

    private final JavaMailSender mailSender;
    private final AppMailProperties mailProperties;

    public SmtpEmailService(JavaMailSender mailSender, AppMailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    @Override
    @Async
    public void sendRegistrationSuccessEmail(String toEmail, String displayName) {
        String subject = "Welcome to SubWatch! ✅ Your account is ready";
        String text = buildText(displayName);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailProperties.getFrom());
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
        } catch (Exception ex) {
            log.error("Failed to send registration email to {}", toEmail, ex);
        }
    }


    private String buildText(String displayName) {
        String name = (displayName == null || displayName.isBlank()) ? "there" : displayName;

        return """
                Hi %s,
                
                Welcome to SubWatch — your account has been successfully created.
                
                You can now sign in and start tracking your subscriptions and recurring expenses.
                Tip: Add your first subscription and set the billing date so SubWatch can remind you before the next charge.
                
                Cheers,
                The SubWatch Team
                """.formatted(name);
    }

    @Override
    @Async
    public void sendSubscriptionCreatedEmail(String toEmail, String displayName, String subscriptionName) {
        String subject = "SubWatch update ✅ Subscription added";
        String text = buildSubscriptionCreatedText(displayName, subscriptionName);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailProperties.getFrom());
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
        } catch (Exception ex) {
            log.error("Failed to send subscription created email to {}", toEmail, ex);
        }
    }

    @Override
    @Async
    public void sendSubscriptionUpdatedEmail(String toEmail, String displayName, String subscriptionName) {
        String subject = "SubWatch update ✅ Subscription updated";
        String text = buildSubscriptionUpdatedText(displayName, subscriptionName);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailProperties.getFrom());
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
        } catch (Exception ex) {
            log.error("Failed to send subscription updated email to {}", toEmail, ex);
        }
    }

    @Override
    @Async
    public void sendSubscriptionDeletedEmail(String toEmail, String displayName, String subscriptionName) {
        String subject = "SubWatch update ✅ Subscription removed";
        String text = buildSubscriptionDeletedText(displayName, subscriptionName);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailProperties.getFrom());
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
        } catch (Exception ex) {
            log.error("Failed to send subscription deleted email to {}", toEmail, ex);
        }
    }
    private String buildSubscriptionCreatedText(String displayName, String subscriptionName) {
        String name = (displayName == null || displayName.isBlank()) ? "there" : displayName;
        String sub = (subscriptionName == null || subscriptionName.isBlank()) ? "your subscription" : subscriptionName;

        return """
            Hi %s,
            
            Good news — "%s" has been added to your SubWatch subscriptions.
            
            You can review or edit it anytime in the app.
            Tip: Make sure the billing date is correct so SubWatch can remind you before the next charge.
            
            Cheers,
            The SubWatch Team
            """.formatted(name, sub);
    }

    private String buildSubscriptionUpdatedText(String displayName, String subscriptionName) {
        String name = (displayName == null || displayName.isBlank()) ? "there" : displayName;
        String sub = (subscriptionName == null || subscriptionName.isBlank()) ? "your subscription" : subscriptionName;

        return """
            Hi %s,
            
            Your subscription "%s" has been updated successfully.
            
            If you didn’t make this change, please review your account activity.
            
            Cheers,
            The SubWatch Team
            """.formatted(name, sub);
    }

    private String buildSubscriptionDeletedText(String displayName, String subscriptionName) {
        String name = (displayName == null || displayName.isBlank()) ? "there" : displayName;
        String sub = (subscriptionName == null || subscriptionName.isBlank()) ? "your subscription" : subscriptionName;

        return """
            Hi %s,
            
            "%s" has been removed from your SubWatch subscriptions.
            
            You can add it back anytime if needed.
            
            Cheers,
            The SubWatch Team
            """.formatted(name, sub);
    }

}