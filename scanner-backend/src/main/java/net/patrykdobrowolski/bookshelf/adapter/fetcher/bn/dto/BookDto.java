package net.patrykdobrowolski.bookshelf.adapter.fetcher.bn.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder @Getter
public class BookDto {

    private final String title;
    private final String author;
    private final String publisher;
    private final String publicationYear;
    private final String language;
    private final String placeOfPublication;
    private final String genre;
}
