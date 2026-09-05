package net.patrykdobrowolski.bookshelf.adapter.db.mapper;

import net.patrykdobrowolski.bookshelf.adapter.db.entity.BookFetchJobEntity;
import net.patrykdobrowolski.bookshelf.domain.model.fetch.BookFetchJob;
import net.patrykdobrowolski.bookshelf.util.ISBNMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { BookDetailsEntityMapper.class, ISBNMapper.class })
public interface BookEntityMapper {

    BookFetchJob fromEntity(BookFetchJobEntity bookFetchJobEntity);
    BookFetchJobEntity toEntity(BookFetchJob bookFetchJob);
}
