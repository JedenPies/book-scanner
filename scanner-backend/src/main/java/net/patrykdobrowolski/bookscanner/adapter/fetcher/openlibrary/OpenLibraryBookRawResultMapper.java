package net.patrykdobrowolski.bookscanner.adapter.fetcher.openlibrary;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.BookRaw;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.BookRawResultMapper;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.openlibrary.dto.BookDto;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.openlibrary.mapper.OpenLibraryBookDtoMapper;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;
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
