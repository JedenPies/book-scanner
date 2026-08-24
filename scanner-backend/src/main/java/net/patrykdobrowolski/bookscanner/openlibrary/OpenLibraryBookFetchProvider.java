package net.patrykdobrowolski.bookscanner.openlibrary;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.model.BookRaw;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.fetcher.BookFetchProvider;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Named
@RequiredArgsConstructor
@Slf4j
public class OpenLibraryBookFetchProvider implements BookFetchProvider {

    static final String OPEN_LIBRARY_PROVIDER_KEY = "open-library";

    private final OpenLibraryFeignClient client;
    private final ObjectMapper objectMapper;

    @Override
    public String getKey() {
        return OPEN_LIBRARY_PROVIDER_KEY;
    }

    @Override
    @CircuitBreaker(name = "open-library-api", fallbackMethod = "fallbackFetchBookRaw")
    public BookRaw fetchBookRaw(ISBN isbn) {
        Map<String, Object> rawResult = client.searchBooks("ISBN:" + isbn.value());
        Object resultGet = rawResult.get("ISBN:" + isbn.value());
        if (resultGet == null) return BookRaw.notFound();
        String rawBook = objectMapper.writeValueAsString(resultGet);
        if (rawBook == null || rawBook.isBlank()) return BookRaw.notFound();
        return BookRaw.from(rawBook);
    }

    @SuppressWarnings("unused")
    private BookRaw fallbackFetchBookRaw(ISBN isbn, Throwable throwable) {
        log.error("Failed to call open library books api for ISBN: {} with error: {}", isbn, throwable.getMessage());
        return BookRaw.failure();
    }
}
