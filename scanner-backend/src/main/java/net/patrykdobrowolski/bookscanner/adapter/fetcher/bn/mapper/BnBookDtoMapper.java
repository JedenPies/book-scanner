package net.patrykdobrowolski.bookscanner.adapter.fetcher.bn.mapper;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.bn.dto.BookDto;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.Year;

import java.util.Collections;
import java.util.Optional;

@Named
public class BnBookDtoMapper {

    public BookDetails fromDto(BookDto dto) {
        return BookDetails.builder()
                .title(cleanTitle(dto.getTitle()))
                .authors(Collections.singletonList(dto.getAuthor()))
                .publicationPlace(placeOfPublication(dto.getPlaceOfPublication()))
                .language(dto.getLanguage())
                .publisher(publisher(dto.getPublisher()))
                .publicationYear(Optional.ofNullable(dto.getPublicationYear()).map(Year::parse).orElse(null))
                .build();
    }

    private String cleanTitle(String title) {
        if (title == null) return null;
        return title.replaceAll("\\s*/\\s*$", "").trim();
    }

    private String placeOfPublication(String place) {
        if (place == null) return null;
        if (place.matches(".* : .*")) return place.replace(" : ", ", ");
        return place;
    }

    private String publisher(String publisher) {
        if (publisher == null) return null;
        publisher = publisher.trim();
        if (publisher.endsWith(",")) return publisher.substring(0, publisher.length() - 1);
        return publisher;
    }
}
