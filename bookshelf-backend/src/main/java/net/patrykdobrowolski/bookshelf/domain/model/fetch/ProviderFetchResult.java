package net.patrykdobrowolski.bookshelf.domain.model.fetch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import net.patrykdobrowolski.bookshelf.domain.model.value.FetchResult;

import java.time.Instant;

@Getter
@Builder @AllArgsConstructor
public class ProviderFetchResult {

    @With
    private String source;
    private FetchResult fetchResult;
    private String value;
    private Instant createdAt;
    private Instant modifiedAt;

    private ProviderFetchResult(String value, FetchResult fetchResult) {
        this.value = value;
        this.fetchResult = fetchResult;
    }

    public void update(BookFetchResult result) {
        this.value = result.getValue();
        this.fetchResult = result.getFetchResult();
        this.modifiedAt = Instant.now();
    }

    public static ProviderFetchResult from(String rawResult) {
        return new ProviderFetchResult(rawResult, FetchResult.SUCCESS);
    }
}
