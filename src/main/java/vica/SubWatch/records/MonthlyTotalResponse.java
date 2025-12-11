package vica.SubWatch.records;

import vica.SubWatch.domain.Currency;

import java.math.BigDecimal;

public record MonthlyTotalResponse(
        Currency currency,
        BigDecimal totalMonthlyAmount
) {}
