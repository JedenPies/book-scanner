package net.patrykdobrowolski.bookshelf.adapter.sse.dto;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class DraftBookDto {

    private final String id;
    private final String isbn;
    private final String status;
    private final BookDetailsDto bookDetails;
}