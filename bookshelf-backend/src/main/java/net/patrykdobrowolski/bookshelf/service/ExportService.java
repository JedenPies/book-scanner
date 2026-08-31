package net.patrykdobrowolski.bookshelf.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.command.ExportCommand;
import net.patrykdobrowolski.bookshelf.domain.model.event.ExportRequestedEvent;
import net.patrykdobrowolski.bookshelf.domain.model.export.Export;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportType;
import net.patrykdobrowolski.bookshelf.domain.port.ExportRepositoryPort;
import net.patrykdobrowolski.bookshelf.domain.port.ExportServicePort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

@Named
@RequiredArgsConstructor
public class ExportService implements ExportServicePort {

    private final ExportRepositoryPort exportRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public Export requestExport(ExportCommand command) throws ExportAlreadyRequestedException, ExportNotFoundException {
        Optional<Export> existingExport = exportRepository.findByTypeAndCorrelationKey(command.getType(), command.getCorrelationKey());
        if (existingExport.isPresent()) {
            if (!existingExport.get().isComplete()) throw new ExportAlreadyRequestedException();
        }
        Export export = existingExport.map(e -> e.reset(command)).orElseGet(() -> Export.createNew(command));
        exportRepository.save(export);
        eventPublisher.publishEvent(ExportRequestedEvent.of(export));
        return export;
    }

    @Transactional
    @Override
    public Export beginExport(UUID exportId) throws ExportNotFoundException {
        Export export = exportRepository.findById(exportId);
        export.begin();
        return exportRepository.save(export);
    }

    @Transactional
    @Override
    public Export findExport(UUID exportId) throws ExportNotFoundException {
        return exportRepository.findById(exportId);
    }

    @Transactional
    @Override
    public Export findForCatalogingSession(UUID sessionId) throws ExportNotFoundException {
        return exportRepository.findByTypeAndCorrelationKey(ExportType.CATALOGING_SESSION, sessionId).orElseThrow(ExportNotFoundException::new);
    }

    @Transactional
    @Override
    public Export save(Export export) {
        return exportRepository.save(export);
    }
}
