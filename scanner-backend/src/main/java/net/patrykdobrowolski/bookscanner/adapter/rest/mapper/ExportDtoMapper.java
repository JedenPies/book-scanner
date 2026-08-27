package net.patrykdobrowolski.bookscanner.adapter.rest.mapper;

import net.patrykdobrowolski.bookscanner.domain.model.command.ExportSessionCommand;
import net.patrykdobrowolski.bookscanner.domain.model.Export;
import net.patrykdobrowolski.bookscanner.adapter.rest.dto.ExportDto;
import net.patrykdobrowolski.bookscanner.adapter.rest.dto.ExportRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExportDtoMapper {

    ExportSessionCommand map(ExportRequestDto dto);
    ExportDto map(Export export);
}
