package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services;

import ru.kpfu.itis.fqw.idrisov.daniyar.account.model.AccountDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Account;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountDto getAccountById(UUID id);

    List<AccountDto> getAccounts();

    Account getCurrentAccount();

    AccountDto appointAdmin(UUID id);

    AccountDto getAccountByToken(String accessToken);
}
