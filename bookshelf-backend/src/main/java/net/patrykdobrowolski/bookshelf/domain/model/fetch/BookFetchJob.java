package net.patrykdobrowolski.bookshelf.domain.model.fetch;

import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookshelf.domain.model.value.FetchResult;
import net.patrykdobrowolski.bookshelf.domain.model.value.ISBN;

import java.util.*;

@Builder
@Getter
public class BookFetchJob {

    private final List<FetchResult> resultsPriority = List.of(FetchResult.SUCCESS, FetchResult.FAILURE, FetchResult.NOT_FOUND);

    private UUID id;
    private ISBN isbn;
    @Builder.Default
    private List<ProviderFetchResult> providerFetchResults = new ArrayList<>();

    public FetchResult getFetchResult() {
        return providerFetchResults.stream().map(ProviderFetchResult::getFetchResult)
                .min(Comparator.comparingInt(book -> {
                    int index = resultsPriority.indexOf(book);
                    return index == -1 ? Integer.MAX_VALUE : index;
                }))
                .orElse(FetchResult.NOT_FOUND);
    }

    public void addEmptyRaw(String adapterKey) {
        ProviderFetchResult providerFetchResult = ProviderFetchResult.builder().source(adapterKey).fetchResult(FetchResult.INIT).build();
        this.providerFetchResults.add(providerFetchResult);
    }

    public List<ProviderFetchResult> getNewOrFailedRaws() {
        return providerFetchResults.stream().filter(
                br -> br.getFetchResult() == FetchResult.INIT || br.getFetchResult() == FetchResult.FAILURE).toList();
    }

    public static BookFetchJob from(ISBN isbn) {
        return BookFetchJob.builder().isbn(isbn).build();
    }
}
