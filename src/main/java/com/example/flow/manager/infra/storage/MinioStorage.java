package com.example.flow.manager.infra.storage;

import com.example.flow.manager.config.properties.MinioAppProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class MinioStorage {

    private final MinioClient minioClient;
    private final MinioAppProperties minioAppProperties;

    public void upload(MultipartFile file, String key) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioAppProperties.bucket())
                        .object(key)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
    }

    public InputStream download(String key) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioAppProperties.bucket())
                        .object(key)
                        .build());
    }
}
