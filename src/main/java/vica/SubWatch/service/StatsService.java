package vica.SubWatch.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vica.SubWatch.domain.Currency;
import vica.SubWatch.domain.User;
import vica.SubWatch.projections.CategorySpendingProjection;
import vica.SubWatch.projections.MonthlyTimelineProjection;
import vica.SubWatch.repository.SubscriptionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class StatsService {

    private final SubscriptionRepository subscriptionRepository;

    public StatsService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional(readOnly = true)
    public BigDecimal getMonthlyTotal(User currentUser, Currency currency) {
        return subscriptionRepository.getMonthlyTotalForUserAndCurrency(currentUser, currency);
    }

    @Transactional(readOnly = true)
    public List<CategorySpendingProjection> getSpendingByCategory(User currentUser, YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        return subscriptionRepository.getCategorySpendingForUserAndPeriod(currentUser, start, end);
    }

    @Transactional(readOnly = true)
    public List<MonthlyTimelineProjection> getTimeline(User currentUser, LocalDate from, LocalDate to) {
        return subscriptionRepository.getMonthlyTimelineForUser(currentUser.getId(), from, to);
    }
}

