package vica.SubWatch.events;

public record SubscriptionCreatedEvent(
        Long userId,
        String userEmail,
        String userName,
        Long subscriptionId,
        String subscriptionName
) {}
