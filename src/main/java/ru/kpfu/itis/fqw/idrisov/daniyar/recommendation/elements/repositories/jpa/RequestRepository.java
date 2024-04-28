package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Request;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.RequestState;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.RequestType;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {

    List<Request> findByCreatedById(UUID accountId);

    List<Request> findByState(RequestState state);

    List<Request> findByType(RequestType type);

    List<Request> findByStateAndType(RequestState state, RequestType type);
}
