package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.EntityNotFoundException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Account;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.redis.AccountRedis;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.redis.AccountRedisRepository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.AccountRedisService;

import java.util.Collections;

import static ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.constants.ExceptionConstants.ACCOUNT_WITH_THIS_EMAIL_NOT_FOUND_MESSAGE;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountRedisServiceImpl implements AccountRedisService {

    AccountRedisRepository accountRedisRepository;

    @Override
    @Transactional
    public void addRefreshTokenToUser(Account account, String refreshToken) {
        var optionalAccountRedis = accountRedisRepository.findById(account.getEmail());
        AccountRedis accountRedis;
        if (optionalAccountRedis.isPresent()) {
            accountRedis = optionalAccountRedis.get();
            accountRedis.getTokens().add(refreshToken);
        } else {
            accountRedis = createRedisAccount(account, refreshToken);
        }
        accountRedisRepository.save(accountRedis);
    }

    @Override
    @Transactional
    public void deleteOldAndAddNewRefreshToken(Account account, String refreshToken) {
        var optionalAccountRedis = accountRedisRepository.findById(account.getEmail());
        AccountRedis accountRedis;
        if (optionalAccountRedis.isPresent()) {
            accountRedis = optionalAccountRedis.get();
            accountRedis.getTokens().clear();
            accountRedis.setTokens(Collections.singletonList(refreshToken));
        } else {
            accountRedis = createRedisAccount(account, refreshToken);
        }
        accountRedisRepository.save(accountRedis);
    }

    private AccountRedis createRedisAccount(Account account, String refreshToken) {
        return AccountRedis.builder()
                .id(account.getEmail())
                .tokens(Collections.singletonList(refreshToken))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkForRefreshTokenExisting(String email, String refreshToken) {
        var accountRedis = accountRedisRepository.findById(email)
                .orElseThrow(() -> new EntityNotFoundException(ACCOUNT_WITH_THIS_EMAIL_NOT_FOUND_MESSAGE, email));
        return accountRedis.getTokens().contains(refreshToken);
    }
}
