package com.subscription.system.scheduler;

import com.subscription.system.entity.Subscription;
import com.subscription.system.entity.SubscriptionStatus;
import com.subscription.system.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SubscriptionExpirationScheduler {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionExpirationScheduler(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Scheduled(cron = "0 0 2 * * ?") // 2h da manhã todo dia
    public void expireSubscriptions() {

        List<Subscription> expired = subscriptionRepository
                .findByStatusAndExpirationDateBefore(
                        SubscriptionStatus.ACTIVE,
                        LocalDateTime.now()
                );

        expired.forEach(subscription -> {
            subscription.expire();
            subscriptionRepository.save(subscription);
        });
    }
}