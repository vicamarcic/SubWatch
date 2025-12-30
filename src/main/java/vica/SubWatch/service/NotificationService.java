package vica.SubWatch.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vica.SubWatch.domain.Notification;
import vica.SubWatch.domain.Subscription;
import vica.SubWatch.domain.User;
import vica.SubWatch.records.NotificationCreatedEvent;
import vica.SubWatch.records.NotificationResponse;
import vica.SubWatch.repository.NotificationRepository;
import vica.SubWatch.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private final SubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public NotificationService(SubscriptionRepository subscriptionRepository,
                               NotificationRepository notificationRepository,
                               ApplicationEventPublisher eventPublisher) {
        this.subscriptionRepository = subscriptionRepository;
        this.notificationRepository = notificationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void generateUpcomingBillingNotifications() {
        LocalDate today = LocalDate.now();
        List<Integer> daysBeforeList = List.of(1, 2, 3);

        for (Integer daysBefore : daysBeforeList) {
            LocalDate targetDate = today.plusDays(daysBefore);

            List<Subscription> dueSubscriptions =
                    subscriptionRepository.findByNextBillingDate(targetDate);

            for (Subscription subscription : dueSubscriptions) {
                createNotificationForSubscription(subscription, daysBefore);
            }
        }
    }

    private void createNotificationForSubscription(Subscription subscription, Integer daysBefore) {
        User user = subscription.getUser();
        LocalDate billingDate = subscription.getNextBillingDate();

        boolean alreadyExists = notificationRepository.existsByUserIdAndSubscriptionIdAndBillingDateAndDaysBefore(
                user.getId(),
                subscription.getId(),
                billingDate,
                daysBefore
        );
        if (alreadyExists) return;

        String message = buildMessage(subscription, daysBefore);

        Notification notification = new Notification(
                user,
                subscription,
                message,
                billingDate,
                daysBefore
        );

        Notification saved = notificationRepository.save(notification);

        String username = user.getUsername();

        eventPublisher.publishEvent(new NotificationCreatedEvent(
                username,
                NotificationResponse.fromEntity(saved)
        ));
    }

    private String buildMessage(Subscription subscription, Integer daysBefore) {
        String subName = subscription.getName();
        LocalDate billingDate = subscription.getNextBillingDate();

        return "Subscription \"" + subName + "\" is due in "
                + daysBefore + " day(s), billing date: " + billingDate;
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotificationsForUser(User user) {
        return notificationRepository.findByUserAndReadFalse(user);
    }

    @Transactional
    public void markAsRead(Long notificationId, User currentUser) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Notification not found with id: " + notificationId
                ));

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You cannot modify this notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
