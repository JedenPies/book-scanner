package net.patrykdobrowolski.bookscanner.db.mapper;

import net.patrykdobrowolski.bookscanner.db.entity.BookEntity;
import net.patrykdobrowolski.bookscanner.domain.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { BookDetailsEntityMapper.class, ISBNMapper.class })
public interface BookEntityMapper {

    Book fromEntity(BookEntity bookEntity);
    BookEntity toEntity(Book book);
}
