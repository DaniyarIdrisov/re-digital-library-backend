package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.controllers;

import jakarta.annotation.security.PermitAll;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.fqw.idrisov.daniyar.file.api.FileApi;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.MinioService;

@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("${openapi.openAPI.base-path:/api/v1}")
public class FileController implements FileApi {

    MinioService minioService;

    @PermitAll
    @Override
    public ResponseEntity<Resource> getFile(String filename) {
        var inputStream = minioService.getObject(filename);
        var inputStreamResource = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment").filename(filename).build().toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(inputStreamResource);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file) {
        var filename = minioService.uploadFileAndGetFilename(file);
        return ResponseEntity.ok(filename);
    }
}
