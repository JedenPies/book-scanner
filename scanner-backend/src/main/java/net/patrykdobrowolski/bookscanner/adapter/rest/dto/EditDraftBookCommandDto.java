package net.patrykdobrowolski.bookscanner.adapter.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder @Getter
public class EditDraftBookCommandDto {

    private final String title;
    private final List<String> authors;
    private final String publisher;
    private final String publicationYear;
    private final String publicationPlace;
    private final String language;
    private final List<String> genres;
}
