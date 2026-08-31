package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.export.Export;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportType;

import java.util.Optional;
import java.util.UUID;

public interface ExportRepositoryPort {

    Export findById(UUID id) throws ExportNotFoundException;
    Optional<Export> findByTypeAndCorrelationKey(ExportType type, UUID correlationKey) throws ExportNotFoundException;
    Export save(Export export);
}
