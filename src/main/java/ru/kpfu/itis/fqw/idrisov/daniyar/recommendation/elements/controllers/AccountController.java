package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.controllers;

import jakarta.annotation.security.PermitAll;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.fqw.idrisov.daniyar.account.api.AccountApi;
import ru.kpfu.itis.fqw.idrisov.daniyar.account.model.AccountDto;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("${openapi.openAPI.base-path:/api/v1}")
public class AccountController implements AccountApi {

    AccountService accountService;

    @PermitAll
    @Override
    public ResponseEntity<List<AccountDto>> getAccounts() {
        var dtoList = accountService.getAccounts();
        return ResponseEntity.ok(dtoList);
    }

    @PermitAll
    @Override
    public ResponseEntity<AccountDto> getAccountById(UUID id) {
        var dto = accountService.getAccountById(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @Override
    public ResponseEntity<AccountDto> appointAdmin(UUID id) {
        var dto = accountService.appointAdmin(id);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @Override
    public ResponseEntity<AccountDto> getAccountByToken(String accessToken) {
        var dto = accountService.getAccountByToken(accessToken);
        return ResponseEntity.ok(dto);
    }
}
