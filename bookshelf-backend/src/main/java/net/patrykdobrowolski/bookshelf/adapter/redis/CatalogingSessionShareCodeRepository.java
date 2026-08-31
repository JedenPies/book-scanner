package net.patrykdobrowolski.bookshelf.adapter.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CatalogingSessionShareCodeRepository extends CrudRepository<CatalogingSessionShareCodeEntity, String> {

    Optional<CatalogingSessionShareCodeEntity> findByShareCode(String code);
}
