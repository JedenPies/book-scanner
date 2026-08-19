package net.patrykdobrowolski.bookscanner.openlibrary.mapper;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.openlibrary.dto.AuthorDto;
import net.patrykdobrowolski.bookscanner.openlibrary.dto.BookDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Named
public class OpenLibraryBookDtoMapper {

    public BookDetails fromDto(BookDto dto) {
        return BookDetails.builder()
                .title(dto.getTitle())
                .authors(fromAuthors(dto.getAuthors()))
                .build();
    }

    private List<String> fromAuthors(List<AuthorDto> authors) {
        return Optional.ofNullable(authors).orElseGet(Collections::emptyList)
                .stream()
                .map(AuthorDto::getName)
                .toList();
    }
}
