package net.patrykdobrowolski.bookscanner.googleapi;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.model.BookRaw;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.fetcher.BookFetchProvider;
import net.patrykdobrowolski.bookscanner.googleapi.dto.BooksResponseDto;
import net.patrykdobrowolski.bookscanner.googleapi.mapper.BooksResponseDtoMapper;
import org.springframework.beans.factory.annotation.Value;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@Named
@RequiredArgsConstructor
@Slf4j
public class GoogleBookFetchProvider implements BookFetchProvider {

    static final String GOOGLE_PROVIDER_KEY = "google";

    private final GoogleBooksFeignClient client;
    private final ObjectMapper objectMapper;
    private final BooksResponseDtoMapper mapper;

    @Value("${api.google-books.api-key}")
    private String apiKey;

    @Override
    public String getKey() {
        return GOOGLE_PROVIDER_KEY;
    }

    @Override
    @CircuitBreaker(name = "google-books-api", fallbackMethod = "fallbackFetchBookRaw")
    public Optional<BookRaw> fetchBookRaw(ISBN isbn) {
        String rawResponse = client.searchBooks("isbn:" + isbn.value(), apiKey);
        BooksResponseDto response = objectMapper.readValue(rawResponse, BooksResponseDto.class);
        if (response.getItems() == null || response.getItems().isEmpty()) return Optional.empty();
        return Optional.of(BookRaw.from(rawResponse));
    }

    @SuppressWarnings("unused")
    private Optional<BookRaw> fallbackFetchBookRaw(ISBN isbn, Throwable throwable) {
        log.error("Failed to call google books api for isbn: {} with error: {}", isbn, throwable.getMessage());
        return Optional.empty();
    }
}
