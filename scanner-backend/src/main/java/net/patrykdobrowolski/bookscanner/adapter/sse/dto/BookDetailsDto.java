package net.patrykdobrowolski.bookscanner.adapter.sse.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@Builder
public class BookDetailsDto {

    private final Set<String> sources;
    private final String title;
    private final List<String> authors;
    private final String publisher;
    private final String publicationYear;
    private final String publicationPlace;
    private final String language;
    private final List<String> genres;
}
