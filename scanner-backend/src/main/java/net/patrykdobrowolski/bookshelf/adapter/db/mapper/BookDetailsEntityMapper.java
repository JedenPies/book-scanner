package net.patrykdobrowolski.bookshelf.adapter.db.mapper;

import net.patrykdobrowolski.bookshelf.adapter.db.entity.BookDetailsEntity;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;
import net.patrykdobrowolski.bookshelf.util.ISBNMapper;
import net.patrykdobrowolski.bookshelf.util.YearMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ISBNMapper.class, YearMapper.class })
public interface BookDetailsEntityMapper {

    BookDetailsEntity toEntity(BookDetails bookDetails);
    BookDetails fromEntity(BookDetailsEntity bookDetailsEntity);
}
