package net.patrykdobrowolski.bookscanner.adapter.sse.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BookDetailsDto {

    private String source;
    private String title;
    private List<String> authors;
}
