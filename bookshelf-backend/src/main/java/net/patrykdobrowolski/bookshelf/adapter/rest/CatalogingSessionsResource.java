package net.patrykdobrowolski.bookshelf.adapter.rest;

import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.CatalogingSessionDto;
import net.patrykdobrowolski.bookshelf.adapter.rest.mapper.CatalogingSessionDtoMapper;
import net.patrykdobrowolski.bookshelf.adapter.sse.SseCatalogingSessionService;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;
import net.patrykdobrowolski.bookshelf.service.CatalogingSessionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/cataloging-sessions")
@RequiredArgsConstructor
public class CatalogingSessionsResource {

    private final CatalogingSessionService sessionService;
    private final SseCatalogingSessionService sseCatalogingSessionService;
    private final CatalogingSessionDtoMapper catalogingSessionDtoMapper;

    @PostMapping
    public CatalogingSessionDto createSession() {
        CatalogingSession catalogingSession = sessionService.createSession();
        return catalogingSessionDtoMapper.toDto(catalogingSession);
    }

    @GetMapping("/{sessionId}/events-stream")
    public SseEmitter getEventsStream(@PathVariable UUID sessionId) throws CatalogingSessionNotFoundException {
        return sseCatalogingSessionService.createConnection(sessionId);
    }

}
