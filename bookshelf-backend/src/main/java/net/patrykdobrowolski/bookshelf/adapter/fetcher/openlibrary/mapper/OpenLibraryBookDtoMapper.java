package net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary.mapper;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary.dto.PublishPlaceDto;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary.dto.PublisherDto;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary.dto.AuthorDto;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary.dto.BookDto;
import net.patrykdobrowolski.bookshelf.domain.model.value.Year;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Named
public class OpenLibraryBookDtoMapper {

    public BookDetails fromDto(BookDto dto) {
        return BookDetails.builder()
                .title(title(dto.getTitle()))
                .authors(fromAuthors(dto.getAuthors()))
                .publisher(Optional.ofNullable(dto.getPublishers()).orElseGet(Collections::emptyList).stream().findFirst().map(PublisherDto::getName).orElse(null))
                .publicationPlace(Optional.ofNullable(dto.getPublishPlaces()).orElseGet(Collections::emptyList).stream().findFirst().map(PublishPlaceDto::getName).orElse(null))
                .publicationYear(publicationYear(dto.getPublishDate()))
                .build();
    }

    private List<String> fromAuthors(List<AuthorDto> authors) {
        return Optional.ofNullable(authors).orElseGet(Collections::emptyList)
                .stream()
                .map(AuthorDto::getName)
                .toList();
    }

    private Year publicationYear(String strValue) {
        if (strValue != null && strValue.matches("[0-9]{4}$")) {
            Year.parse(strValue.substring(strValue.length() - 4));
        }
        return null;
    }

    private String title(String title) {
        return title.substring(0, 1).toUpperCase() + title.substring(1);
    }
}
