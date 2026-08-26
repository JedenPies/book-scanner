package net.patrykdobrowolski.bookscanner.rest.mapper;

import net.patrykdobrowolski.bookscanner.domain.command.ExportSessionCommand;
import net.patrykdobrowolski.bookscanner.domain.model.Export;
import net.patrykdobrowolski.bookscanner.rest.dto.ExportDto;
import net.patrykdobrowolski.bookscanner.rest.dto.ExportRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExportDtoMapper {

    ExportSessionCommand map(ExportRequestDto dto);
    ExportDto map(Export export);
}
