package vica.SubWatch.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private LocalDate billingDate;

    @Column(nullable = false)
    private Integer daysBefore;

    @Column(nullable = false)
    private boolean read = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Notification() {
    }

    public Notification(User user,
                        Subscription subscription,
                        String message,
                        LocalDate billingDate,
                        Integer daysBefore) {
        this.user = user;
        this.subscription = subscription;
        this.message = message;
        this.billingDate = billingDate;
        this.daysBefore = daysBefore;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public Integer getDaysBefore() {
        return daysBefore;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBillingDate(LocalDate billingDate) {
        this.billingDate = billingDate;
    }

    public void setDaysBefore(Integer daysBefore) {
        this.daysBefore = daysBefore;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

