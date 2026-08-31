package net.patrykdobrowolski.bookshelf.adapter.rest.mapper;

import net.patrykdobrowolski.bookshelf.adapter.rest.dto.EditDraftBookCommandDto;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;
import net.patrykdobrowolski.bookshelf.util.YearMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { YearMapper.class })
public interface EditDraftBookCommandDtoMapper {

    @Mapping(target = "sources", ignore = true)
    BookDetails map(EditDraftBookCommandDto dto);
}
