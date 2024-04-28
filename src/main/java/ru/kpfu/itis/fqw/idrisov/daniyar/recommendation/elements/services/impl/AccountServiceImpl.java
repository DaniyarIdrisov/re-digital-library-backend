package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kpfu.itis.fqw.idrisov.daniyar.account.model.AccountDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.EntityNotFoundException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.ServiceBadRequestException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers.AccountMapper;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Account;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.Role;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa.AccountRepository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.security.providers.JwtProvider;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.AccountService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.constants.ExceptionConstants.ACCOUNT_NOT_FOUND_MESSAGE;
import static ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.constants.ExceptionConstants.INCORRECT_ACCESS_TOKEN;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    AccountMapper accountMapper;
    JwtProvider jwtProvider;

    @Override
    @Transactional(readOnly = true)
    public AccountDto getAccountById(UUID id) {
        var entity = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE));
        return accountMapper.entityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccounts() {
        var entities = accountRepository.findAll();
        return accountMapper.entitiesToDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return accountRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE));
    }

    @Override
    @Transactional
    public AccountDto appointAdmin(UUID id) {
        var entity = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE));
        entity.getRoles().add(Role.ADMIN);
        entity = accountRepository.save(entity);
        return accountMapper.entityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getAccountByToken(String accessToken) {
        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            var token = accessToken.substring(7);
            if (jwtProvider.validateAccessToken(token)) {
                var claims = jwtProvider.getAccessClaims(token);
                var email = claims.getSubject();
                var entity = accountRepository.findByEmail(email)
                        .orElseThrow(() -> new EntityNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE));
                return accountMapper.entityToDto(entity);
            }
        }
        throw new ServiceBadRequestException(INCORRECT_ACCESS_TOKEN);
    }
}
