package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.services;


import ru.kpfu.itis.fqw.idrisov.daniyar.request.model.RequestDto;

import java.util.List;
import java.util.UUID;

public interface RequestService {

    RequestDto comment(UUID id, String comment);

    RequestDto getRequestById(UUID id);

    List<RequestDto> getRequests(String state, String type);

    List<RequestDto> getRequestsByAccountId(UUID accountId);

    RequestDto approve(UUID id);

    RequestDto reject(UUID id);
}
