package com.subscription.system.repository;

import com.subscription.system.entity.Subscription;
import com.subscription.system.entity.SubscriptionStatus;
import com.subscription.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUserAndStatus(User user, SubscriptionStatus status);

    List<Subscription> findByStatusAndExpirationDateBefore(
            SubscriptionStatus status,
            LocalDateTime date
    );
}