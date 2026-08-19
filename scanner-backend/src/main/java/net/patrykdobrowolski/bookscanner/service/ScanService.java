package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookscanner.domain.port.ScanRepositoryPort;

import java.util.List;
import java.util.UUID;

@Named
@RequiredArgsConstructor
@Slf4j
public class ScanService {

    private final ScanRepositoryPort scanRepository;
    private final BookDetailsAsyncFetcherPort bookDetailsFetcher;

    @Transactional
    public Scan createScan(UUID sessionId, String isbnStr) {
        Scan saved = scanRepository.save(new Scan(sessionId, new ISBN(isbnStr)));
        bookDetailsFetcher.fetchBookDetails(saved.getId(), saved.getIsbn());
        return saved;
    }

    @Transactional
    public Scan save(Scan scan) {
        return scanRepository.save(scan);
    }

    @Transactional
    public List<Scan> getScans(UUID sessionId, Integer pageSize, Integer page) {
        return scanRepository.findBySessionId(sessionId);
    }

    @Transactional
    public Scan findScan(UUID scanId) throws ScanNotFoundException {
        return scanRepository.findById(scanId);
    }
}
