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
}