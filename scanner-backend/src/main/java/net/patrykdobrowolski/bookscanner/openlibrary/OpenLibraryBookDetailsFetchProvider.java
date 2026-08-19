package net.patrykdobrowolski.bookscanner.openlibrary;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
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
public class OpenLibraryBookDetailsFetchProvider implements BookDetailsFetchProvider {

    private final OpenLibraryFeignClient client;
    private final OpenLibraryBookDtoMapper mapper;

    @Override
    public String getKey() {
        return "openlibrary";
    }

    @Override
    public Optional<BookDetails> fetchBookDetails(ISBN isbn) {
        Map<String, BookDto> result = client.searchBooks("ISBN:" + isbn.value());
        return Optional.ofNullable(result.get("ISBN:" + isbn.value()))
                .map(mapper::fromDto);
    }
}
