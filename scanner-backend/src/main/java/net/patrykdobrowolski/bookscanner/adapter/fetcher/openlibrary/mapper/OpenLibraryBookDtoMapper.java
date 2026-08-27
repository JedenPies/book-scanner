package net.patrykdobrowolski.bookscanner.adapter.fetcher.openlibrary.mapper;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.openlibrary.dto.PublishPlaceDto;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.openlibrary.dto.PublisherDto;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.openlibrary.dto.AuthorDto;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.openlibrary.dto.BookDto;
import net.patrykdobrowolski.bookscanner.domain.model.Year;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Named
public class OpenLibraryBookDtoMapper {

    public BookDetails fromDto(BookDto dto) {
        return BookDetails.builder()
                .title(dto.getTitle())
                .authors(fromAuthors(dto.getAuthors()))
                .publisher(Optional.ofNullable(dto.getPublishers()).orElseGet(Collections::emptyList).stream().findFirst().map(PublisherDto::getName).orElse(null))
                .publicationPlace(Optional.ofNullable(dto.getPublishPlaces()).orElseGet(Collections::emptyList).stream().findFirst().map(PublishPlaceDto::getName).orElse(null))
                .publicationYear(Optional.ofNullable(dto.getPublishDate()).map(Year::parse).orElse(null))
                .build();
    }

    private List<String> fromAuthors(List<AuthorDto> authors) {
        return Optional.ofNullable(authors).orElseGet(Collections::emptyList)
                .stream()
                .map(AuthorDto::getName)
                .toList();
    }
}
