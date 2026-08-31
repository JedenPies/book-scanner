package net.patrykdobrowolski.bookshelf.adapter.fetcher.googleapi;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.BookRaw;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.BookRawResultMapper;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.googleapi.dto.BooksResponseDto;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.googleapi.dto.ItemDto;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.googleapi.mapper.BooksResponseDtoMapper;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;
import tools.jackson.databind.ObjectMapper;

@Named
@RequiredArgsConstructor
public class GoogleBookRawResultMapper implements BookRawResultMapper {

    private final BooksResponseDtoMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public String getKey() {
        return GoogleBookFetchProvider.GOOGLE_PROVIDER_KEY;
    }

    @Override
    public BookDetails map(BookRaw bookRaw) {
        BooksResponseDto responseDto = objectMapper.readValue(bookRaw.getValue(), BooksResponseDto.class);
        return responseDto.getItems().stream()
                .findFirst()
                .map(ItemDto::getVolumeInfo)
                .map(mapper::fromDto).orElse(null);
    }
}
