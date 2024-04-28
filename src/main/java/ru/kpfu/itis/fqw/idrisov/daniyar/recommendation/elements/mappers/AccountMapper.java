package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import ru.kpfu.itis.fqw.idrisov.daniyar.account.model.AccountDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Account;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    AccountDto entityToDto(Account entity);

    List<AccountDto> entitiesToDtoList(List<Account> entities);
}
