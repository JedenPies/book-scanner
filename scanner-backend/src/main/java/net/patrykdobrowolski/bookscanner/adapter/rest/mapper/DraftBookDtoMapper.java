package net.patrykdobrowolski.bookscanner.adapter.rest.mapper;

import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.DraftBook;
import net.patrykdobrowolski.bookscanner.adapter.rest.dto.DraftBookDto;
import net.patrykdobrowolski.bookscanner.util.YearMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = YearMapper.class)
public interface DraftBookDtoMapper {

    DraftBookDto toDto(DraftBook draftBook);

    default String map(ISBN isbn) {
        return isbn.value();
    }
}
