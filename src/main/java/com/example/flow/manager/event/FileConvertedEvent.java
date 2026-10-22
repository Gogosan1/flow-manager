package com.example.flow.manager.event;

import java.util.UUID;

public record FileConvertedEvent(
        UUID eventId,
        String status,
        String newFilePath,
        String errorCode,
        String error
) {
}
