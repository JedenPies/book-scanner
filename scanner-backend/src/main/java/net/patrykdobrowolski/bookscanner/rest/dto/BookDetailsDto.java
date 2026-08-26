package net.patrykdobrowolski.bookscanner.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder @Getter
public class BookDetailsDto {

    private String source;
    private String title;
    private List<String> authors;
}
