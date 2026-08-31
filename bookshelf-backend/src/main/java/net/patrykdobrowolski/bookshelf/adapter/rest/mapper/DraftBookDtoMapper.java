package net.patrykdobrowolski.bookshelf.adapter.rest.mapper;

import net.patrykdobrowolski.bookshelf.domain.model.value.ISBN;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.DraftBook;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.DraftBookDto;
import net.patrykdobrowolski.bookshelf.util.YearMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = YearMapper.class)
public interface DraftBookDtoMapper {

    DraftBookDto toDto(DraftBook draftBook);

    default String map(ISBN isbn) {
        return isbn.value();
    }
}
