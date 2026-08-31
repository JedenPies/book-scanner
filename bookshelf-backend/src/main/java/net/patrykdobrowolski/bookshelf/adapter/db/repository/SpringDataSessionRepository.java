package net.patrykdobrowolski.bookshelf.adapter.db.repository;

import net.patrykdobrowolski.bookshelf.adapter.db.entity.CatalogingSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataSessionRepository extends JpaRepository<CatalogingSessionEntity, UUID> {
}
