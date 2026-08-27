package net.patrykdobrowolski.bookscanner.util;


import net.patrykdobrowolski.bookscanner.domain.model.Year;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface YearMapper {

    default String map(Year isbn) {
        return Optional.ofNullable(isbn).map(Year::value).orElse(null);
    }
    default Year map(String isbn) {
            return Year.parse(isbn);
        }
}
