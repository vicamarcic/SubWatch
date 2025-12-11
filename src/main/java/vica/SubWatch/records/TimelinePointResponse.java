package vica.SubWatch.records;

import java.math.BigDecimal;

public record TimelinePointResponse(
        String month,
        BigDecimal total
) {}
