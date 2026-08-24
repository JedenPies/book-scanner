package net.patrykdobrowolski.bookscanner.bn;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.bn.dto.BnResponseDto;
import net.patrykdobrowolski.bookscanner.domain.model.BookFetchResult;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.fetcher.BookFetchProvider;
import tools.jackson.databind.ObjectMapper;

@Named
@RequiredArgsConstructor
@Slf4j
public class BnBookFetchProvider implements BookFetchProvider {

    static final String BIBLIOTEKA_NARODOWA_PROVIDER_KEY = "biblioteka-narodowa";

    private final BnFeignClient client;
    private final ObjectMapper objectMapper;

    @Override
    public String getKey() {
        return BIBLIOTEKA_NARODOWA_PROVIDER_KEY;
    }

    @Override
    @CircuitBreaker(name = "biblioteka-narodowa-api", fallbackMethod = "fallbackFetchBookRaw")
    public BookFetchResult fetchBookRaw(ISBN isbn) {
        String rawResponse = client.searchBooks(isbn.value());
        BnResponseDto response = objectMapper.readValue(rawResponse, BnResponseDto.class);
        if (response.getBibs() == null || response.getBibs().isEmpty()) return BookFetchResult.notFound();
        return BookFetchResult.success(rawResponse);
    }

    @SuppressWarnings("unused")
    private BookFetchResult fallbackFetchBookRaw(ISBN isbn, Throwable throwable) {
        log.error("Failed to call biblioteka narodowa books api for isbn: {} with error: {}", isbn, throwable.getMessage());
        return BookFetchResult.failure();
    }
}
