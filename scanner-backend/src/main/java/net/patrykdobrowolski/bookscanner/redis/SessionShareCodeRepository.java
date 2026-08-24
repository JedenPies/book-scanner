package net.patrykdobrowolski.bookscanner.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionShareCodeRepository extends CrudRepository<SessionShareCodeEntity, String> {

    Optional<SessionShareCodeEntity> findByShareCode(String code);
}
