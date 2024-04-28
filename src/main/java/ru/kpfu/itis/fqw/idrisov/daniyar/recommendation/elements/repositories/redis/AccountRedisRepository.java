package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.repositories.redis;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.models.redis.AccountRedis;

@Repository
public interface AccountRedisRepository extends KeyValueRepository<AccountRedis, String> {
}
