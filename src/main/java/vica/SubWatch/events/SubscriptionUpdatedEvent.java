package vica.SubWatch.events;

public record SubscriptionUpdatedEvent(
        Long userId,
        String userEmail,
        String userName,
        Long subscriptionId,
        String subscriptionName
) {}
