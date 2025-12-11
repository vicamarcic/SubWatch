package vica.SubWatch.records;

import java.math.BigDecimal;

public record CategoryStatsResponse(
        String categoryName,
        BigDecimal total
) {}
