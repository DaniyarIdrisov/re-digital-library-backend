package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.impl;

import io.minio.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.configs.properties.MinioProperties;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.MinioException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.MinioService;

import java.io.InputStream;
import java.time.Instant;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@ConditionalOnProperty(value = "minio.enabled", havingValue = "true")
public class MinioServiceImpl implements MinioService {

    MinioClient client;
    MinioProperties properties;

    @Override
    public String uploadFileAndGetFilename(MultipartFile file) {
        createBucketIfNotExists();
        String filename = createFilename(file.getOriginalFilename());
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        } catch (Exception e) {
            throw new MinioException("Ошибка загрузки файла в minio");
        }
        return filename;
    }

    @Override
    public InputStream getObject(String filename) {
        createBucketIfNotExists();
        InputStream stream;
        try {
            stream = client.getObject(GetObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(filename)
                    .build());
        } catch (Exception e) {
            throw new MinioException("Ошибка получения файла из minio");
        }
        return stream;
    }

    private String createFilename(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        if (extension == null || extension.isBlank()) {
            originalFilename = originalFilename + "(" + Instant.now().getEpochSecond() + ")";
        } else {
            originalFilename = FilenameUtils.removeExtension(originalFilename) + "(" + Instant.now().getEpochSecond() + ")." + extension;
        }
        return originalFilename;
    }

    private void createBucketIfNotExists() {
        try {
            var bucketExistsArgs = BucketExistsArgs.builder()
                    .bucket(properties.getBucket())
                    .build();
            if (!client.bucketExists(bucketExistsArgs)) {
                var makeBucketArgs = MakeBucketArgs.builder()
                        .bucket(properties.getBucket())
                        .build();
                client.makeBucket(makeBucketArgs);
            }
        } catch (Exception e) {
            throw new MinioException("Ошибка работы с bucket в minio");
        }
    }
}
