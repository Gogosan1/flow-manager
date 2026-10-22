package com.example.flow.manager.service;

import com.example.flow.manager.event.FileConvertedEvent;
import com.example.flow.manager.event.FileUploadedEvent;
import com.example.flow.manager.infra.storage.MinioStorage;
import com.example.flow.manager.model.entity.FileMetadata;
import com.example.flow.manager.model.enums.SagaStatus;
import com.example.flow.manager.util.exceptions.SourceObjectNotFoundException;
import com.example.flow.manager.util.exceptions.UploadToMinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowManagerService {

    private final PersistenceService persistenceService;
    private final MinioStorage minioStorage;

    public FileUploadedEvent saveToS3(MultipartFile file) {

        try {
            minioStorage.upload(file, file.getOriginalFilename());
        } catch (Exception e) {
            throw new UploadToMinioException("Не удалось загрузить объект в minio");
        }
        log.info("Файл {} заргужен в минио", file.getOriginalFilename());

        return persistenceService.processFile(file.getOriginalFilename());
    }

    public FileMetadata getFileConvertingMetadata(UUID id) {
        return persistenceService.getFileConvertingMetadataById(id);
    }

    public InputStream getFile(String key) {
        try {
            return minioStorage.download(key);
        } catch (Exception e) {
            throw new SourceObjectNotFoundException("Объекта по ключу:" + key + " не существует!");
        }
    }

    public void handle(FileConvertedEvent event) {
        log.info("Событие вернулось {}", event.toString());
        if (event.status().equals(SagaStatus.SUCCESS.name())) {
            persistenceService.updateFileMetadata(event, SagaStatus.SUCCESS);
        }
        if (event.status().equals(SagaStatus.FAILURE.name())) {
            persistenceService.updateFileMetadata(event, SagaStatus.FAILURE);
        }
        log.info("Установлен новый статус для события {}", event.toString());
    }
}
