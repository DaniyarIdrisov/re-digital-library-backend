package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import ru.kpfu.itis.fqw.idrisov.daniyar.author.model.AuthorDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.author.model.CreateAuthorDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Author;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {

    Author dtoToEntity(CreateAuthorDto dto);

    AuthorDto entityToDto(Author entity);

    List<AuthorDto> entitiesToDtoList(List<Author> entities);
}
