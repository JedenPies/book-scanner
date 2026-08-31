package net.patrykdobrowolski.bookscanner.adapter.rest.mapper;

import net.patrykdobrowolski.bookscanner.adapter.rest.dto.EditScanCommandDto;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;
import net.patrykdobrowolski.bookscanner.util.YearMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { YearMapper.class })
public interface EditScanCommandDtoMapper {

    BookDetails map(EditScanCommandDto dto);
}
