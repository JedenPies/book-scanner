package net.patrykdobrowolski.bookshelf.util;


import net.patrykdobrowolski.bookshelf.domain.model.value.Year;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface YearMapper {

    default String map(Year year) {
        return Optional.ofNullable(year).map(Year::value).orElse(null);
    }

    default Year map(String year) {
        if (year == null || year.isBlank()) return null;
        return Year.parse(year);
    }
}
