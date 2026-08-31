package net.patrykdobrowolski.bookshelf.adapter.db.mapper;

import net.patrykdobrowolski.bookshelf.adapter.db.entity.BookEntity;
import net.patrykdobrowolski.bookshelf.domain.model.Book;
import net.patrykdobrowolski.bookshelf.util.ISBNMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { BookDetailsEntityMapper.class, ISBNMapper.class })
public interface BookEntityMapper {

    Book fromEntity(BookEntity bookEntity);
    BookEntity toEntity(Book book);
}
