package net.patrykdobrowolski.bookscanner.rest;

import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.rest.dto.CreateScanRequestDto;
import net.patrykdobrowolski.bookscanner.rest.dto.ModifyScanDto;
import net.patrykdobrowolski.bookscanner.rest.dto.ScanDto;
import net.patrykdobrowolski.bookscanner.rest.mapper.ScanDtoMapper;
import net.patrykdobrowolski.bookscanner.service.ScanService;
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
    private final ScanService scanService;
    private final ScanDtoMapper scanDtoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScanDto createNewScanRequest(@PathVariable UUID sessionId, @Validated @RequestBody CreateScanRequestDto scanRequestDto) throws SessionNotFoundException {
        sessionService.ensureSessionExists(sessionId);
        Scan scan = scanService.createScan(sessionId, scanRequestDto.getIsbn());
        return scanDtoMapper.toDto(scan);
    }

    @GetMapping
    public List<ScanDto> getScans(@PathVariable UUID sessionId, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer page) {
        List<Scan> scans = scanService.getScans(sessionId, pageSize, page);
        return scans.stream().map(scanDtoMapper::toDto).toList();
    }

    @PatchMapping("{scanId}")
    public ScanDto modifyScan(
            @PathVariable UUID sessionId, @PathVariable UUID scanId,
            @Validated @RequestBody ModifyScanDto modifyScanDto) {
        return ScanDto.builder().build();
    }
}
