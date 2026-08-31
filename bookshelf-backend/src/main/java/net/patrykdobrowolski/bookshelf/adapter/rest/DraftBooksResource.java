package net.patrykdobrowolski.bookshelf.adapter.rest;

import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.DeleteDraftBookCommandDto;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.DraftBookDto;
import net.patrykdobrowolski.bookshelf.adapter.rest.mapper.EditDraftBookCommandDtoMapper;
import net.patrykdobrowolski.bookshelf.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
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
@RequestMapping("/api/cataloging-sessions/{sessionId}/draft-books")
@RequiredArgsConstructor
public class DraftBooksResource {

    private final DraftBookServicePort draftBookService;
    private final DraftBookDtoMapper draftBookDtoMapper;
    private final EditDraftBookCommandDtoMapper editDraftBookCommandDtoMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DraftBookDto createNewDraftBookRequest(@PathVariable UUID sessionId, @Validated @RequestBody CreateDraftBookRequestDto draftBookRequestDto) throws CatalogingSessionNotFoundException {
        DraftBook draftBook = draftBookService.createDraftBook(sessionId, draftBookRequestDto.getIsbn());
        return draftBookDtoMapper.toDto(draftBook);
    }

    @GetMapping
    public List<DraftBookDto> getDraftBooks(@PathVariable UUID sessionId) throws CatalogingSessionNotFoundException {
        List<DraftBook> draftBooks = draftBookService.getDraftBooks(sessionId);
        return draftBooks.stream().map(draftBookDtoMapper::toDto).toList();
    }

    @PostMapping("{draftBookId}/retry")
    @ResponseStatus(HttpStatus.CREATED)
    public void fetchRetry(@PathVariable UUID sessionId, @PathVariable UUID draftBookId) throws DraftBookNotFoundException, CatalogingSessionNotFoundException {
        draftBookService.retryDraftBookFetch(sessionId, draftBookId);
    }

    @PatchMapping("{draftBookId}")
    public DraftBookDto modifyDraftBook(
            @PathVariable UUID sessionId, @PathVariable UUID draftBookId,
            @Validated @RequestBody EditDraftBookCommandDto editDraftBookCommandDto) throws DraftBookNotFoundException, CatalogingSessionNotFoundException {
        DraftBook updated = draftBookService.updateDraftBook(sessionId, draftBookId, editDraftBookCommandDtoMapper.map(editDraftBookCommandDto));
        return draftBookDtoMapper.toDto(updated);
    }

    @PostMapping("delete-requests")
    public void deleteDraftBooks(@PathVariable UUID sessionId, @RequestBody DeleteDraftBookCommandDto deleteDraftBookCommandDto) throws CatalogingSessionNotFoundException {
        draftBookService.deleteDraftBooks(sessionId, deleteDraftBookCommandDto.getDraftBooksIds());
    }
}
