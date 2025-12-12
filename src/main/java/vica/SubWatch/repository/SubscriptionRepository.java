package vica.SubWatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vica.SubWatch.domain.Currency;
import vica.SubWatch.domain.Subscription;
import vica.SubWatch.domain.User;
import vica.SubWatch.projections.CategorySpendingProjection;
import vica.SubWatch.projections.MonthlyTimelineProjection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUser(User currentUser);

    @Query("""
            SELECT COALESCE(SUM(s.price), 0)
            FROM Subscription s
            WHERE s.user = :user
              AND s.currency = :currency
            """)
    BigDecimal getMonthlyTotalForUserAndCurrency(
            @Param("user") User user,
            @Param("currency") Currency currency
    );

    @Query("""
            SELECT 
                COALESCE(c.name, 'Uncategorized') AS categoryName,
                SUM(s.price) AS total
            FROM Subscription s
            LEFT JOIN s.category c
            WHERE s.user = :user
              AND s.nextBillingDate BETWEEN :start AND :end
            GROUP BY c.name
            """)
    List<CategorySpendingProjection> getCategorySpendingForUserAndPeriod(
            @Param("user") User user,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    // 3) Timeline – native query (PostgreSQL primer)
    @Query(value = """
            SELECT 
                TO_CHAR(s.next_billing_date, 'YYYY-MM') AS month,
                SUM(s.price) AS total
            FROM subscription s
            WHERE s.user_id = :userId
              AND s.next_billing_date BETWEEN :fromDate AND :toDate
            GROUP BY TO_CHAR(s.next_billing_date, 'YYYY-MM')
            ORDER BY month
            """, nativeQuery = true)
    List<MonthlyTimelineProjection> getMonthlyTimelineForUser(
            @Param("userId") Long userId,
            @Param("fromDate") LocalDate from,
            @Param("toDate") LocalDate to
    );

    List<Subscription> findByNextBillingDate(LocalDate targetDate);
}
