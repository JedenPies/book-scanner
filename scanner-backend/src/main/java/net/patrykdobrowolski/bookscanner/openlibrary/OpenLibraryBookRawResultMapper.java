package net.patrykdobrowolski.bookscanner.openlibrary;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.BookRaw;
import net.patrykdobrowolski.bookscanner.fetcher.BookRawResultMapper;
import net.patrykdobrowolski.bookscanner.openlibrary.dto.BookDto;
import net.patrykdobrowolski.bookscanner.openlibrary.mapper.OpenLibraryBookDtoMapper;
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
