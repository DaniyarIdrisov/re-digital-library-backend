package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.base.UpdatableEntity;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.PublicationState;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "publication")
public class Publication extends UpdatableEntity {

    @Column(columnDefinition = "TEXT")
    String topic;

    @Column(name = "order_authors")
    String orderAuthors;

    @Column(columnDefinition = "TEXT")
    String resume;

    @Column(name = "literary references")
    String literaryReferences;

    String code;

    String filename;

    String organization;

    @Enumerated(EnumType.STRING)
    PublicationState state;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "publications")
    List<Keyword> keywords;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "publications")
    List<Author> authors;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "publication")
    Request request;
}
