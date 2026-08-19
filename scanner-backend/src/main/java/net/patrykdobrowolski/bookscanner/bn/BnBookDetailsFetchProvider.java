package net.patrykdobrowolski.bookscanner.bn;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.bn.dto.BnResponseDto;
import net.patrykdobrowolski.bookscanner.bn.mapper.BnBookDtoMapper;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.fetcher.BookDetailsFetchProvider;
import org.springframework.core.annotation.Order;

import java.util.Collections;
import java.util.Optional;

@Named
@RequiredArgsConstructor
@Order(40)
public class BnBookDetailsFetchProvider implements BookDetailsFetchProvider {

    private final BnFeignClient client;
    private final BnBookDtoMapper mapper;

    @Override
    public String getKey() {
        return "biblioteka-narodowa";
    }

    @Override
    public Optional<BookDetails> fetchBookDetails(ISBN isbn) {
        BnResponseDto response = client.searchBooks(isbn.value());
        return Optional.ofNullable(response.getBibs())
                .orElseGet(Collections::emptyList).stream()
                .findFirst().map(mapper::fromDto);
    }
}
