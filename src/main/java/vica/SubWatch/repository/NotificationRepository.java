package vica.SubWatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vica.SubWatch.domain.Notification;
import vica.SubWatch.domain.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserAndReadFalse(User user);

    boolean existsByUserIdAndSubscriptionIdAndBillingDateAndDaysBefore(
            Long userId,
            Long subscriptionId,
            LocalDate billingDate,
            Integer daysBefore
    );
}

