package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.controllers;

import jakarta.annotation.security.PermitAll;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.fqw.idrisov.daniyar.auth.api.AuthApi;
import ru.kpfu.itis.fqw.idrisov.daniyar.auth.model.*;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.AuthService;

@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("${openapi.openAPI.base-path:/api/v1}")
public class AuthController implements AuthApi {

    AuthService authService;

    @PermitAll
    @Override
    public ResponseEntity<AccountDto> signUp(SignUpDto signUpDto) {
        var dto = authService.signUp(signUpDto);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @Override
    public ResponseEntity<JwtDto> signIn(AuthDto authDto) {
        var dto = authService.signIn(authDto);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @Override
    public ResponseEntity<JwtDto> refreshToken(RefreshDto refreshDto) {
        var dto = authService.refreshToken(refreshDto);
        return ResponseEntity.ok(dto);
    }
}
