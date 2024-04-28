package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.base.UpdatableEntity;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.AuthorState;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "author")
public class Author extends UpdatableEntity {

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    String patronymic;

    String institution;

    @Column(name = "position_and_titles")
    String positionAndTitles;

    @Column(name = "full_name")
    String fullName;

    @Enumerated(EnumType.STRING)
    AuthorState state;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "publication_author",
            joinColumns = {@JoinColumn(name = "author_id")},
            inverseJoinColumns = {@JoinColumn(name = "publication_id")}
    )
    List<Publication> publications = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "author")
    Request request;
}
