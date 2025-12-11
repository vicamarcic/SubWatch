package vica.SubWatch.projections;

import java.math.BigDecimal;

public interface CategorySpendingProjection {
    String getCategoryName();
    BigDecimal getTotal();
}
