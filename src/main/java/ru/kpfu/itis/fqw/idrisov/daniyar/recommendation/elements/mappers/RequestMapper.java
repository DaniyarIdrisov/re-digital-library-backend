package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Request;
import ru.kpfu.itis.fqw.idrisov.daniyar.request.model.RequestDto;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestMapper {

    RequestDto entityToDto(Request entity);

    List<RequestDto> entitiesToDtoList(List<Request> entities);
}
