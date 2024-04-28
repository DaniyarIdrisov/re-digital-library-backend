package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Author;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.AuthorState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {

    Optional<Author> findByRequestId(UUID requestId);

    List<Author> findByFullNameContainingIgnoreCaseAndState(String fullName, AuthorState state);

    List<Author> findByState(AuthorState state);
}
