package net.patrykdobrowolski.bookscanner.rest;

import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.rest.dto.ExportDto;
import net.patrykdobrowolski.bookscanner.rest.dto.ExportRequestDto;
import net.patrykdobrowolski.bookscanner.rest.dto.SessionDto;
import net.patrykdobrowolski.bookscanner.rest.mapper.SessionDtoMapper;
import net.patrykdobrowolski.bookscanner.service.SessionService;
import net.patrykdobrowolski.bookscanner.sse.SseSessionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionsResource {

    private final SessionService sessionService;
    private final SseSessionService sseSessionService;
    private final SessionDtoMapper sessionDtoMapper;

    @PostMapping
    public SessionDto createSession() {
        Session session = sessionService.createSession();
        return sessionDtoMapper.toDto(session);
    }

    @GetMapping("/{sessionId}/events-stream")
    public SseEmitter getEventsStream(@PathVariable UUID sessionId) throws SessionNotFoundException {
        return sseSessionService.createConnection(sessionId);
    }

    @PostMapping("/{sessionId}/export-requests")
    public ExportDto createNewExportRequest(
            @PathVariable UUID sessionId, @RequestBody ExportRequestDto exportDto) {
        return ExportDto.builder().id(UUID.randomUUID()).build();
    }

    @GetMapping("/{sessionId}/export-requests/{requestId}")
    public ExportDto getExportRequest(@PathVariable UUID sessionId, @PathVariable UUID requestId) {
        return ExportDto.builder().id(requestId).build();
    }

}
