package com.subscription.system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType planType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELED;
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
    }

    public void activate() {
        this.status = SubscriptionStatus.ACTIVE;
    }

    // getters e setters
}