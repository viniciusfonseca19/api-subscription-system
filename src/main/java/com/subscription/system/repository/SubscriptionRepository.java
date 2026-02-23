package com.subscription.system.repository;

import com.subscription.system.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    boolean existsByUserAndStatus(User user, SubscriptionStatus status);

    List<Subscription> findByStatusAndExpirationDateBefore(
            SubscriptionStatus status,
            LocalDateTime date
    );
}