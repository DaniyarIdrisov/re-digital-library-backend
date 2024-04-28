package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.fqw.idrisov.daniyar.keyword.api.KeywordApi;
import ru.kpfu.itis.fqw.idrisov.daniyar.keyword.model.KeywordDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.KeywordService;

import java.util.List;

@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("${openapi.openAPI.base-path:/api/v1}")
public class KeywordController implements KeywordApi {

    KeywordService keywordService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @Override
    public ResponseEntity<List<KeywordDto>> addKeywordsByFile(MultipartFile file) {
        var dtoList = keywordService.addKeywordsByFile(file);
        return ResponseEntity.ok(dtoList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @Override
    public ResponseEntity<List<KeywordDto>> addKeywordsByList(List<String> keywords) {
        var dtoList = keywordService.addKeywordsByList(keywords);
        return ResponseEntity.ok(dtoList);
    }
}
