package net.patrykdobrowolski.bookshelf.domain.model.fetch;

import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookshelf.domain.model.value.FetchResult;

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
