package net.patrykdobrowolski.bookscanner.openlibrary;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.fetcher.BookDetailsFetchProvider;
import net.patrykdobrowolski.bookscanner.openlibrary.dto.BookDto;
import net.patrykdobrowolski.bookscanner.openlibrary.mapper.OpenLibraryBookDtoMapper;
import org.springframework.core.annotation.Order;

import java.util.Map;
import java.util.Optional;

@Named
@RequiredArgsConstructor
@Order(30)
@Slf4j
public class OpenLibraryBookDetailsFetchProvider implements BookDetailsFetchProvider {

    private final OpenLibraryFeignClient client;
    private final OpenLibraryBookDtoMapper mapper;

    @Override
    public String getKey() {
        return "open-library";
    }

    @Override
    @CircuitBreaker(name = "main", fallbackMethod = "fallbackFetchBookDetails")
    public Optional<BookDetails> fetchBookDetails(ISBN isbn) {
        Map<String, BookDto> result = client.searchBooks("ISBN:" + isbn.value());
        return Optional.ofNullable(result.get("ISBN:" + isbn.value()))
                .map(mapper::fromDto);
    }

    @SuppressWarnings("unused")
    private Optional<BookDetails> fallbackFetchBookDetails(ISBN isbn, Throwable throwable) {
        log.error("Failed to call open library books api for isbn: {} with error: {}", isbn, throwable.getMessage());
        return Optional.empty();
    }
}
