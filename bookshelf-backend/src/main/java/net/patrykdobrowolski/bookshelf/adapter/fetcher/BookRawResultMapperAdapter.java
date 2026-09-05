package net.patrykdobrowolski.bookshelf.adapter.fetcher;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookshelf.domain.model.fetch.ProviderFetchResult;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
public class BookRawResultMapperAdapter {

    private final Map<String, ProviderFetchResultMapper> mappersByKey;

    public BookRawResultMapperAdapter(List<ProviderFetchResultMapper> mappers) {
        this.mappersByKey = mappers.stream().collect(Collectors.toMap(ProviderFetchResultMapper::getKey, br -> br));
    }

    public BookDetails map(ProviderFetchResult providerFetchResult) {
        return Optional.ofNullable(mappersByKey.get(providerFetchResult.getSource()))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported source: " + providerFetchResult.getSource()))
                .map(providerFetchResult).withSource(providerFetchResult.getSource());
    }
}
