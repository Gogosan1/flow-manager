package com.example.flow.manager.util;

import com.example.flow.manager.util.exceptions.SubscriptionNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new SubscriptionNotFoundException("Подписка не найдена!" + response.body());
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
