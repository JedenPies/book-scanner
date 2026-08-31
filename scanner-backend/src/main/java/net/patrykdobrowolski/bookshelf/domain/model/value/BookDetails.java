package net.patrykdobrowolski.bookshelf.domain.model.value;

import lombok.Builder;
import net.patrykdobrowolski.bookshelf.domain.model.Year;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
public record BookDetails(
        String title,
        List<String> authors,
        String publisher,
        String publicationPlace,
        Year publicationYear,
        String language,
        List<String> genres,
        Set<String> sources
) {
    public BookDetails {
        authors = authors != null ? List.copyOf(authors) : Collections.emptyList();
        genres = genres != null ? List.copyOf(genres) : Collections.emptyList();
        sources = sources != null ? Set.copyOf(sources) : Collections.emptySet();
    }

    public BookDetails withSource(String newSource) {
        HashSet<String> newSources = new HashSet<>(this.sources);
        newSources.add(newSource);
        return this.withSources(newSources);
    }

    public BookDetails withSources(Set<String> sources) {
        return new BookDetails(
                this.title, this.authors, this.publisher, this.publicationPlace,
                this.publicationYear, this.language, this.genres, sources);
    }
}
