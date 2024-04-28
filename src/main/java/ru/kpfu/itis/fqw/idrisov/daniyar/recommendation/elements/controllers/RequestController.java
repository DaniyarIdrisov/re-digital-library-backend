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
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services.RequestService;
import ru.kpfu.itis.fqw.idrisov.daniyar.request.api.RequestApi;
import ru.kpfu.itis.fqw.idrisov.daniyar.request.model.RequestDto;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("${openapi.openAPI.base-path:/api/v1}")
public class RequestController implements RequestApi {

    RequestService requestService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @Override
    public ResponseEntity<RequestDto> comment(UUID id, String comment) {
        var dto = requestService.comment(id, comment);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @Override
    public ResponseEntity<RequestDto> getRequestById(UUID id) {
        var dto = requestService.getRequestById(id);
        return ResponseEntity.ok(dto);
    }

    @PermitAll
    @Override
    public ResponseEntity<List<RequestDto>> getRequests(String state, String type) {
        var dtoList = requestService.getRequests(state, type);
        return ResponseEntity.ok(dtoList);
    }

    @PermitAll
    @Override
    public ResponseEntity<List<RequestDto>> getRequestsByAccountId(UUID accountId) {
        var dtoList = requestService.getRequestsByAccountId(accountId);
        return ResponseEntity.ok(dtoList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @Override
    public ResponseEntity<RequestDto> approve(UUID id) {
        var dto = requestService.approve(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @Override
    public ResponseEntity<RequestDto> reject(UUID id) {
        var dto = requestService.reject(id);
        return ResponseEntity.ok(dto);
    }
}
