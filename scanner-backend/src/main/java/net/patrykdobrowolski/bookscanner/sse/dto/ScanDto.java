package net.patrykdobrowolski.bookscanner.sse.dto;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class ScanDto {

    private final String id;
    private final String isbn;
    private final String status;
    private final BookDetailsDto bookDetails;
}