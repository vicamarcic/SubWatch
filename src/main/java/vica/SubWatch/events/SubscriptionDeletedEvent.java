package vica.SubWatch.events;

public record SubscriptionDeletedEvent(
        Long userId,
        String userEmail,
        String userName,
        Long subscriptionId,
        String subscriptionName
) {}