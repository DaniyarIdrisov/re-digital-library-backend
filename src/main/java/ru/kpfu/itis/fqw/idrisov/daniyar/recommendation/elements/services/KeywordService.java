package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services;


import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.fqw.idrisov.daniyar.keyword.model.KeywordDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Keyword;

import java.util.List;

public interface KeywordService {
    List<KeywordDto> addKeywordsByFile(MultipartFile file);

    List<KeywordDto> addKeywordsByList(List<String> keywords);

    List<Keyword> getAllKeywords();
}
