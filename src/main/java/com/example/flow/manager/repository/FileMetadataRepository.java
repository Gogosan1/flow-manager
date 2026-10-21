package com.example.flow.manager.repository;

import com.example.flow.manager.model.entity.FileMetadata;
import com.example.flow.manager.model.enums.SagaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, UUID> {

    @Modifying
    @Query("""
            UPDATE FileMetadata i 
            SET i.status = :newStatus,
                i.processedAt = :processedAt,
                i.convertedFilePath = :convertedFilePath
            WHERE i.id = :id
            AND i.status = :expectedStatus
            """)
    int updateStatus(
            @Param("id") UUID id,
            @Param("expectedStatus") SagaStatus expectedStatus,
            @Param("newStatus") SagaStatus newStatus,
            @Param("convertedFilePath") String convertedFilePath,
            @Param("processedAt") OffsetDateTime processedAt
    );
}