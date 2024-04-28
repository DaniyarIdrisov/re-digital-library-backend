package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.jpa.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class UpdatableEntity extends AbstractEntity {

    @Column(name = "updated_at")
    @UpdateTimestamp
    OffsetDateTime updatedAt;
}
