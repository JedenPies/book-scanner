package net.patrykdobrowolski.bookscanner.util;

import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
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
