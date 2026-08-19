package net.patrykdobrowolski.bookscanner.rest.mapper;

import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.rest.dto.ScanDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScanDtoMapper {

    ScanDto toDto(Scan scan);

    default String map(ISBN isbn) {
        return isbn.value();
    }
}
