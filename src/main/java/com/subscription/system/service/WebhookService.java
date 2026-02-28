package com.subscription.system.service;

import com.subscription.system.dto.request.PaymentWebhookRequest;
import com.subscription.system.entity.Subscription;
import com.subscription.system.entity.SubscriptionStatus;
import com.subscription.system.entity.User;
import com.subscription.system.exception.BusinessException;
import com.subscription.system.repository.SubscriptionRepository;
import com.subscription.system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WebhookService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public WebhookService(SubscriptionRepository subscriptionRepository,
                          UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void handleWebhook(PaymentWebhookRequest request) {

        //  1 - Busca usuário
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException("User not found."));

        //  2 - Busca assinatura ativa
        Subscription active = subscriptionRepository
                .findByUserAndStatus(user, SubscriptionStatus.ACTIVE)
                .orElseThrow(() ->
                        new BusinessException("No active subscription.")
                );

        //  3 - Processa evento
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
    }
}