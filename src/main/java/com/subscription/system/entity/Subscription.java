package com.subscription.system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  RELACIONAMENTO REAL
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime expirationDate;

    public Subscription() {}

    public Subscription(User user, String plan) {
        this.user = user;
        this.plan = plan;
        this.status = SubscriptionStatus.ACTIVE;
        this.startDate = LocalDateTime.now();
        this.expirationDate = LocalDateTime.now().plusMonths(1);
    }

    //  Regras de negócio
    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.endDate = LocalDateTime.now();
    }

    public void renew(int months) {
        this.expirationDate = this.expirationDate.plusMonths(months);
        this.status = SubscriptionStatus.ACTIVE;
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
        this.endDate = LocalDateTime.now();
    }

    // getters e setter do user
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getPlan() { return plan; }
    public SubscriptionStatus getStatus() { return status; }
}