package net.patrykdobrowolski.bookscanner.sse.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BookDetailsDto {

    private String source;
    private String title;
    private String subtitle;
    private List<String> authors;
}
