package com.example.flow.manager.kafka;

import com.example.flow.manager.event.FileConvertedEvent;
import com.example.flow.manager.service.FlowManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileConvertedKafkaConsumer {

    private final FlowManagerService flowManagerService;

    @KafkaListener(topics = "${spring.kafka.input-topic}")
    public void listen(FileConvertedEvent event) {
        flowManagerService.handle(event);
    }
}
