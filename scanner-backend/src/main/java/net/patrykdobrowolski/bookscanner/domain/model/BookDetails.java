package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.util.List;
import java.util.Set;

@Builder
@Getter
public class BookDetails {

    @With(AccessLevel.PRIVATE)
    private Set<String> sources;
    private String title;
    private List<String> authors;
    private String publisher;
    private Year publicationYear;
    private String publicationPlace;
    private String language;

    public BookDetails withSource(String source) {
        return this.withSources(Set.of(source));
    }
}
