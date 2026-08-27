package net.patrykdobrowolski.bookscanner.adapter.fetcher.openlibrary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder @Getter
public class PublisherDto {

    private final String name;
}
