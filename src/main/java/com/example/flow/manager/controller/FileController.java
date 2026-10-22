package com.example.flow.manager.controller;

import com.example.flow.manager.event.FileUploadedEvent;
import com.example.flow.manager.model.entity.FileMetadata;
import com.example.flow.manager.service.FlowManagerService;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FlowManagerService flowManagerService;

    @GetMapping("/download")
    public ResponseEntity<Resource> getFile(@RequestParam String key) {
        InputStream inputStream = flowManagerService.getFile(key);

        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<FileMetadata> getStatus(@RequestParam UUID id) {
        FileMetadata metadata = flowManagerService.getFileConvertingMetadata(id);
        return ResponseEntity.ok(metadata);
    }


    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadedEvent> uploadFile(@RequestParam("file") MultipartFile file) {
        FileUploadedEvent event = flowManagerService.saveToS3(file);
        return ResponseEntity.ok(event);
    }

}
