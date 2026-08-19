package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class Book {

    private UUID id;
    private ISBN isbn;
    private List<BookDetails> bookDetails;
    private Instant createdAt;
}
