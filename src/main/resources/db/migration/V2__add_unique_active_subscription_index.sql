CREATE UNIQUE INDEX unique_active_subscription
ON subscriptions(user_id)
WHERE status = 'ACTIVE';