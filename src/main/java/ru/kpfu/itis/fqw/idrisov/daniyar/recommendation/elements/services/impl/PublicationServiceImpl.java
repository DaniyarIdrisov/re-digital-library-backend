package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.kpfu.itis.fqw.idrisov.daniyar.publication.model.*;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.EntityNotFoundException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers.PublicationMapper;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Keyword;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Publication;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Request;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.PublicationState;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.RequestState;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.RequestType;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa.AuthorRepository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa.KeywordRepository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa.PublicationRepository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.AccountService;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.KeywordService;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.PublicationService;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.utils.WordsUtils;

import java.util.*;

import static ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.constants.ExceptionConstants.*;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {

    final PublicationRepository publicationRepository;
    final PublicationMapper publicationMapper;
    final AccountService accountService;
    final AuthorRepository authorRepository;
    final KeywordRepository keywordRepository;
    final WordsUtils wordsUtils;
    final KeywordService keywordService;

    @Value("${similarity-coefficient}")
    double similarityCoefficient;

    @Override
    @Transactional
    public PublicationDto createPublication(CreatePublicationDto createPublicationDto) {
        var entity = publicationMapper.dtoToEntity(createPublicationDto);
        StringBuilder orderAuthors = new StringBuilder();
        entity.setAuthors(new ArrayList<>());
        for (UUID authorId : createPublicationDto.getAuthorIds()) {
            var author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE));
            orderAuthors.append(author.getFullName())
                    .append(" ")
                    .append(author.getPositionAndTitles())
                    .append("; ");
            entity.getAuthors().add(author);
            author.getPublications().add(entity);
        }
        orderAuthors.delete(orderAuthors.length() - 2, orderAuthors.length() - 1);
        entity.setOrderAuthors(orderAuthors.toString());
        entity.setRequest(createRequest(entity));
        entity.setState(PublicationState.ON_APPROVE);
        entity = publicationRepository.save(entity);
        return publicationMapper.entityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KeywordDto> generateKeywords(UUID id) {
        var entity = publicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PUBLICATION_NOT_FOUND_MESSAGE));
        var keywords = generate(entity);
        return publicationMapper.keywordEntitiesToDto(keywords);
    }

    @Override
    @Transactional
    public PublicationDto setKeywords(UUID id, List<SetKeywordDto> keywordDtoList) {
        var entity = publicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PUBLICATION_NOT_FOUND_MESSAGE));
        setKeywords(keywordDtoList, entity);
        entity.setCode("51" + entity.getId().toString());
        entity = publicationRepository.save(entity);
        return publicationMapper.entityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PublicationDto getPublicationById(UUID id) {
        var entity = publicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PUBLICATION_NOT_FOUND_MESSAGE));
        return publicationMapper.entityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PublicationDto getPublicationByRequestId(UUID requestId) {
        var entity = publicationRepository.findByRequestId(requestId)
                .orElseThrow(() -> new EntityNotFoundException(PUBLICATION_NOT_FOUND_MESSAGE));
        return publicationMapper.entityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicationDto> getPublicationsByAuthorId(UUID authorId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException(AUTHOR_NOT_FOUND_MESSAGE));
        var entities = publicationRepository.findByAuthors(List.of(author));
        return publicationMapper.entitiesToDto(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicationDto> getPublications(GetPublicationsDto dto) {
        var state = dto != null && StringUtils.isNotEmpty(dto.getState()) ? dto.getState() : null;
        var topic = dto != null && StringUtils.isNotEmpty(dto.getTopic()) ? dto.getTopic() : null;
        var keywords = dto != null ? dto.getKeywords() : null;
        var publicationState = PublicationState.valueOf(state);
        List<Publication> entities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(keywords) && StringUtils.isNotEmpty(topic)) {
            var keywordEntities = getKeywordsByLikeConditional(keywords);
            for (Keyword keyword : keywordEntities) {
                var localEntities = publicationRepository.findByStateAndKeywordsAndTopicContainingIgnoreCase(
                        publicationState, List.of(keyword), topic
                );
                entities.addAll(localEntities);
            }
            var setOfEntities = new HashSet<>(entities);
            entities.clear();
            entities.addAll(setOfEntities);
        } else if (!CollectionUtils.isEmpty(keywords)) {
            var keywordEntities = getKeywordsByLikeConditional(keywords);
            for (Keyword keyword : keywordEntities) {
                var localEntities = publicationRepository.findByStateAndKeywords(
                        publicationState,
                        List.of(keyword));
                entities.addAll(localEntities);
            }
            var setOfEntities = new HashSet<>(entities);
            entities.clear();
            entities.addAll(setOfEntities);
        } else if (StringUtils.isNotEmpty(topic)) {
            entities = publicationRepository.findByStateAndTopicContainingIgnoreCase(publicationState, topic);
        } else {
            entities = publicationRepository.findByState(publicationState);
        }
        return publicationMapper.entitiesToDto(entities);
    }

    private Set<Keyword> getKeywordsByLikeConditional(List<String> keywords) {
        var keywordEntities = new HashSet<Keyword>();
        keywords.forEach(keyword -> {
                    var localKeywordEntities = keywordRepository.findByKeywordContainingIgnoreCase(keyword);
                    if (!CollectionUtils.isEmpty(localKeywordEntities)) {
                        keywordEntities.addAll(localKeywordEntities);
                    }
                }
        );
        return keywordEntities;
    }

    private void setKeywords(List<SetKeywordDto> keywordDtoList, Publication publication) {
        for (SetKeywordDto dto : keywordDtoList) {
            Keyword keyword;
            if (dto.getKeywordId() != null) {
                keyword = keywordRepository.findById(dto.getKeywordId())
                        .orElseThrow(() -> new EntityNotFoundException(KEYWORD_NOT_FOUND_MESSAGE));
            } else {
                keyword = keywordRepository.findByKeyword(dto.getKeywordName())
                        .orElse(Keyword.builder()
                                .keyword(dto.getKeywordName())
                                .build());
            }
            publication.getKeywords().add(keyword);
            if (CollectionUtils.isEmpty(keyword.getPublications())) {
                keyword.setPublications(new ArrayList<>());
            }
            keyword.getPublications().add(publication);
        }
    }

    private List<Keyword> generate(Publication entity) {
        var text = entity.getTopic() + " " + entity.getResume();
        var words = wordsUtils.getCleanedWords(text);
        var keywords = keywordService.getAllKeywords();
        if (CollectionUtils.isEmpty(words) || CollectionUtils.isEmpty(keywords)) {
            return Collections.emptyList();
        }

        var generatedKeywords = new HashSet<Keyword>();
        for (var word: words) {
            for (var keyword: keywords) {
                var splitKeywordList = keyword.getKeyword().split(" ");
                for (var splitKeyword: splitKeywordList) {
                    var similarity = wordsUtils.similarity(word, splitKeyword);
                    if (similarity >= similarityCoefficient) {
                        generatedKeywords.add(keyword);
                        break;
                    }
                }
            }
        }
        return new ArrayList<>(generatedKeywords);
    }

    private Request createRequest(Publication publication) {
        return Request.builder()
                .state(RequestState.IN_PROCESSING)
                .type(RequestType.PUBLICATION)
                .createdBy(accountService.getCurrentAccount())
                .publication(publication)
                .build();
    }
}
