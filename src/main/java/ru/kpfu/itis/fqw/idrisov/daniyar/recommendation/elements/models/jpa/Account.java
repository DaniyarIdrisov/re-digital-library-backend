package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.enums.Role;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.base.UpdatableEntity;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "account")
public class Account extends UpdatableEntity {

    @Column(unique = true)
    String email;

    @Column(name = "hash_password")
    String hashPassword;

    @Column(name = "full_name")
    String fullName;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    Set<Role> roles;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "createdBy")
    List<Request> requests;
}
