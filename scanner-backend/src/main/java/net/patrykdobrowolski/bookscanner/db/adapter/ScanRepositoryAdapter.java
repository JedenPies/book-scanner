package net.patrykdobrowolski.bookscanner.db.adapter;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.db.entity.ScanEntity;
import net.patrykdobrowolski.bookscanner.db.mapper.ScanEntityMapper;
import net.patrykdobrowolski.bookscanner.db.repository.SpringDataScanRepository;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.port.ScanRepositoryPort;

import java.util.List;
import java.util.UUID;

@Named
@RequiredArgsConstructor
public class ScanRepositoryAdapter implements ScanRepositoryPort {

    private final SpringDataScanRepository scanRepository;
    private final ScanEntityMapper scanEntityMapper;

    @Override
    public Scan findById(UUID scanId) throws ScanNotFoundException {
        ScanEntity entity = scanRepository.findById(scanId).orElseThrow(() -> ScanNotFoundException.of(scanId));
        return scanEntityMapper.fromEntity(entity);
    }

    @Override
    public Scan save(Scan scan) {
        ScanEntity saved = scanRepository.save(scanEntityMapper.toEntity(scan));
        return scanEntityMapper.fromEntity(saved);
    }

    @Override
    public List<Scan> findBySessionId(UUID sessionId) {
        return scanRepository.findBySessionIdOrderByCreatedAt(sessionId).stream().map(scanEntityMapper::fromEntity).toList();
    }
}
