package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class Book {

    private UUID id;
    private ISBN isbn;
    @Builder.Default
    private List<BookDetails> bookDetails = new ArrayList<>();
    private Instant createdAt;
    private Instant modifiedAt;

    public void addDetails(BookDetails bookDetails) {
        if (!existsBySource(bookDetails.getSource())) {
            this.bookDetails.add(bookDetails);
        }
    }

    private boolean existsBySource(String source) {
        return this.bookDetails.stream().anyMatch(bd -> bd.getSource().equals(source));
    }

    public static Book from(ISBN isbn) {
        return Book.builder().isbn(isbn).build();
    }
}
