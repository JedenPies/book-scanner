package net.patrykdobrowolski.bookshelf.adapter.rest;

import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.Export;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.ExportDto;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.ExportRequestDto;
import net.patrykdobrowolski.bookshelf.adapter.rest.mapper.ExportDtoMapper;
import net.patrykdobrowolski.bookshelf.domain.port.ExportServicePort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cataloging-sessions/{sessionId}")
@RequiredArgsConstructor
public class ExportsResource {

    private final ExportServicePort exportService;
    private final ExportDtoMapper exportDtoMapper;

    @PutMapping("export-request")
    @ResponseStatus(HttpStatus.CREATED)
    public ExportDto createNewExportRequest(
            @PathVariable UUID sessionId, @RequestBody ExportRequestDto exportDto) throws ExportAlreadyRequestedException, CatalogingSessionNotFoundException {
        Export export = exportService.requestExport(sessionId, exportDtoMapper.map(exportDto));
        return exportDtoMapper.map(export);
    }

    @GetMapping("export")
    public ExportDto getExportRequest(@PathVariable UUID sessionId) throws ExportNotRequestedException, CatalogingSessionNotFoundException {
        Export export = exportService.findExport(sessionId);
        return exportDtoMapper.map(export);
    }

    @GetMapping("export/data")
    public ResponseEntity<Resource> downloadExport(@PathVariable UUID sessionId) throws ExportNotRequestedException, CatalogingSessionNotFoundException {
        Export export = exportService.findExport(sessionId);
        ByteArrayResource resource = new ByteArrayResource(export.getData());
        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export-" + export.getId() + "." + export.getFormat().name().toLowerCase())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }
}
