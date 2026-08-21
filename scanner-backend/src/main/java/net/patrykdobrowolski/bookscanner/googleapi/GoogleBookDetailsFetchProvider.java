package net.patrykdobrowolski.bookscanner.googleapi;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.fetcher.BookDetailsFetchProvider;
import net.patrykdobrowolski.bookscanner.googleapi.dto.BooksResponseDto;
import net.patrykdobrowolski.bookscanner.googleapi.dto.ItemDto;
import net.patrykdobrowolski.bookscanner.googleapi.mapper.BooksResponseDtoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import java.util.Collections;
import java.util.Optional;

@Named
@RequiredArgsConstructor
@Order(20)
@Slf4j
public class GoogleBookDetailsFetchProvider implements BookDetailsFetchProvider {

    private final GoogleBooksFeignClient client;
    private final BooksResponseDtoMapper mapper;

    @Value("${api.google-books.api-key}")
    private String apiKey;

    @Override
    public String getKey() {
        return "google";
    }

    @Override
    @CircuitBreaker(name = "main", fallbackMethod = "fallbackFetchBookDetails")
    public Optional<BookDetails> fetchBookDetails(ISBN isbn) {
        BooksResponseDto response = client.searchBooks("isbn:" + isbn.value(), apiKey);
        Optional<ItemDto> firstItem = Optional.ofNullable(response.getItems()).orElseGet(Collections::emptyList).stream().findFirst();
        return firstItem.map(itemDto -> mapper.fromDto(itemDto.getVolumeInfo()));
    }

    @SuppressWarnings("unused")
    private Optional<BookDetails> fallbackFetchBookDetails(ISBN isbn, Throwable throwable) {
        log.error("Failed to call google books api for isbn: {} with error: {}", isbn, throwable.getMessage());
        return Optional.empty();
    }
}
