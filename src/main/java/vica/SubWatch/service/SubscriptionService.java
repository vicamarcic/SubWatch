package vica.SubWatch.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import vica.SubWatch.domain.Category;
import vica.SubWatch.domain.Subscription;
import vica.SubWatch.domain.SubscriptionDTO;
import vica.SubWatch.domain.User;
import vica.SubWatch.repository.CategoryRepository;
import vica.SubWatch.repository.SubscriptionRepository;

import java.util.List;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;
    private CategoryRepository categoryRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, CategoryRepository categoryRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void createSubscription(User currentUser, @Valid SubscriptionDTO dto) {

        Category category = null;
        if(dto.getCategoryId()!=null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + dto.getCategoryId()));
        }

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

        subscriptionRepository.save(subscription);
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
            dto.setCategoryId(s.getCategory().getId());
        }

        return dto;
    }
}
