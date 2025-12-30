package vica.SubWatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vica.SubWatch.domain.Notification;
import vica.SubWatch.domain.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserAndReadFalse(User user);

    @Query("""
    select (count(n) > 0) from Notification n
    where n.user.id = :userId
      and n.subscription.id = :subId
      and n.billingDate = :billingDate
      and n.daysBefore = :daysBefore
""")
    boolean existsSameNotification(Long userId, Long subId, LocalDate billingDate, Integer daysBefore);
}

