package com.subscription.system.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentWebhookRequest {

    private Long userId;
    private String event;
}