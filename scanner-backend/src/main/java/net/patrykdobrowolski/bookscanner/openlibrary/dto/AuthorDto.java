package net.patrykdobrowolski.bookscanner.openlibrary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder @Getter
public class AuthorDto {

    private final String name;
}
