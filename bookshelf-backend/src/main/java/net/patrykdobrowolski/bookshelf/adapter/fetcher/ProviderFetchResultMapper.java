package net.patrykdobrowolski.bookshelf.adapter.fetcher;

import net.patrykdobrowolski.bookshelf.domain.model.fetch.ProviderFetchResult;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;

public interface ProviderFetchResultMapper {

    String getKey();
    BookDetails map(ProviderFetchResult providerFetchResult);
}
