package net.patrykdobrowolski.bookscanner.adapter.fetcher.bn.mapper;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.bn.dto.BookDto;
import net.patrykdobrowolski.bookscanner.domain.model.Year;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Named
public class BnBookDtoMapper {

    public BookDetails fromDto(BookDto dto) {
        return BookDetails.builder()
                .title(cleanTitle(dto.getTitle()))
                .authors(AuthorsCleaner.cleanAndExtract(dto.getAuthor(), dto.getPublisher()))
                .publicationPlace(placeOfPublication(dto.getPlaceOfPublication()))
                .language(dto.getLanguage())
                .publisher(publisher(dto.getPublisher()))
                .publicationYear(Optional.ofNullable(dto.getPublicationYear()).map(Year::parse).orElse(null))
                .genres(genres(dto.getGenre()))
                .build();
    }

    private String cleanTitle(String title) {
        if (title == null) return null;
        title = title.replaceAll("\\s*/\\s*$", "").trim();
        if (title.contains(" / " )) title = title.substring(0, title.indexOf('/') - 1);
        return title;
    }

    private String placeOfPublication(String place) {
        if (place == null) return null;
        if (place.matches(".* : .*")) place = place.replace(" : ", ", ");
        if (place.matches(".* ; .*")) place = place.replace(" ; ", ", ");
        return place;
    }

    private String publisher(String publisher) {
        if (publisher == null) return null;
        publisher = publisher.trim();
        if (publisher.endsWith(",")) return publisher.substring(0, publisher.length() - 1);
        return publisher;
    }

    private List<String> genres(String genre) {
        return Optional.ofNullable(genre).map(g -> Collections.singletonList(g.trim())).orElseGet(Collections::emptyList);
    }
}
