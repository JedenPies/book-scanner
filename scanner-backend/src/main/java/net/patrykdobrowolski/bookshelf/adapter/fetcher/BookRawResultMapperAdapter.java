package net.patrykdobrowolski.bookshelf.adapter.fetcher;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookshelf.domain.model.BookRaw;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
public class BookRawResultMapperAdapter {

    private final Map<String, BookRawResultMapper> mappersByKey;

    public BookRawResultMapperAdapter(List<BookRawResultMapper> mappers) {
        this.mappersByKey = mappers.stream().collect(Collectors.toMap(BookRawResultMapper::getKey, br -> br));
    }

    public BookDetails map(BookRaw bookRaw) {
        return Optional.ofNullable(mappersByKey.get(bookRaw.getSource()))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported source: " + bookRaw.getSource()))
                .map(bookRaw).withSource(bookRaw.getSource());
    }
}
