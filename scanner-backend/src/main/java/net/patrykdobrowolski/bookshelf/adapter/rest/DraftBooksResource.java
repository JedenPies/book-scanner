package net.patrykdobrowolski.bookshelf.adapter.rest;

import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.DeleteDraftBookCommandDto;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.DraftBookDto;
import net.patrykdobrowolski.bookshelf.adapter.rest.mapper.EditDraftBookCommandDtoMapper;
import net.patrykdobrowolski.bookshelf.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.DraftBook;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.CreateDraftBookRequestDto;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.EditDraftBookCommandDto;
import net.patrykdobrowolski.bookshelf.adapter.rest.mapper.DraftBookDtoMapper;
import net.patrykdobrowolski.bookshelf.domain.port.DraftBookServicePort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions/{sessionId}/scans")
@RequiredArgsConstructor
public class DraftBooksResource {

    private final DraftBookServicePort scanService;
    private final DraftBookDtoMapper draftBookDtoMapper;
    private final EditDraftBookCommandDtoMapper editDraftBookCommandDtoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DraftBookDto createNewScanRequest(@PathVariable UUID sessionId, @Validated @RequestBody CreateDraftBookRequestDto scanRequestDto) throws SessionNotFoundException {
        DraftBook draftBook = scanService.createScan(sessionId, scanRequestDto.getIsbn());
        return draftBookDtoMapper.toDto(draftBook);
    }

    @GetMapping
    public List<DraftBookDto> getScans(@PathVariable UUID sessionId) throws SessionNotFoundException {
        List<DraftBook> draftBooks = scanService.getScans(sessionId);
        return draftBooks.stream().map(draftBookDtoMapper::toDto).toList();
    }

    @PostMapping("{scanId}/retry")
    @ResponseStatus(HttpStatus.CREATED)
    public void fetchRetry(@PathVariable UUID sessionId, @PathVariable UUID scanId) throws DraftBookNotFoundException, SessionNotFoundException {
        scanService.retryScan(sessionId, scanId);
    }

    @PatchMapping("{scanId}")
    public DraftBookDto modifyScan(
            @PathVariable UUID sessionId, @PathVariable UUID scanId,
            @Validated @RequestBody EditDraftBookCommandDto editDraftBookCommandDto) throws DraftBookNotFoundException, SessionNotFoundException {
        DraftBook updated = scanService.updateScan(sessionId, scanId, editDraftBookCommandDtoMapper.map(editDraftBookCommandDto));
        return draftBookDtoMapper.toDto(updated);
    }

    @PostMapping("delete-requests")
    public void deleteScans(@PathVariable UUID sessionId, @RequestBody DeleteDraftBookCommandDto deleteDraftBookCommandDto) throws SessionNotFoundException {

        scanService.deleteScans(sessionId, deleteDraftBookCommandDto.getScanIds());
    }
}
