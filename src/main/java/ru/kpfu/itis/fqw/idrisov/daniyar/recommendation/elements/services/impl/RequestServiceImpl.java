package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.EntityNotFoundException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers.RequestMapper;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Request;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.AuthorState;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.PublicationState;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.RequestState;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.RequestType;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa.RequestRepository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.RequestService;
import ru.kpfu.itis.fqw.idrisov.daniyar.request.model.RequestDto;

import java.util.List;
import java.util.UUID;

import static ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.constants.ExceptionConstants.REQUEST_NOT_FOUND_MESSAGE;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    RequestRepository requestRepository;
    RequestMapper requestMapper;

    @Override
    @Transactional
    public RequestDto comment(UUID id, String comment) {
        var entity = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(REQUEST_NOT_FOUND_MESSAGE));
        entity.setComment(comment);
        entity = requestRepository.save(entity);
        return requestMapper.entityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto getRequestById(UUID id) {
        var entity = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(REQUEST_NOT_FOUND_MESSAGE));
        return requestMapper.entityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequests(String state, String type) {
        List<Request> entities;
        if (StringUtils.isNotEmpty(state) && StringUtils.isNotEmpty(type)) {
            entities = requestRepository.findByStateAndType(RequestState.valueOf(state), RequestType.valueOf(type));
        } else if (StringUtils.isNotEmpty(state)) {
            entities = requestRepository.findByState(RequestState.valueOf(state));
        } else if (StringUtils.isNotEmpty(type)) {
            entities = requestRepository.findByType(RequestType.valueOf(type));
        } else {
            entities = requestRepository.findAll();
        }
        return requestMapper.entitiesToDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsByAccountId(UUID accountId) {
        var entities = requestRepository.findByCreatedById(accountId);
        return requestMapper.entitiesToDtoList(entities);
    }

    @Override
    @Transactional
    public RequestDto approve(UUID id) {
        var entity = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(REQUEST_NOT_FOUND_MESSAGE));
        entity.setState(RequestState.APPROVED);
        if (entity.getType().equals(RequestType.AUTHOR)) {
            entity.getAuthor().setState(AuthorState.CREATED);
        } else {
            entity.getPublication().setState(PublicationState.CREATED);
        }
        entity = requestRepository.save(entity);
        return requestMapper.entityToDto(entity);
    }

    @Override
    @Transactional
    public RequestDto reject(UUID id) {
        var entity = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(REQUEST_NOT_FOUND_MESSAGE));
        entity.setState(RequestState.REJECTED);
        if (entity.getType().equals(RequestType.AUTHOR)) {
            entity.getAuthor().setState(AuthorState.REJECTED);
        } else {
            entity.getPublication().setState(PublicationState.REJECTED);
        }
        entity = requestRepository.save(entity);
        return requestMapper.entityToDto(entity);
    }
}
