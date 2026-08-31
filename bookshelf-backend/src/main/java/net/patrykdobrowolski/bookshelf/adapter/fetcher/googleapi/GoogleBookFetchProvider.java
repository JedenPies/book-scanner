package net.patrykdobrowolski.bookshelf.adapter.fetcher.googleapi;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookshelf.domain.model.BookFetchResult;
import net.patrykdobrowolski.bookshelf.domain.model.value.ISBN;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.BookFetchProvider;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.googleapi.dto.BooksResponseDto;
import org.springframework.beans.factory.annotation.Value;
import tools.jackson.databind.ObjectMapper;

@Named
@RequiredArgsConstructor
@Slf4j
public class GoogleBookFetchProvider implements BookFetchProvider {

    static final String GOOGLE_PROVIDER_KEY = "google";

    private final GoogleBooksFeignClient client;
    private final ObjectMapper objectMapper;

    @Value("${api.google-books.api-key}")
    private String apiKey;

    @Override
    public String getKey() {
        return GOOGLE_PROVIDER_KEY;
    }

    @Override
    @CircuitBreaker(name = "google-books-api", fallbackMethod = "fallbackFetchBookRaw")
    public BookFetchResult fetchBookRaw(ISBN isbn) {
        String rawResponse = client.searchBooks("isbn:" + isbn.value(), apiKey);
        BooksResponseDto response = objectMapper.readValue(rawResponse, BooksResponseDto.class);
        if (response.getItems() == null || response.getItems().isEmpty()) return BookFetchResult.notFound();
        return BookFetchResult.success(rawResponse);
    }

    @SuppressWarnings("unused")
    private BookFetchResult fallbackFetchBookRaw(ISBN isbn, Throwable throwable) {
        log.error("Failed to call google books api for isbn: {} with error: {}", isbn, throwable.getMessage());
        return BookFetchResult.failure();
    }
}
