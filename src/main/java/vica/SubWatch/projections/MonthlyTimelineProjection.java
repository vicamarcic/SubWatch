package vica.SubWatch.projections;

import java.math.BigDecimal;

public interface MonthlyTimelineProjection {
    String getMonth();
    BigDecimal getTotal();
}
