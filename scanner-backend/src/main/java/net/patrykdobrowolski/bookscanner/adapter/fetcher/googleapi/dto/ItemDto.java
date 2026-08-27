package net.patrykdobrowolski.bookscanner.adapter.fetcher.googleapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder @Getter
public class ItemDto {

    private final String id;
    private final VolumeInfoDto volumeInfo;
}
