package com.example.flow.manager.event;

import java.util.List;

public record ExpiredSubscriptionEvent (
        String userLogin
) {
}
