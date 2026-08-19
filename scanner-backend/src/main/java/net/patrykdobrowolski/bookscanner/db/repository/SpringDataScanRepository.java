package net.patrykdobrowolski.bookscanner.db.repository;

import net.patrykdobrowolski.bookscanner.db.entity.ScanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataScanRepository extends JpaRepository<ScanEntity, UUID> {

    List<ScanEntity> findBySessionId(UUID sessionId);
}
