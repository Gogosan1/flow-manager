package com.example.flow.manager.service;

import com.example.flow.manager.controller.client.SubscriptionClient;
import com.example.flow.manager.model.enums.SubscriptionType;
import com.example.flow.manager.util.exceptions.SubscriptionServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionCacheHelper {

    private final SubscriptionClient subscriptionClient;

    @Cacheable(value = "subscriptionTypes", key = "#userLogin")
    @CircuitBreaker(name = "subscriptionService", fallbackMethod = "subscriptionServiceFallback")
    @Retry(name ="subscriptionService")
    public SubscriptionType getSubscriptionType(String userLogin){
        return subscriptionClient.getSubscriptionTypeInfo(userLogin);
    }

    private SubscriptionType subscriptionServiceFallback(String userLogin, Throwable throwable){
        throw new SubscriptionServiceUnavailableException(
                "Сервис подписок временно недоступен"
        );
    }

    @CacheEvict(value = "subscriptionTypes", key = "#userLogin")
    public void invalidateCache(String userLogin){

    }
}
