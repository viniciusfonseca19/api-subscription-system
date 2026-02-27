package com.subscription.system.service;

import com.subscription.system.entity.*;
import com.subscription.system.exception.BusinessException;
import com.subscription.system.exception.ResourceNotFoundException;
import com.subscription.system.repository.SubscriptionRepository;
import com.subscription.system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    //  Criar assinatura FREE automaticamente
    @Transactional
    public void createFreeSubscription(User user) {

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlanType(PlanType.FREE);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setExpirationDate(LocalDateTime.now().plusYears(100)); // FREE não expira

        subscriptionRepository.save(subscription);
    }

    //  Upgrade / Downgrade
    @Transactional
    public void changePlan(Long userId, PlanType newPlan) {

        Subscription active = subscriptionRepository
                .findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException("User has no active subscription."));

        if (active.getPlanType() == newPlan) {
            throw new BusinessException("User already has this plan.");
        }

        // Cancela atual (histórico mantido)
        active.cancel();
        subscriptionRepository.save(active);

        // Cria nova
        Subscription newSubscription = new Subscription();
        newSubscription.setUser(active.getUser());
        newSubscription.setPlanType(newPlan);
        newSubscription.setStatus(SubscriptionStatus.ACTIVE);
        newSubscription.setStartDate(LocalDateTime.now());
        newSubscription.setExpirationDate(LocalDateTime.now().plusMonths(1));

        subscriptionRepository.save(newSubscription);
    }

    //  Cancelamento
    @Transactional
    public void cancelSubscription(Long userId) {

        Subscription active = subscriptionRepository
                .findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException("No active subscription."));

        active.cancel();
        subscriptionRepository.save(active);
    }

    //  Histórico
    public List<Subscription> getUserSubscriptions(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }
}