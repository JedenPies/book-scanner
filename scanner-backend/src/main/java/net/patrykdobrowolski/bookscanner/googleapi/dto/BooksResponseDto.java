package net.patrykdobrowolski.bookscanner.googleapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder @Getter
public class BooksResponseDto {

    private final String kind;
    private final List<ItemDto> items;
}
