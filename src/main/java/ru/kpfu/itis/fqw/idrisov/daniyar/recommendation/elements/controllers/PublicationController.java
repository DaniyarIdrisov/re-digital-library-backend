package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.controllers;

import jakarta.annotation.security.PermitAll;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.fqw.idrisov.daniyar.publication.api.PublicationApi;
import ru.kpfu.itis.fqw.idrisov.daniyar.publication.model.*;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.PublicationService;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("${openapi.openAPI.base-path:/api/v1}")
public class PublicationController implements PublicationApi {

    PublicationService publicationService;

    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<PublicationDto> createPublication(CreatePublicationDto createPublicationDto) {
        var dto = publicationService.createPublication(createPublicationDto);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<List<KeywordDto>> generateKeywords(UUID id) {
        var dtoList = publicationService.generateKeywords(id);
        return ResponseEntity.ok(dtoList);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<PublicationDto> setKeywords(UUID id, List<SetKeywordDto> keywordDtoList) {
        var dto = publicationService.setKeywords(id, keywordDtoList);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @Override
    public ResponseEntity<PublicationDto> getPublicationById(UUID id) {
        var dto = publicationService.getPublicationById(id);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @Override
    public ResponseEntity<PublicationDto> getPublicationByRequestId(UUID requestId) {
        var dto = publicationService.getPublicationByRequestId(requestId);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @Override
    public ResponseEntity<List<PublicationDto>> getPublicationsByAuthorId(UUID authorId) {
        var dtoList = publicationService.getPublicationsByAuthorId(authorId);
        return ResponseEntity.ok(dtoList);
    }

    @PermitAll
    @Override
    public ResponseEntity<List<PublicationDto>> getPublications(GetPublicationsDto dto) {
        var dtoList = publicationService.getPublications(dto);
        return ResponseEntity.ok(dtoList);
    }
}
