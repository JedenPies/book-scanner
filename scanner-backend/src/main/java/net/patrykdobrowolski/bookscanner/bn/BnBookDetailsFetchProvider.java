package net.patrykdobrowolski.bookscanner.bn;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class BnBookDetailsFetchProvider implements BookDetailsFetchProvider {

    private final BnFeignClient client;
    private final BnBookDtoMapper mapper;

    @Override
    public String getKey() {
        return "biblioteka-narodowa";
    }

    @Override
    @CircuitBreaker(name = "main", fallbackMethod = "fallbackFetchBookDetails")
    public Optional<BookDetails> fetchBookDetails(ISBN isbn) {
        BnResponseDto response = client.searchBooks(isbn.value());
        return Optional.ofNullable(response.getBibs())
                .orElseGet(Collections::emptyList).stream()
                .findFirst().map(mapper::fromDto);
    }

    @SuppressWarnings("unused")
    private Optional<BookDetails> fallbackFetchBookDetails(ISBN isbn, Throwable throwable) {
        log.error("Failed to call biblioteka narodowa books api for isbn: {} with error: {}", isbn, throwable.getMessage());
        return Optional.empty();
    }
}
