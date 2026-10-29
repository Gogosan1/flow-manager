package com.example.flow.manager.kafka;

import com.example.flow.manager.event.ExpiredSubscriptionEvent;
import com.example.flow.manager.service.SubscriptionCacheHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisInvalidationSubscriptionConsumer {

    private final SubscriptionCacheHelper subscriptionCacheHelper;

    @KafkaListener(topics = "${spring.kafka.subscription-invalidation-topic}",
            properties = {
                    "spring.json.value.default.type=com.example.flow.manager.event.ExpiredSubscriptionEvent",
                    "spring.json.trusted.packages=com.example.flow.manager.event",
                    "spring.json.use.type.headers=false"
            }
    )
    public void listen(ExpiredSubscriptionEvent event) {
        subscriptionCacheHelper.invalidateCache(event.userLogin());
    }
}
