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

    public void update(BookFetchResult result) {
        this.value = result.getValue();
        this.fetchResult = result.getFetchResult();
        this.modifiedAt = Instant.now();
    }

    public static BookRaw from(String rawResult) {
        return new BookRaw(rawResult, FetchResult.SUCCESS);
    }
}
