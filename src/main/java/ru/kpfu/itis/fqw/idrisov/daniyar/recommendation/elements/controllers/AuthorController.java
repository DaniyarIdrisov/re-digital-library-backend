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
import ru.kpfu.itis.fqw.idrisov.daniyar.author.api.AuthorApi;
import ru.kpfu.itis.fqw.idrisov.daniyar.author.model.AuthorDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.author.model.CreateAuthorDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.AuthorService;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("${openapi.openAPI.base-path:/api/v1}")
public class AuthorController implements AuthorApi {

    AuthorService authorService;

    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<AuthorDto> createAuthor(CreateAuthorDto createAuthorDto) {
        var dto = authorService.createAuthor(createAuthorDto);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @Override
    public ResponseEntity<AuthorDto> getAuthorById(UUID id) {
        var dto = authorService.getAuthorById(id);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @Override
    public ResponseEntity<AuthorDto> getAuthorByRequestId(UUID requestId) {
        var dto = authorService.getAuthorByRequestId(requestId);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @Override
    public ResponseEntity<List<AuthorDto>> getAuthors(String state, String fullName) {
        var dto = authorService.getAuthors(fullName, state);
        return ResponseEntity.ok(dto);
    }
}
