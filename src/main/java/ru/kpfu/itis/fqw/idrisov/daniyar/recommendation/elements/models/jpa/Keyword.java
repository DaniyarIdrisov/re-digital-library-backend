package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.base.AbstractEntity;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.base.UpdatableEntity;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "keyword")
public class Keyword extends UpdatableEntity {

    @Column(unique = true)
    String keyword;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "publication_keyword",
            joinColumns = {@JoinColumn(name = "keyword_id")},
            inverseJoinColumns = {@JoinColumn(name = "publication_id")}
    )
    List<Publication> publications = new ArrayList<>();
}
