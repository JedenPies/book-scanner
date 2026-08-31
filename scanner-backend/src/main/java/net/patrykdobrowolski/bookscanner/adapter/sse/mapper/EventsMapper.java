package net.patrykdobrowolski.bookscanner.adapter.sse.mapper;

import net.patrykdobrowolski.bookscanner.adapter.sse.dto.ExportCompleteSseEvent;
import net.patrykdobrowolski.bookscanner.adapter.sse.dto.ScanCreatedSseEvent;
import net.patrykdobrowolski.bookscanner.adapter.sse.dto.ScanUpdatedSseEvent;
import net.patrykdobrowolski.bookscanner.adapter.sse.dto.ScansDeletedSseEvent;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.event.ExportCompleteEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScanCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScanUpdatedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScansDeletedEvent;
import net.patrykdobrowolski.bookscanner.util.YearMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = YearMapper.class)
public interface EventsMapper {

    ScanUpdatedSseEvent toSseEvent(ScanUpdatedEvent event);
    ScanCreatedSseEvent toSseEvent(ScanCreatedEvent event);
    @Mapping(target = "count", expression = "java(event.getScans() != null ? event.getScans().size() : 0)")
    ScansDeletedSseEvent toSseEvent(ScansDeletedEvent event);
    ExportCompleteSseEvent toSseEvent(ExportCompleteEvent event);

    default String map(UUID uuid) {
        return uuid.toString();
    }
    default String map(ISBN isbn) {
        return isbn.value();
    }
}
