package net.patrykdobrowolski.bookscanner.bn;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.bn.dto.BnResponseDto;
import net.patrykdobrowolski.bookscanner.bn.mapper.BnBookDtoMapper;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.BookRaw;
import net.patrykdobrowolski.bookscanner.fetcher.BookRawResultMapper;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Optional;

@Named
@RequiredArgsConstructor
public class BnBookRawResultMapper implements BookRawResultMapper {

    private final BnBookDtoMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public String getKey() {
        return BnBookFetchProvider.BIBLIOTEKA_NARODOWA_PROVIDER_KEY;
    }

    @Override
    public BookDetails map(BookRaw bookRaw) {
        BnResponseDto response = objectMapper.readValue(bookRaw.getValue(), BnResponseDto.class);
        return Optional.ofNullable(response.getBibs())
                .orElseGet(Collections::emptyList).stream()
                .findFirst().map(mapper::fromDto).orElse(null);
    }
}
