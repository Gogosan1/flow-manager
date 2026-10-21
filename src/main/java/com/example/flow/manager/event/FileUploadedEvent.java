package com.example.flow.manager.event;

import java.util.UUID;

public record FileUploadedEvent(
        UUID eventId,
        String pathToFile
) {
}
