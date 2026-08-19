package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.*;

import java.util.List;
import java.util.Objects;

@Builder
@Getter
public class BookDetails {

    @With
    private String source;
    private String title;
    private String subtitle;
    private List<String> authors;

    public boolean isLocal() {
        return Objects.equals(source, "local");
    }
}
