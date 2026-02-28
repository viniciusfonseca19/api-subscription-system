package com.subscription.system.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String plan;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime expirationDate;

// ========================
// REGRAS DE NEGÓCIO
// ========================

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.endDate = LocalDateTime.now();
    }

    public void renew(int months) {
        if (this.expirationDate == null) {
            this.expirationDate = LocalDateTime.now().plusMonths(months);
        } else {
            this.expirationDate = this.expirationDate.plusMonths(months);
        }
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
        this.endDate = LocalDateTime.now();
    }

    // ========================
    // GETTERS E SETTERS
    // ========================

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}