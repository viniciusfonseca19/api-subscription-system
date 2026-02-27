package com.subscription.system.controller;

import com.subscription.system.dto.request.PaymentWebhookRequest;
import com.subscription.system.service.WebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/payment")
    public ResponseEntity<Void> handlePayment(
            @RequestBody PaymentWebhookRequest request) {

        webhookService.handleWebhook(request);
        return ResponseEntity.ok().build();
    }
}