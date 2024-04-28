package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services;

import ru.kpfu.itis.fqw.idrisov.daniyar.publication.model.*;

import java.util.List;
import java.util.UUID;

public interface PublicationService {

    PublicationDto createPublication(CreatePublicationDto createPublicationDto);

    List<KeywordDto> generateKeywords(UUID id);

    PublicationDto setKeywords(UUID id, List<SetKeywordDto> keywordDtoList);

    PublicationDto getPublicationById(UUID id);

    PublicationDto getPublicationByRequestId(UUID requestId);

    List<PublicationDto> getPublicationsByAuthorId(UUID authorId);

    List<PublicationDto> getPublications(GetPublicationsDto dto);
}
