package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.redis;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRedis {

    @Id
    String id;

    List<String> tokens;
}
