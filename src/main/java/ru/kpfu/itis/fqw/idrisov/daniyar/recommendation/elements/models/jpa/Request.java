package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.base.UpdatableEntity;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.RequestState;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.RequestType;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "request")
public class Request extends UpdatableEntity {

    String comment;

    @Enumerated(EnumType.STRING)
    RequestState state;

    @Enumerated(EnumType.STRING)
    RequestType type;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    Author author;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "publication_id", referencedColumnName = "id")
    Publication publication;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    Account createdBy;
}
