package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.MinioService;

import java.io.InputStream;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@ConditionalOnProperty(value = "minio.enabled", havingValue = "false", matchIfMissing = true)
public class MinioServiceImplMock implements MinioService {

    @Override
    public String uploadFileAndGetFilename(MultipartFile file) {
        return "publication.txt";
    }

    @Override
    public InputStream getObject(String filename) {
        return getClass().getClassLoader().getResourceAsStream("publication/publication.txt");
    }
}
