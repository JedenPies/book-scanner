package net.patrykdobrowolski.bookscanner.googleapi;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
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
@Order(2)
public class GoogleBookDetailsFetchProvider implements BookDetailsFetchProvider {

    private final GoogleBooksFeignClient client;
    private final BooksResponseDtoMapper mapper;

    @Value("${api.google-books.api-key}")
    private String apiKey;

    @Override
    public Optional<BookDetails> fetchBookDetails(ISBN isbn) {
        BooksResponseDto response = client.searchBooks("isbn:" + isbn.value(), apiKey);
        Optional<ItemDto> firstItem = Optional.ofNullable(response.getItems()).orElseGet(Collections::emptyList).stream().findFirst();
        return firstItem.map(itemDto -> mapper.fromDto(itemDto.getVolumeInfo()));
    }
}
