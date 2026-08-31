package net.patrykdobrowolski.bookscanner.adapter.db.mapper;

import net.patrykdobrowolski.bookscanner.adapter.db.entity.BookDetailsEntity;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;
import net.patrykdobrowolski.bookscanner.util.ISBNMapper;
import net.patrykdobrowolski.bookscanner.util.YearMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ISBNMapper.class, YearMapper.class })
public interface BookDetailsEntityMapper {

    BookDetailsEntity toEntity(BookDetails bookDetails);
    BookDetails fromEntity(BookDetailsEntity bookDetailsEntity);
}
