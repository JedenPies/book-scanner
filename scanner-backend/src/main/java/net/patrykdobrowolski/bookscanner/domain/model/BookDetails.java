package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.util.List;

@Builder
@Getter
public class BookDetails {

    @With
    private String source;
    private String title;
    private List<String> authors;
}
