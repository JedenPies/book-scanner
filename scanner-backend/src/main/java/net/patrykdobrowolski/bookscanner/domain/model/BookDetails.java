package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import net.patrykdobrowolski.bookscanner.domain.model.command.UpdateScanCommand;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    private List<String> genres;

    public BookDetails withSource(String source) {
        return this.withSources(Set.of(source));
    }

    static BookDetails empty() {
        return BookDetails.builder().build();
    }

    static BookDetails copyOf(BookDetails oldDetails) {
        return BookDetails.builder()
                .title(oldDetails.getTitle())
                .authors(oldDetails.getAuthors())
                .publisher(oldDetails.getPublisher())
                .publicationYear(oldDetails.getPublicationYear())
                .publicationPlace(oldDetails.getPublicationPlace())
                .language(oldDetails.getLanguage())
                .genres(oldDetails.getGenres())
                .build();
    }

    void update(UpdateScanCommand command) {
        this.authors = Optional.ofNullable(command.getAuthors()).orElseGet(Collections::emptyList).stream().toList();
        this.title = command.getTitle();
        this.publisher = command.getPublisher();
        this.publicationYear = command.getPublicationYear();
        this.publicationPlace = command.getPublicationPlace();
        this.language = command.getLanguage();
        this.genres = Optional.ofNullable(command.getGenres()).orElseGet(Collections::emptyList).stream().toList();
    }
}
