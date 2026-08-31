package net.patrykdobrowolski.bookscanner.adapter.rest;

import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.adapter.rest.dto.DeleteScansCommandDto;
import net.patrykdobrowolski.bookscanner.adapter.rest.mapper.EditScanCommandDtoMapper;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.adapter.rest.dto.CreateScanRequestDto;
import net.patrykdobrowolski.bookscanner.adapter.rest.dto.EditScanCommandDto;
import net.patrykdobrowolski.bookscanner.adapter.rest.dto.ScanDto;
import net.patrykdobrowolski.bookscanner.adapter.rest.mapper.ScanDtoMapper;
import net.patrykdobrowolski.bookscanner.domain.port.ScanServicePort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions/{sessionId}/scans")
@RequiredArgsConstructor
public class ScansResource {

    private final ScanServicePort scanService;
    private final ScanDtoMapper scanDtoMapper;
    private final EditScanCommandDtoMapper editScanCommandDtoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScanDto createNewScanRequest(@PathVariable UUID sessionId, @Validated @RequestBody CreateScanRequestDto scanRequestDto) throws SessionNotFoundException {
        Scan scan = scanService.createScan(sessionId, scanRequestDto.getIsbn());
        return scanDtoMapper.toDto(scan);
    }

    @GetMapping
    public List<ScanDto> getScans(@PathVariable UUID sessionId) throws SessionNotFoundException {
        List<Scan> scans = scanService.getScans(sessionId);
        return scans.stream().map(scanDtoMapper::toDto).toList();
    }

    @PostMapping("{scanId}/retry")
    @ResponseStatus(HttpStatus.CREATED)
    public void fetchRetry(@PathVariable UUID sessionId, @PathVariable UUID scanId) throws ScanNotFoundException, SessionNotFoundException {
        scanService.retryScan(sessionId, scanId);
    }

    @PatchMapping("{scanId}")
    public ScanDto modifyScan(
            @PathVariable UUID sessionId, @PathVariable UUID scanId,
            @Validated @RequestBody EditScanCommandDto editScanCommandDto) throws ScanNotFoundException, SessionNotFoundException {
        Scan updated = scanService.updateScan(sessionId, scanId, editScanCommandDtoMapper.map(editScanCommandDto));
        return scanDtoMapper.toDto(updated);
    }

    @PostMapping("delete-requests")
    public void deleteScans(@PathVariable UUID sessionId, @RequestBody DeleteScansCommandDto deleteScansCommandDto) throws SessionNotFoundException {

        scanService.deleteScans(sessionId, deleteScansCommandDto.getScanIds());
    }
}
