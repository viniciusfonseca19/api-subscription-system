package com.subscription.system.service;

import com.subscription.system.entity.*;
import com.subscription.system.repository.SubscriptionRepository;
import com.subscription.system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // CREATE
    // =========================
    @Transactional
    public Subscription createSubscription(Long userId, String plan) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        subscriptionRepository.findByUserAndStatus(user, SubscriptionStatus.ACTIVE)
                .ifPresent(sub -> {
                    throw new IllegalStateException("User already has active subscription");
                });

        Subscription subscription = new Subscription(user, plan);

        return subscriptionRepository.save(subscription);
    }

    // =========================
    // CANCEL
    // =========================
    @Transactional
    public void cancelSubscription(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Subscription subscription = subscriptionRepository
                .findByUserAndStatus(user, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Active subscription not found"));

        subscription.cancel();
    }

    // =========================
    // UPGRADE
    // =========================
    @Transactional
    public Subscription upgradeSubscription(Long userId, String newPlan) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Subscription current = subscriptionRepository
                .findByUserAndStatus(user, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Active subscription not found"));

        current.cancel();

        Subscription newSubscription = new Subscription(user, newPlan);

        return subscriptionRepository.save(newSubscription);
    }
}