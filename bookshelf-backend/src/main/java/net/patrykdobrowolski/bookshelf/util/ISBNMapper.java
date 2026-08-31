package net.patrykdobrowolski.bookshelf.util;

import net.patrykdobrowolski.bookshelf.domain.model.ISBN;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ISBNMapper {

    default String map(ISBN isbn) {
        return isbn.value();
    }
    default ISBN map(String isbn) {
        return new ISBN(isbn);
    }
}
