package com.subscription.system.service;

import com.subscription.system.entity.Subscription;
import com.subscription.system.entity.SubscriptionStatus;
import com.subscription.system.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

    @Service
    public class SubscriptionService {

        private final SubscriptionRepository subscriptionRepository;

        public SubscriptionService(SubscriptionRepository subscriptionRepository) {
            this.subscriptionRepository = subscriptionRepository;
        }

        /**
         * Cria uma nova assinatura.
         * Só permite se o usuário não tiver uma ACTIVE.
         */
        @Transactional
        public Subscription createSubscription(Long userId, String plan) {

            Optional<Subscription> activeSubscription =
                    subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

            if (activeSubscription.isPresent()) {
                throw new IllegalStateException("User already has an active subscription.");
            }

            Subscription subscription = new Subscription();
            subscription.setUserId(userId);
            subscription.setPlan(plan);
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setStartDate(LocalDateTime.now());

            return subscriptionRepository.save(subscription);
        }

    /**
     * Cancela assinatura ativa
     */
    @Transactional
    public void cancelSubscription(Long userId) {

        Subscription subscription = subscriptionRepository
                .findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Active subscription not found."));

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setEndDate(LocalDateTime.now());

        subscriptionRepository.save(subscription);
    }

    /**
     * Upgrade de plano (mantém histórico)
     */
    @Transactional
    public Subscription upgradeSubscription(Long userId, String newPlan) {

        Subscription currentSubscription = subscriptionRepository
                .findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Active subscription not found."));

        // Finaliza assinatura atual
        currentSubscription.setStatus(SubscriptionStatus.CANCELLED);
        currentSubscription.setEndDate(LocalDateTime.now());
        subscriptionRepository.save(currentSubscription);

        // Cria nova assinatura
        Subscription newSubscription = new Subscription();
        newSubscription.setUserId(userId);
        newSubscription.setPlan(newPlan);
        newSubscription.setStatus(SubscriptionStatus.ACTIVE);
        newSubscription.setStartDate(LocalDateTime.now());

        return subscriptionRepository.save(newSubscription);
    }
}