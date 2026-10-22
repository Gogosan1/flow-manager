package com.example.flow.manager.model.entity;

import com.example.flow.manager.model.enums.SagaStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String originalFilePath;
    private String convertedFilePath;
    @Enumerated(EnumType.STRING)
    private SagaStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime processedAt;

}
