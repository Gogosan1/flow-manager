package com.example.flow.manager.service;

import com.example.flow.manager.model.enums.SubscriptionType;
import com.example.flow.manager.util.exceptions.SubscriptionInsufficientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionCacheHelper subscriptionCacheHelper;
    private static final long MAX_FILE_SIZE_IN_BYTES_FREE = 10L * 1024 * 1024;

    public void validateUserAccess(String userLogin, Long fileSizeInBytes) {
        SubscriptionType subscriptionType = subscriptionCacheHelper.getSubscriptionType(userLogin);

        if (subscriptionType.equals(SubscriptionType.FREE) && fileSizeInBytes > MAX_FILE_SIZE_IN_BYTES_FREE){
            throw new SubscriptionInsufficientException("Уровня подписки недостаточно!");
        }
    }
}
