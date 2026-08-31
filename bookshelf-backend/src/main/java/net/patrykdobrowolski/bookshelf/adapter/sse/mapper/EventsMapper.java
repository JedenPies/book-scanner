package net.patrykdobrowolski.bookshelf.adapter.sse.mapper;

import net.patrykdobrowolski.bookshelf.adapter.sse.dto.DraftBooksDeletedSseEvent;
import net.patrykdobrowolski.bookshelf.adapter.sse.dto.ExportCompleteSseEvent;
import net.patrykdobrowolski.bookshelf.adapter.sse.dto.DraftBookCreatedSseEvent;
import net.patrykdobrowolski.bookshelf.adapter.sse.dto.DraftBookUpdatedSseEvent;
import net.patrykdobrowolski.bookshelf.domain.model.value.ISBN;
import net.patrykdobrowolski.bookshelf.domain.model.event.DraftBookCreatedEvent;
import net.patrykdobrowolski.bookshelf.domain.model.event.DraftBooksDeletedEvent;
import net.patrykdobrowolski.bookshelf.domain.model.event.ExportCompleteEvent;
import net.patrykdobrowolski.bookshelf.domain.model.event.DraftBookUpdatedEvent;
import net.patrykdobrowolski.bookshelf.util.YearMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = YearMapper.class)
public interface EventsMapper {

    DraftBookUpdatedSseEvent toSseEvent(DraftBookUpdatedEvent event);
    DraftBookCreatedSseEvent toSseEvent(DraftBookCreatedEvent event);
    @Mapping(target = "count", expression = "java(event.getDraftBooks() != null ? event.getDraftBooks().size() : 0)")
    DraftBooksDeletedSseEvent toSseEvent(DraftBooksDeletedEvent event);
    ExportCompleteSseEvent toSseEvent(ExportCompleteEvent event);

    default String map(UUID uuid) {
        return uuid.toString();
    }
    default String map(ISBN isbn) {
        return isbn.value();
    }
}
