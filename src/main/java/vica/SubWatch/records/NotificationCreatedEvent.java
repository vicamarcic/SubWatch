package vica.SubWatch.records;

public record NotificationCreatedEvent(
        String username,
        NotificationResponse payload
) {}
