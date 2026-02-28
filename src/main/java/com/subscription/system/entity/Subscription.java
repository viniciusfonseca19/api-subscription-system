package com.subscription.system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    public Subscription() {}

    public Subscription(User user, String plan) {
        this.user = user;
        this.plan = plan;
        this.status = SubscriptionStatus.ACTIVE;
        this.startDate = LocalDateTime.now();
        this.expirationDate = LocalDateTime.now().plusMonths(1);
    }

    // ========== REGRAS DE NEGÓCIO ==========

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.canceledAt = LocalDateTime.now();
    }

    public void renew(int months) {
        this.expirationDate = this.expirationDate.plusMonths(months);
        this.status = SubscriptionStatus.ACTIVE;
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
        this.canceledAt = LocalDateTime.now();
    }

    // ========== GETTERS ==========

    public Long getId() { return id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getPlan() { return plan; }

    public SubscriptionStatus getStatus() { return status; }

    public LocalDateTime getStartDate() { return startDate; }

    public LocalDateTime getExpirationDate() { return expirationDate; }

    public LocalDateTime getCanceledAt() { return canceledAt; }
}