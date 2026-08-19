package net.patrykdobrowolski.bookscanner.googleapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder @Getter
public class VolumeInfoDto {

    private final String title;
    private final String subtitle;
    private final List<String> authors;
    private final String publishedDate;
    private final String language;

}
