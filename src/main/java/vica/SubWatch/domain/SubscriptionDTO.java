package vica.SubWatch.domain;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SubscriptionDTO {
    @NotBlank
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull
    private Currency currency;

    @NotNull
    private BillingPeriod billingPeriod;

    @NotNull
    @FutureOrPresent(message = "nextBillingDate can't be in the past!")
    private LocalDate nextBillingDate;

    private Long categoryId;

    @NotNull
    private Boolean autoRenew = true;

    @Size(max = 1000)
    private String notes;

    public SubscriptionDTO(String name, Currency currency, BigDecimal price, BillingPeriod billingPeriod, LocalDate nextBillingDate, Long categoryId, String notes, Boolean autoRenew) {
        this.name = name;
        this.currency = currency;
        this.price = price;
        this.billingPeriod = billingPeriod;
        this.nextBillingDate = nextBillingDate;
        this.categoryId = categoryId;
        this.notes = notes;
        this.autoRenew = autoRenew;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDate getNextBillingDate() {
        return nextBillingDate;
    }

    public void setNextBillingDate(LocalDate nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }
}
