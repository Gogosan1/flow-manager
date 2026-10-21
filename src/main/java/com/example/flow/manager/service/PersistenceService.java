package com.example.flow.manager.service;

import com.example.flow.manager.event.FileConvertedEvent;
import com.example.flow.manager.event.FileUploadedEvent;
import com.example.flow.manager.model.entity.FileMetadata;
import com.example.flow.manager.model.entity.Outbox;
import com.example.flow.manager.model.enums.SagaStatus;
import com.example.flow.manager.repository.FileMetadataRepository;
import com.example.flow.manager.repository.OutboxRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersistenceService {

    private final OutboxRepository outboxRepository;
    private final FileMetadataRepository fileMetadataRepository;
    private final JsonMapper jsonMapper;

    @Transactional
    public FileUploadedEvent processFile(String pathToFile) {

        FileMetadata metadata = new FileMetadata();
        metadata.setOriginalFilePath(pathToFile);
        metadata.setStatus(SagaStatus.PROCESS);
        metadata.setCreatedAt(OffsetDateTime.now());
        fileMetadataRepository.save(metadata);

        FileUploadedEvent event = new FileUploadedEvent(metadata.getId(), pathToFile);

        Outbox outbox = new Outbox();
        outbox.setAggregateType("files-to-convert");
        outbox.setAggregateId(event.eventId().toString());
        outbox.setEventType(SagaStatus.PROCESS.name());
        outbox.setPayload(jsonMapper.writeValueAsString(event));
        outbox.setCreatedAt(OffsetDateTime.now());
        outboxRepository.save(outbox);

        return event;
    }

    @Transactional
    public FileMetadata getFileConvertingMetadataById(UUID id) {
        FileMetadata metadata = fileMetadataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Файла с таким id:" + id + " не существует!"));
        return metadata;
    }

    @Transactional
    public void updateFileMetadata(FileConvertedEvent event, SagaStatus newStatus) {
        fileMetadataRepository.updateStatus(
                event.eventId(),
                SagaStatus.PROCESS,
                newStatus,
                event.newFilePath(),
                OffsetDateTime.now());
    }
}
