package net.patrykdobrowolski.bookscanner.sse;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.event.ScanCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.event.ScanUpdatedEvent;
import net.patrykdobrowolski.bookscanner.sse.mapper.EventsMapper;
import org.springframework.context.event.EventListener;

@Named
@RequiredArgsConstructor
@Slf4j
public class SseEventNotifier {

    private final SseSessionService sseSessionService;
    private final EventsMapper eventsMapper;

    @EventListener
    public void handleScanRequestedEvent(ScanCreatedEvent event) {
        log.debug("Sending SCAN_REQUESTED event to all connected clients");
        sseSessionService.broadcastToSession(event.getSessionId(), "SCAN_REQUESTED", eventsMapper.toDto(event));
    }

    @EventListener
    public void handleScanUpdatedEvent(ScanUpdatedEvent event) {
        log.debug("Sending SCAN_UPDATED event");
        sseSessionService.broadcastToSession(event.getScan().getSessionId(), "SCAN_UPDATED", eventsMapper.toDto(event));

    }
}
