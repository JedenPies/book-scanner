package net.patrykdobrowolski.bookscanner.adapter.db.repository;

import net.patrykdobrowolski.bookscanner.adapter.db.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataSessionRepository extends JpaRepository<SessionEntity, UUID> {
}
