package vica.SubWatch.service;

public interface EmailService {
    void sendRegistrationSuccessEmail(String toEmail, String displayName);
    void sendSubscriptionCreatedEmail(String toEmail, String displayName, String subscriptionName);
    void sendSubscriptionUpdatedEmail(String toEmail, String displayName, String subscriptionName);
    void sendSubscriptionDeletedEmail(String toEmail, String displayName, String subscriptionName);
}
