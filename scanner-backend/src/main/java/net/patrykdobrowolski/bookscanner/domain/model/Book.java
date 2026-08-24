package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.*;

@Builder
@Getter
public class Book {

    private final List<String> sourcePriority = List.of("bn", "open-library", "google");

    private UUID id;
    private ISBN isbn;
    @Builder.Default
    private List<BookRaw> bookRaws = new ArrayList<>();

    public BookRaw getPreferededBookRaw() {
        return bookRaws.stream()
                .min(Comparator.comparingInt(book -> {
                    int index = sourcePriority.indexOf(book.getSource());
                    return index == -1 ? Integer.MAX_VALUE : index;
                }))
                .orElse(null);

    }

    public void addRaws(Collection<? extends BookRaw> bookRaws) {
        this.bookRaws.addAll(bookRaws);
    }

    public static Book from(ISBN isbn) {
        return Book.builder().isbn(isbn).build();
    }
}
