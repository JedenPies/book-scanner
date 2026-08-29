package net.patrykdobrowolski.bookscanner.adapter.rest.mapper;

import net.patrykdobrowolski.bookscanner.adapter.rest.dto.EditScanCommandDto;
import net.patrykdobrowolski.bookscanner.domain.model.command.UpdateScanCommand;
import net.patrykdobrowolski.bookscanner.util.YearMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { YearMapper.class })
public interface EditScanCommandDtoMapper {

    UpdateScanCommand map(EditScanCommandDto dto);
}
