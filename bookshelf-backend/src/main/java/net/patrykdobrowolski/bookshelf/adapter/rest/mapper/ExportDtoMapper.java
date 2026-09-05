package net.patrykdobrowolski.bookshelf.adapter.rest.mapper;

import net.patrykdobrowolski.bookshelf.domain.model.command.ExportCommand;
import net.patrykdobrowolski.bookshelf.domain.model.export.Export;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.ExportDto;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.ExportRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExportDtoMapper {

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "correlationKey", ignore = true)
    ExportCommand map(ExportRequestDto dto);
    ExportDto map(Export export);
}
