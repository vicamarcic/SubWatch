package vica.SubWatch.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vica.SubWatch.domain.Currency;
import vica.SubWatch.domain.User;
import vica.SubWatch.projections.CategorySpendingProjection;
import vica.SubWatch.projections.MonthlyTimelineProjection;
import vica.SubWatch.records.CategoryStatsResponse;
import vica.SubWatch.records.MonthlyTotalResponse;
import vica.SubWatch.records.TimelinePointResponse;
import vica.SubWatch.service.StatsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/monthly")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<MonthlyTotalResponse> getMonthlyTotal(
            @AuthenticationPrincipal User currentUser,
            @RequestParam Currency currency
    ) {
        BigDecimal total = statsService.getMonthlyTotal(currentUser, currency);
        return ResponseEntity.ok(new MonthlyTotalResponse(currency, total));
    }

    @GetMapping("/by-category")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<CategoryStatsResponse>> getByCategory(
            @AuthenticationPrincipal User currentUser,
            @RequestParam String month
    ) {
        YearMonth ym = YearMonth.parse(month); // npr. "2025-12"
        List<CategorySpendingProjection> projections =
                statsService.getSpendingByCategory(currentUser, ym);

        List<CategoryStatsResponse> body = projections.stream()
                .map(p -> new CategoryStatsResponse(p.getCategoryName(), p.getTotal()))
                .toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/timeline")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<TimelinePointResponse>> getTimeline(
            @AuthenticationPrincipal User currentUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<MonthlyTimelineProjection> projections =
                statsService.getTimeline(currentUser, from, to);

        List<TimelinePointResponse> body = projections.stream()
                .map(p -> new TimelinePointResponse(p.getMonth(), p.getTotal()))
                .toList();

        return ResponseEntity.ok(body);
    }

}

