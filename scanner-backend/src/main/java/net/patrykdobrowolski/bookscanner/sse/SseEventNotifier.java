package net.patrykdobrowolski.bookscanner.sse;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.event.ScanCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.event.ScanDeletedEvent;
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
        log.debug("Sending SCAN_CREATED event to all connected clients");
        sseSessionService.broadcastToSession(event.getSession().getId(), "SCAN_CREATED", eventsMapper.toSseEvent(event));
    }

    @EventListener
    public void handleScanUpdatedEvent(ScanUpdatedEvent event) {
        log.debug("Sending SCAN_UPDATED event");
        sseSessionService.broadcastToSession(event.getSession().getId(), "SCAN_UPDATED", eventsMapper.toSseEvent(event));
    }

    @EventListener
    public void handleScanDeletedEvent(ScanDeletedEvent event) {
        log.debug("Sending SCAN_DELETED event");
        sseSessionService.broadcastToSession(event.getSession().getId(), "SCAN_DELETED", eventsMapper.toSseEvent(event));
    }
}
