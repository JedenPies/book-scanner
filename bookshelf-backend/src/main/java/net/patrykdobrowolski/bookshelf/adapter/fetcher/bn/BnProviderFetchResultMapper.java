package net.patrykdobrowolski.bookshelf.adapter.fetcher.bn;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.bn.dto.BnResponseDto;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.bn.mapper.BnBookDtoMapper;
import net.patrykdobrowolski.bookshelf.domain.model.fetch.ProviderFetchResult;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.ProviderFetchResultMapper;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Optional;

@Named
@RequiredArgsConstructor
public class BnProviderFetchResultMapper implements ProviderFetchResultMapper {

    private final BnBookDtoMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public String getKey() {
        return BnBookFetchProvider.BIBLIOTEKA_NARODOWA_PROVIDER_KEY;
    }

    @Override
    public BookDetails map(ProviderFetchResult providerFetchResult) {
        BnResponseDto response = objectMapper.readValue(providerFetchResult.getValue(), BnResponseDto.class);
        return Optional.ofNullable(response.getBibs())
                .orElseGet(Collections::emptyList).stream()
                .findFirst().map(mapper::fromDto).orElse(null);
    }
}
