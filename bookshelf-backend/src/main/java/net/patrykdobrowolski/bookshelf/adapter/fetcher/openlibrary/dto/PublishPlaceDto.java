package net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder @Getter
public class PublishPlaceDto {

    private final String name;
}
