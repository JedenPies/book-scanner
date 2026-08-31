package net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder @Getter
public class BookDto {

    private final String title;
    private final List<AuthorDto> authors;
    private final List<PublisherDto> publishers;
    @JsonProperty("publish_places")
    private final List<PublishPlaceDto> publishPlaces;
    @JsonProperty("publish_date")
    private final String publishDate;


}
