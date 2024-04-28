package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Author;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Keyword;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Publication;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.PublicationState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, UUID> {

    List<Publication> findByStateAndKeywords(PublicationState state, List<Keyword> keywords);

    List<Publication> findByStateAndTopicContainingIgnoreCase(PublicationState state, String topic);

    List<Publication> findByStateAndKeywordsAndTopicContainingIgnoreCase(PublicationState state,
                                                                         List<Keyword> keywords,
                                                                         String topic);

    List<Publication> findByState(PublicationState state);

    List<Publication> findByAuthors(List<Author> authors);

    Optional<Publication> findByRequestId(UUID requestId);
}
