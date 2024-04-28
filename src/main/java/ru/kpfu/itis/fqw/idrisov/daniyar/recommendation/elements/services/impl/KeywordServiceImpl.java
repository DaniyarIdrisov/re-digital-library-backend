package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.fqw.idrisov.daniyar.keyword.model.KeywordDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.ServiceException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers.KeywordMapper;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Keyword;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa.KeywordRepository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.KeywordService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {

    KeywordRepository keywordRepository;
    KeywordMapper keywordMapper;

    @Override
    @Transactional
    public List<KeywordDto> addKeywordsByFile(MultipartFile file) {
        String keywordsInText;
        try {
            var stream = new ByteArrayInputStream(file.getBytes());
            keywordsInText = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ServiceException("Ошбика чтения из файла", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var keywords = keywordsInText.split(";");
        var entities = Arrays.stream(keywords)
                .map(keyword -> (Keyword) Keyword.builder()
                        .keyword(keyword)
                        .build())
                .toList();
        entities = keywordRepository.saveAll(entities);
        return keywordMapper.entitiesToDtoList(entities);
    }

    @Override
    @Transactional
    public List<KeywordDto> addKeywordsByList(List<String> keywords) {
        if (!CollectionUtils.isEmpty(keywords)) {
            var entities = keywords.stream()
                    .map(keyword -> (Keyword) Keyword.builder()
                            .keyword(keyword)
                            .build())
                    .toList();
            entities = keywordRepository.saveAll(entities);
            return keywordMapper.entitiesToDtoList(entities);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("get_all_keywords")
    public List<Keyword> getAllKeywords() {
        return keywordRepository.findAll();
    }
}
