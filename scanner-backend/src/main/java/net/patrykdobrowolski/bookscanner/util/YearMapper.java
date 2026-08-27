package net.patrykdobrowolski.bookscanner.util;


import net.patrykdobrowolski.bookscanner.domain.model.Year;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YearMapper {

    default String map(Year isbn) {
            return isbn.value();
        }
    default Year map(String isbn) {
            return Year.parse(isbn);
        }
}
