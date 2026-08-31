package net.patrykdobrowolski.bookshelf.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookFetchResult {

    private final FetchResult fetchResult;
    private final String value;

    public static BookFetchResult notFound() {
        return BookFetchResult.builder().fetchResult(FetchResult.NOT_FOUND).build();
    }

    public static BookFetchResult failure() {
        return BookFetchResult.builder().fetchResult(FetchResult.FAILURE).build();
    }

    public static BookFetchResult success(String value) {
        return BookFetchResult.builder().fetchResult(FetchResult.SUCCESS).value(value).build();
    }
}
