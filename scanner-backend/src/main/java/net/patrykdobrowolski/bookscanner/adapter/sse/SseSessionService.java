package net.patrykdobrowolski.bookscanner.adapter.sse;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.service.SessionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Named
@RequiredArgsConstructor
@Slf4j
public class SseSessionService {

    private final SessionService sessionService;

    private final Map<UUID, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter createConnection(UUID sessionId) throws SessionNotFoundException {
        log.debug("Creating SSE connection for session: {}", sessionId);
        sessionService.ensureSessionExists(sessionId);
        SseEmitter emitter = new SseEmitter(-1L);
        this.emitters.computeIfAbsent(sessionId, k -> Collections.synchronizedList(new ArrayList<>())).add(emitter);

        emitter.onCompletion(() -> removeEmitter(sessionId, emitter));
        emitter.onTimeout(() -> removeEmitter(sessionId, emitter));
        emitter.onError(e -> removeEmitter(sessionId, emitter));
        sendNotification(emitter, "INIT", "init");
        return emitter;
    }

    public void broadcastToSession(UUID sessionId, String eventName, Object payload) {
        List<SseEmitter> sseEmitters = emitters.get(sessionId);
        if (sseEmitters == null || sseEmitters.isEmpty()) return;
        synchronized (sseEmitters) {
            sseEmitters.forEach(emitter -> sendNotification(emitter, eventName, payload));
        }
    }

    @Scheduled(fixedRate = 30_000)
    public void broadcastHeartbeat() {
        log.debug("Sent heartbeat to {} connections", totalEmittersCount());
        emitters.entrySet().removeIf(e -> {
            List<SseEmitter> list = e.getValue();
            synchronized (list) {
                list.removeIf(emitter -> !sendNotification(emitter, "HEARTBEAT", "heartbeat"));
                return list.isEmpty();
            }
        });
        log.debug("Currently there are {} connections", totalEmittersCount());
    }

    private int totalEmittersCount() {
        return emitters.values().stream().mapToInt(List::size).sum();
    }

    private boolean sendNotification(SseEmitter emitter, String event, Object data) {
        try {
            emitter.send(SseEmitter.event().name(event).data(data));
            return true;
        } catch (IOException e) {
            emitter.complete();
            return false;
        }
    }

    private void removeEmitter(UUID boardId, SseEmitter emitter) {
        log.debug("Removing SSE connection for board: {}", boardId);
        List<SseEmitter> boardEmitters = emitters.get(boardId);
        if (boardEmitters != null) {
            synchronized (boardEmitters) {
                boardEmitters.remove(emitter);
                if (boardEmitters.isEmpty()) {
                    emitters.remove(boardId);
                }
            }
        }
    }
}
