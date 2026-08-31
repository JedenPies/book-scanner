package net.patrykdobrowolski.bookscanner.adapter.rest.mapper;

import net.patrykdobrowolski.bookscanner.adapter.rest.dto.EditDraftBookCommandDto;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;
import net.patrykdobrowolski.bookscanner.util.YearMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { YearMapper.class })
public interface EditDraftBookCommandDtoMapper {

    BookDetails map(EditDraftBookCommandDto dto);
}
