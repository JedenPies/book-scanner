package net.patrykdobrowolski.bookscanner.adapter.db.mapper;

import net.patrykdobrowolski.bookscanner.adapter.db.entity.BookEntity;
import net.patrykdobrowolski.bookscanner.domain.model.Book;
import net.patrykdobrowolski.bookscanner.util.ISBNMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { BookDetailsEntityMapper.class, ISBNMapper.class })
public interface BookEntityMapper {

    Book fromEntity(BookEntity bookEntity);
    BookEntity toEntity(Book book);
}
