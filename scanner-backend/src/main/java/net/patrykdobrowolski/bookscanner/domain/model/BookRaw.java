package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.Instant;

@Getter
@Builder @AllArgsConstructor
public class BookRaw {

    @With
    private String source;
    private FetchResult fetchResult;
    private String value;
    private Instant createdAt;
    private Instant modifiedAt;

    private BookRaw(String value, FetchResult fetchResult) {
        this.value = value;
        this.fetchResult = fetchResult;
    }

    public static BookRaw from(String rawResult) {
        return new BookRaw(rawResult, FetchResult.SUCCESS);
    }

    public static BookRaw failure() {
        return new BookRaw(null, FetchResult.FAILURE);
    }

    public static BookRaw notFound() {
        return new BookRaw(null, FetchResult.NOT_FOUND);
    }
}
