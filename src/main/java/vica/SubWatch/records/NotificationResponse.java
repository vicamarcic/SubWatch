package vica.SubWatch.records;

import vica.SubWatch.domain.Notification;

public record NotificationResponse(
        Long id,
        String message,
        String subscriptionName,
        boolean read,
        String billingDate,
        Integer daysBefore
) {

    public static NotificationResponse fromEntity(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getMessage(),
                n.getSubscription().getName(),
                n.isRead(),
                n.getBillingDate().toString(),
                n.getDaysBefore()
        );
    }
}
