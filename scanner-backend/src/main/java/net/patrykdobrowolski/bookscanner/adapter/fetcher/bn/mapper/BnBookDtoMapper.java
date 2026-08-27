package net.patrykdobrowolski.bookscanner.adapter.fetcher.bn.mapper;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.bn.dto.BookDto;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;

import java.util.Collections;

@Named
public class BnBookDtoMapper {

    public BookDetails fromDto(BookDto dto) {
        return BookDetails.builder()
                .title(cleanTitle(dto.getTitle()))
                .authors(Collections.singletonList(dto.getAuthor()))
                .publicationPlace(dto.getPlaceOfPublication())
                .language(dto.getLanguage())
                .publisher(dto.getPublisher())
                .publicationYear(dto.getPublicationYear())
                .build();
    }

    private String cleanTitle(String title) {
        if (title == null) return null;
        return title.replaceAll("\\s*/\\s*$", "").trim();
    }
}
