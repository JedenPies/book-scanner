package net.patrykdobrowolski.bookscanner.rest;

import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.rest.dto.CreateScanRequestDto;
import net.patrykdobrowolski.bookscanner.rest.dto.EditScanCommandDto;
import net.patrykdobrowolski.bookscanner.rest.dto.ScanDto;
import net.patrykdobrowolski.bookscanner.rest.mapper.ScanDtoMapper;
import net.patrykdobrowolski.bookscanner.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions/{sessionId}/scans")
@RequiredArgsConstructor
public class ScansResource {

    private final SessionService sessionService;
    private final ScanDtoMapper scanDtoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScanDto createNewScanRequest(@PathVariable UUID sessionId, @Validated @RequestBody CreateScanRequestDto scanRequestDto) throws SessionNotFoundException {
        Scan scan = sessionService.createScan(sessionId, scanRequestDto.getIsbn());
        return scanDtoMapper.toDto(scan);
    }

    @GetMapping
    public List<ScanDto> getScans(@PathVariable UUID sessionId) throws SessionNotFoundException {
        List<Scan> scans = sessionService.getScans(sessionId);
        return scans.stream().map(scanDtoMapper::toDto).toList();
    }

    @PostMapping("{scanId}/retry")
    @ResponseStatus(HttpStatus.CREATED)
    public void fetchRetry(@PathVariable UUID sessionId, @PathVariable UUID scanId) throws ScanNotFoundException, SessionNotFoundException {
        sessionService.retryScan(sessionId, scanId);
    }

    @DeleteMapping("{scanId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScan(@PathVariable UUID sessionId, @PathVariable UUID scanId) throws ScanNotFoundException, SessionNotFoundException {
        sessionService.deleteScan(sessionId, scanId);
    }

    @PatchMapping("{scanId}")
    public ScanDto modifyScan(
            @PathVariable UUID sessionId, @PathVariable UUID scanId,
            @Validated @RequestBody EditScanCommandDto editScanCommandDto) {
        return ScanDto.builder().build();
    }

}
