package vica.SubWatch.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import vica.SubWatch.domain.Category;
import vica.SubWatch.domain.Subscription;
import vica.SubWatch.domain.SubscriptionDTO;
import vica.SubWatch.domain.User;
import vica.SubWatch.events.SubscriptionCreatedEvent;
import vica.SubWatch.events.SubscriptionDeletedEvent;
import vica.SubWatch.events.SubscriptionUpdatedEvent;
import vica.SubWatch.repository.SubscriptionRepository;

import java.util.List;

@Service
public class SubscriptionService {

    private final CategoryService categoryService;
    private final SubscriptionRepository subscriptionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public SubscriptionService(
            CategoryService categoryService,
            SubscriptionRepository subscriptionRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.categoryService = categoryService;
        this.subscriptionRepository = subscriptionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void createSubscription(User currentUser, @Valid SubscriptionDTO dto) {

        Category category = categoryService.findOrCreateCategory(dto.getCategoryName());

        Subscription subscription = new Subscription();
        subscription.setUser(currentUser);
        subscription.setName(dto.getName());
        subscription.setPrice(dto.getPrice());
        subscription.setCurrency(dto.getCurrency());
        subscription.setBillingPeriod(dto.getBillingPeriod());
        subscription.setNextBillingDate(dto.getNextBillingDate());
        subscription.setCategory(category);
        subscription.setAutoRenew(dto.getAutoRenew());
        subscription.setNotes(dto.getNotes());

        Subscription saved = subscriptionRepository.save(subscription);

        eventPublisher.publishEvent(new SubscriptionCreatedEvent(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getName(),
                saved.getId(),
                saved.getName()
        ));
    }

    @Transactional
    public List<SubscriptionDTO> getSubscriptionsForUser(User currentUser) {
        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(currentUser);

        return subscriptions.stream()
                .map(this::toDto)
                .toList();
    }

    private SubscriptionDTO toDto(Subscription s) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setPrice(s.getPrice());
        dto.setCurrency(s.getCurrency());
        dto.setBillingPeriod(s.getBillingPeriod());
        dto.setNextBillingDate(s.getNextBillingDate());
        dto.setAutoRenew(s.isAutoRenew());
        dto.setNotes(s.getNotes());

        if (s.getCategory() != null) {
            dto.setCategoryName(s.getCategory().getName());
        }
        return dto;
    }

    @Transactional
    public void updateSubscription(User currentUser, Long subscriptionId, @Valid SubscriptionDTO dto) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Subscription not found with id: " + subscriptionId
                ));

        if (!subscription.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not allowed to update this subscription.");
        }

        Category category = categoryService.findOrCreateCategory(dto.getCategoryName());

        subscription.setName(dto.getName());
        subscription.setPrice(dto.getPrice());
        subscription.setCurrency(dto.getCurrency());
        subscription.setBillingPeriod(dto.getBillingPeriod());
        subscription.setNextBillingDate(dto.getNextBillingDate());
        subscription.setCategory(category);
        subscription.setAutoRenew(dto.getAutoRenew());
        subscription.setNotes(dto.getNotes());

        Subscription saved = subscriptionRepository.save(subscription);

        eventPublisher.publishEvent(new SubscriptionUpdatedEvent(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getName(),
                saved.getId(),
                saved.getName()
        ));
    }

    @Transactional
    public void deleteSubscription(User currentUser, Long subscriptionId) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Subscription not found with id: " + subscriptionId
                ));

        if (!subscription.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not allowed to delete this subscription.");
        }

        Long id = subscription.getId();
        String name = subscription.getName();

        subscriptionRepository.delete(subscription);

        eventPublisher.publishEvent(new SubscriptionDeletedEvent(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getName(),
                id,
                name
        ));
    }
}