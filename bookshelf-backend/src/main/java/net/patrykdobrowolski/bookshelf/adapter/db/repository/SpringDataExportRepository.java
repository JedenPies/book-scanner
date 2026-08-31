package net.patrykdobrowolski.bookshelf.adapter.db.repository;

import net.patrykdobrowolski.bookshelf.adapter.db.entity.ExportEntity;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataExportRepository extends JpaRepository<ExportEntity, UUID> {

    Optional<ExportEntity> findByTypeAndCorrelationKey(ExportType type, UUID correlationKey);
}
