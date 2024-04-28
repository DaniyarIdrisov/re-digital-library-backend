package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import ru.kpfu.itis.fqw.idrisov.daniyar.publication.model.CreatePublicationDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.publication.model.KeywordDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.publication.model.PublicationDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Keyword;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Publication;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PublicationMapper {

    Publication dtoToEntity(CreatePublicationDto dto);

    PublicationDto entityToDto(Publication entity);

    List<PublicationDto> entitiesToDto(List<Publication> entities);

    List<KeywordDto> keywordEntitiesToDto(List<Keyword> entities);
}
