package net.patrykdobrowolski.bookshelf.adapter.rest.mapper;

import net.patrykdobrowolski.bookshelf.domain.model.command.ExportSessionCommand;
import net.patrykdobrowolski.bookshelf.domain.model.Export;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.ExportDto;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.ExportRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExportDtoMapper {

    ExportSessionCommand map(ExportRequestDto dto);
    ExportDto map(Export export);
}
