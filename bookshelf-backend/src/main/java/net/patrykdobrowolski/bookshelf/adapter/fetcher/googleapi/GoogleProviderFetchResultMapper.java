package net.patrykdobrowolski.bookshelf.adapter.fetcher.googleapi;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.fetch.ProviderFetchResult;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.ProviderFetchResultMapper;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.googleapi.dto.BooksResponseDto;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.googleapi.dto.ItemDto;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.googleapi.mapper.BooksResponseDtoMapper;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;
import tools.jackson.databind.ObjectMapper;

@Named
@RequiredArgsConstructor
public class GoogleProviderFetchResultMapper implements ProviderFetchResultMapper {

    private final BooksResponseDtoMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public String getKey() {
        return GoogleBookFetchProvider.GOOGLE_PROVIDER_KEY;
    }

    @Override
    public BookDetails map(ProviderFetchResult providerFetchResult) {
        BooksResponseDto responseDto = objectMapper.readValue(providerFetchResult.getValue(), BooksResponseDto.class);
        return responseDto.getItems().stream()
                .findFirst()
                .map(ItemDto::getVolumeInfo)
                .map(mapper::fromDto).orElse(null);
    }
}
