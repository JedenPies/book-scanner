package net.patrykdobrowolski.bookscanner.sse.mapper;

import net.patrykdobrowolski.bookscanner.domain.event.ScanCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.event.ScanUpdatedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.sse.dto.BookScanRequestedEventDto;
import net.patrykdobrowolski.bookscanner.sse.dto.ScanUpdatedEventDto;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EventsMapper {

    BookScanRequestedEventDto toDto(ScanCreatedEvent event);
    ScanUpdatedEventDto toDto(ScanUpdatedEvent event);

    default String map(UUID uuid) {
        return uuid.toString();
    }

    default String map(ISBN isbn) {
        return isbn.value();
    }
}
