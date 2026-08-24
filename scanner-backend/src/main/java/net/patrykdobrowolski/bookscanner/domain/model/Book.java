package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.*;

@Builder
@Getter
public class Book {

    private final List<String> sourcePriority = List.of("bn", "open-library", "google");
    private final List<FetchResult> resultsPriority = List.of(FetchResult.SUCCESS, FetchResult.FAILURE, FetchResult.NOT_FOUND);

    private UUID id;
    private ISBN isbn;
    @Builder.Default
    private List<BookRaw> bookRaws = new ArrayList<>();

    public BookRaw getPreferededBookRaw() {
        return bookRaws.stream()
                .filter(br -> br.getFetchResult() == FetchResult.SUCCESS)
                .min(Comparator.comparingInt(book -> {
                    int index = sourcePriority.indexOf(book.getSource());
                    return index == -1 ? Integer.MAX_VALUE : index;
                }))
                .orElse(null);

    }

    public FetchResult getFetchResult() {
        return bookRaws.stream().map(BookRaw::getFetchResult)
                .min(Comparator.comparingInt(book -> {
                    int index = resultsPriority.indexOf(book);
                    return index == -1 ? Integer.MAX_VALUE : index;
                }))
                .orElse(FetchResult.NOT_FOUND);
    }

    public void addEmptyRaw(String adapterKey) {
        BookRaw bookRaw = BookRaw.builder().source(adapterKey).fetchResult(FetchResult.INIT).build();
        this.bookRaws.add(bookRaw);
    }

    public List<BookRaw> getNewOrFailedRaws() {
        return bookRaws.stream().filter(
                br -> br.getFetchResult() == FetchResult.INIT || br.getFetchResult() == FetchResult.FAILURE).toList();
    }

    public static Book from(ISBN isbn) {
        return Book.builder().isbn(isbn).build();
    }
}
