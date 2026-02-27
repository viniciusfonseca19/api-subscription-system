package com.subscription.system.repository;

import com.subscription.system.entity.Subscription;
import com.subscription.system.entity.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    List<Subscription> findByStatusAndExpirationDateBefore(
            SubscriptionStatus status,
            LocalDateTime dateTime
    );

    List<Subscription> findByUserId(Long userId);
}