package net.patrykdobrowolski.bookscanner.googleapi;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.BookRaw;
import net.patrykdobrowolski.bookscanner.fetcher.BookRawResultMapper;
import net.patrykdobrowolski.bookscanner.googleapi.dto.BooksResponseDto;
import net.patrykdobrowolski.bookscanner.googleapi.dto.ItemDto;
import net.patrykdobrowolski.bookscanner.googleapi.mapper.BooksResponseDtoMapper;
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
