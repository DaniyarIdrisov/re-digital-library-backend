package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import ru.kpfu.itis.fqw.idrisov.daniyar.keyword.model.KeywordDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Keyword;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeywordMapper {

    List<KeywordDto> entitiesToDtoList(List<Keyword> entities);
}
