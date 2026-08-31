package net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.BookRaw;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.BookRawResultMapper;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary.dto.BookDto;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.openlibrary.mapper.OpenLibraryBookDtoMapper;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;
import tools.jackson.databind.ObjectMapper;

@Named
@RequiredArgsConstructor
public class OpenLibraryBookRawResultMapper implements BookRawResultMapper {

    private final OpenLibraryBookDtoMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public String getKey() {
        return OpenLibraryBookFetchProvider.OPEN_LIBRARY_PROVIDER_KEY;
    }

    @Override
    public BookDetails map(BookRaw bookRaw) {
        BookDto bookDto = objectMapper.readValue(bookRaw.getValue(), BookDto.class);
        return mapper.fromDto(bookDto);
    }
}
