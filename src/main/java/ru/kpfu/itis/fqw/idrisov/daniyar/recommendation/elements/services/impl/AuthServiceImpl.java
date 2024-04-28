package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.impl;

import io.jsonwebtoken.JwtException;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.fqw.idrisov.daniyar.auth.model.*;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.EntityConflictException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.ServiceBadRequestException;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.mappers.AuthMapper;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Account;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.Role;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa.AccountRepository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.security.providers.JwtProvider;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.AccountRedisService;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.AuthService;

import java.util.Collections;

import static ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.exceptions.constants.ExceptionConstants.*;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    AccountRepository accountRepository;
    AuthMapper authMapper;
    PasswordEncoder passwordEncoder;
    JwtProvider jwtProvider;
    AccountRedisService accountRedisService;

    @Override
    @Transactional
    public AccountDto signUp(@NotNull SignUpDto dto) {
        checkForEmptyFields(dto);
        checkForEmailExisting(dto);
        var entity = authMapper.dtoToEntity(dto);
        var hashPassword = passwordEncoder.encode(dto.getPassword());
        entity.setHashPassword(hashPassword);
        entity.setRoles(Collections.singleton(Role.USER));
        entity = accountRepository.save(entity);
        return authMapper.entityToDto(entity);
    }

    private void checkForEmptyFields(SignUpDto dto) {
        if (dto.getEmail() == null) {
            throw new ServiceBadRequestException(EMPTY_FIELD_MESSAGE, "email");
        }
        if (dto.getFullName() == null) {
            throw new ServiceBadRequestException(EMPTY_FIELD_MESSAGE, "fullName");
        }
        if (dto.getPassword() == null) {
            throw new ServiceBadRequestException(EMPTY_FIELD_MESSAGE, "password");
        }
    }

    private void checkForEmailExisting(SignUpDto dto) {
        if (accountRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EntityConflictException(ACCOUNT_WITH_THIS_EMAIL_EXISTS_MESSAGE, dto.getEmail());
        }
    }

    @Override
    @Transactional
    public JwtDto signIn(@NotNull AuthDto dto) {
        var accountEntity = accountRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ServiceBadRequestException(INVALID_LOGIN_OR_PASSWORD_MESSAGE));
        checkForPasswordsMatches(accountEntity, dto);
        var accessToken = jwtProvider.generateAccessToken(accountEntity);
        var refreshToken = jwtProvider.generateRefreshToken(accountEntity);
        accountRedisService.addRefreshTokenToUser(accountEntity, refreshToken);
        return new JwtDto()
                .accessToken(accessToken)
                .refreshToken(refreshToken);
    }

    private void checkForPasswordsMatches(Account account, AuthDto dto) {
        if (!passwordEncoder.matches(dto.getPassword(), account.getHashPassword())) {
            throw new ServiceBadRequestException(INVALID_LOGIN_OR_PASSWORD_MESSAGE);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public JwtDto refreshToken(RefreshDto dto) {
        checkRefreshTokenForValidity(dto.getRefreshToken());
        var claims = jwtProvider.getRefreshClaims(dto.getRefreshToken());
        var email = claims.getSubject();
        checkForRefreshTokenExisting(email, dto.getRefreshToken());
        var accountEntity = accountRepository.findByEmail(email)
                .orElseThrow(() -> new JwtException(INVALID_REFRESH_TOKEN_MESSAGE));
        var accessToken = jwtProvider.generateAccessToken(accountEntity);
        var newRefreshToken = jwtProvider.generateRefreshToken(accountEntity);
        accountRedisService.deleteOldAndAddNewRefreshToken(accountEntity, newRefreshToken);
        return new JwtDto()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken);
    }

    private void checkForRefreshTokenExisting(String email, String refreshToken) {
        if (!accountRedisService.checkForRefreshTokenExisting(email, refreshToken)) {
            throw new JwtException(INVALID_REFRESH_TOKEN_MESSAGE);
        }
    }

    private void checkRefreshTokenForValidity(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new JwtException(INVALID_REFRESH_TOKEN_MESSAGE);
        }
    }
}
