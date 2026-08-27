package net.patrykdobrowolski.bookscanner.adapter.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Set;

@Jacksonized
@Builder @Getter
public class BookDetailsDto {

    private Set<String> sources;
    private String title;
    private List<String> authors;
    private final String publisher;
    private final String publicationYear;
    private final String publicationPlace;
    private final String language;
}
