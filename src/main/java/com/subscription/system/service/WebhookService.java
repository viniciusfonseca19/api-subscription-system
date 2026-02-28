package com.subscription.system.service;

import com.subscription.system.dto.request.PaymentWebhookRequest;
import com.subscription.system.entity.Subscription;
import com.subscription.system.entity.SubscriptionStatus;
import com.subscription.system.exception.BusinessException;
import com.subscription.system.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WebhookService {

    private final SubscriptionRepository subscriptionRepository;

    public WebhookService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public void handleWebhook(PaymentWebhookRequest request) {

        Subscription active = subscriptionRepository
                .findByUserIdAndStatus(
                        request.getUserId(),
                        SubscriptionStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new BusinessException("No active subscription.")
                );

        switch (request.getEvent()) {

            case "PAYMENT_FAILED":
                active.cancel();
                break;

            case "RENEWAL":
            case "PAYMENT_SUCCESS":
                active.renew(1);
                break;

            default:
                throw new BusinessException("Invalid event.");
        }

        subscriptionRepository.save(active);
    }
}