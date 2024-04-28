package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.Keyword;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, UUID> {

    List<Keyword> findByKeywordContainingIgnoreCase(String keyword);

     Optional<Keyword> findByKeyword(String keyword);
}
