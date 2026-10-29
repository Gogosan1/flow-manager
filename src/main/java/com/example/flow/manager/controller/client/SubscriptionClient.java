package com.example.flow.manager.controller.client;

import com.example.flow.manager.model.enums.SubscriptionType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "subscriptions")
public interface SubscriptionClient {

    @GetMapping("/api/v1/subscriptions/info/{userLogin}")
    SubscriptionType getSubscriptionTypeInfo(@PathVariable String userLogin);
}
