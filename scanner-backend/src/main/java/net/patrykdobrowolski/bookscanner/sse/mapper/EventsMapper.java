package net.patrykdobrowolski.bookscanner.sse.mapper;

import net.patrykdobrowolski.bookscanner.domain.event.*;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.sse.dto.*;
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
