package net.patrykdobrowolski.bookscanner.adapter.sse.mapper;

import net.patrykdobrowolski.bookscanner.adapter.sse.dto.ExportCompleteSseEvent;
import net.patrykdobrowolski.bookscanner.adapter.sse.dto.ScanCreatedSseEvent;
import net.patrykdobrowolski.bookscanner.adapter.sse.dto.ScanDeletedSseEvent;
import net.patrykdobrowolski.bookscanner.adapter.sse.dto.ScanUpdatedSseEvent;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.event.ExportCompleteEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScanCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScanDeletedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScanUpdatedEvent;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EventsMapper {

    ScanUpdatedSseEvent toSseEvent(ScanUpdatedEvent event);
    ScanCreatedSseEvent toSseEvent(ScanCreatedEvent event);
    ScanDeletedSseEvent toSseEvent(ScanDeletedEvent event);
    ExportCompleteSseEvent toSseEvent(ExportCompleteEvent event);

    default String map(UUID uuid) {
        return uuid.toString();
    }
    default String map(ISBN isbn) {
        return isbn.value();
    }
}
