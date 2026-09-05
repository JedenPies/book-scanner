package net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.fetch.ProviderFetchResult;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.ProviderFetchResultMapper;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary.dto.BookDto;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary.mapper.OpenLibraryBookDtoMapper;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;
import tools.jackson.databind.ObjectMapper;

@Named
@RequiredArgsConstructor
public class OpenLibraryProviderFetchResultMapper implements ProviderFetchResultMapper {

    private final OpenLibraryBookDtoMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public String getKey() {
        return OpenLibraryBookFetchProvider.OPEN_LIBRARY_PROVIDER_KEY;
    }

    @Override
    public BookDetails map(ProviderFetchResult providerFetchResult) {
        BookDto bookDto = objectMapper.readValue(providerFetchResult.getValue(), BookDto.class);
        return mapper.fromDto(bookDto);
    }
}
