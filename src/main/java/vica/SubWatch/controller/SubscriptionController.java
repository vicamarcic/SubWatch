package vica.SubWatch.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vica.SubWatch.domain.ApiResponse;
import vica.SubWatch.domain.SubscriptionDTO;
import vica.SubWatch.domain.User;
import vica.SubWatch.service.SubscriptionService;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse> createSubscription(@AuthenticationPrincipal User currentUser,
                                                          @Valid @RequestBody SubscriptionDTO dto) {
        subscriptionService.createSubscription(currentUser,dto);
        return ResponseEntity.ok(new ApiResponse("Subscription created successfully"));

    }

}
