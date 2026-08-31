package net.patrykdobrowolski.bookscanner.adapter.sse.mapper;

import net.patrykdobrowolski.bookscanner.adapter.sse.dto.DraftBooksDeletedSseEvent;
import net.patrykdobrowolski.bookscanner.adapter.sse.dto.ExportCompleteSseEvent;
import net.patrykdobrowolski.bookscanner.adapter.sse.dto.DraftBookCreatedSseEvent;
import net.patrykdobrowolski.bookscanner.adapter.sse.dto.DraftBookUpdatedSseEvent;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.event.DraftBookCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.DraftBooksDeletedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ExportCompleteEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.DraftBookUpdatedEvent;
import net.patrykdobrowolski.bookscanner.util.YearMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = YearMapper.class)
public interface EventsMapper {

    DraftBookUpdatedSseEvent toSseEvent(DraftBookUpdatedEvent event);
    DraftBookCreatedSseEvent toSseEvent(DraftBookCreatedEvent event);
    @Mapping(target = "count", expression = "java(event.getScans() != null ? event.getScans().size() : 0)")
    DraftBooksDeletedSseEvent toSseEvent(DraftBooksDeletedEvent event);
    ExportCompleteSseEvent toSseEvent(ExportCompleteEvent event);

    default String map(UUID uuid) {
        return uuid.toString();
    }
    default String map(ISBN isbn) {
        return isbn.value();
    }
}
