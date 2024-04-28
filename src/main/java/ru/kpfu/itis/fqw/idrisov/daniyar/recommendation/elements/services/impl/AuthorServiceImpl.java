package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.fqw.idrisov.daniyar.author.model.AuthorDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.author.model.CreateAuthorDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.EntityNotFoundException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers.AuthorMapper;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Author;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Request;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.AuthorState;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.RequestState;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.RequestType;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa.AuthorRepository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.AccountService;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.AuthorService;

import java.util.List;
import java.util.UUID;

import static ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.constants.ExceptionConstants.AUTHOR_NOT_FOUND_MESSAGE;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    AuthorRepository authorRepository;
    AuthorMapper authorMapper;
    AccountService accountService;

    @Override
    @Transactional
    public AuthorDto createAuthor(CreateAuthorDto createAuthorDto) {
        var entity = authorMapper.dtoToEntity(createAuthorDto);
        entity.setFullName(getFullName(entity));
        entity.setState(AuthorState.ON_APPROVE);
        entity.setRequest(createRequest(entity));
        entity = authorRepository.save(entity);
        return authorMapper.entityToDto(entity);
    }

    private Request createRequest(Author author) {
        return Request.builder()
                .state(RequestState.IN_PROCESSING)
                .type(RequestType.AUTHOR)
                .createdBy(accountService.getCurrentAccount())
                .author(author)
                .build();
    }

    private String getFullName(Author entity) {
        var firstName = StringUtils.isNotEmpty(entity.getFirstName()) ? entity.getFirstName() + " " : "";
        var patronymic = StringUtils.isNotEmpty(entity.getPatronymic()) ? entity.getPatronymic() + " " : "";
        var lastName = StringUtils.isNotEmpty(entity.getLastName()) ? entity.getLastName() : "";
        return firstName + patronymic + lastName;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto getAuthorById(UUID id) {
        var entity = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE));
        return authorMapper.entityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto getAuthorByRequestId(UUID requestId) {
        var entity = authorRepository.findByRequestId(requestId)
                .orElseThrow(() -> new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE));
        return authorMapper.entityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> getAuthors(String fullName, String state) {
        List<Author> entities;
        if (StringUtils.isEmpty(fullName)) {
            entities = authorRepository.findByState(AuthorState.valueOf(state));
        } else {
            entities = authorRepository.findByFullNameContainingIgnoreCaseAndState(fullName, AuthorState.valueOf(state));
        }
        return authorMapper.entitiesToDtoList(entities);
    }
}
