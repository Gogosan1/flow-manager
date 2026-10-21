package com.example.flow.manager.config.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioAppProperties(
        String endpoint,
        String accessKey,
        String secretKey,
        String bucket
) {
}
