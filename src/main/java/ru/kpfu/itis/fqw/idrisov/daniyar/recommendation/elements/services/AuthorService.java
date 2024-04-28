package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services;

import ru.kpfu.itis.fqw.idrisov.daniyar.author.model.AuthorDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.author.model.CreateAuthorDto;

import java.util.List;
import java.util.UUID;

public interface AuthorService {
    AuthorDto getAuthorById(UUID id);

    AuthorDto getAuthorByRequestId(UUID requestId);

    List<AuthorDto> getAuthors(String fullName, String state);

    AuthorDto createAuthor(CreateAuthorDto createAuthorDto);
}
