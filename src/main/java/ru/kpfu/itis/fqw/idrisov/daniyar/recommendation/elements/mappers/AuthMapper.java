package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import ru.kpfu.itis.fqw.idrisov.daniyar.auth.model.AccountDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.auth.model.SignUpDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Account;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    Account dtoToEntity(SignUpDto dto);

    AccountDto entityToDto(Account entity);
}
