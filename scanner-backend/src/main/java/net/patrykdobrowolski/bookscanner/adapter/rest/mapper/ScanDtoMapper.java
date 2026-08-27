package net.patrykdobrowolski.bookscanner.adapter.rest.mapper;

import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.adapter.rest.dto.ScanDto;
import net.patrykdobrowolski.bookscanner.util.YearMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = YearMapper.class)
public interface ScanDtoMapper {

    ScanDto toDto(Scan scan);

    default String map(ISBN isbn) {
        return isbn.value();
    }
}
