package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioService {

    String uploadFileAndGetFilename(MultipartFile file);

    InputStream getObject(String filename);
}
