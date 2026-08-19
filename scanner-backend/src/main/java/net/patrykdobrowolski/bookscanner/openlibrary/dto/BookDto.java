package net.patrykdobrowolski.bookscanner.openlibrary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder @Getter
public class BookDto {

    private final String title;
    private final List<AuthorDto> authors;

}
