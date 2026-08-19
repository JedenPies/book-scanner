package net.patrykdobrowolski.bookscanner.bn.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder @Getter
public class BookDto {

    private final String title;
    private final String author;
}
