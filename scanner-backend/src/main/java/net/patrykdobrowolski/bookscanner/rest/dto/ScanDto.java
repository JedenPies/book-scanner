package net.patrykdobrowolski.bookscanner.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Jacksonized
@Builder @Getter
public class ScanDto {

    private final UUID id;
    private final String isbn;
}
