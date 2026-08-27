package net.patrykdobrowolski.bookscanner.adapter.fetcher.bn.mapper;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.bn.dto.BookDto;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;

import java.util.Collections;

@Named
public class BnBookDtoMapper {

    public BookDetails fromDto(BookDto dto) {
        return BookDetails.builder()
                .title(dto.getTitle())
                .authors(Collections.singletonList(dto.getAuthor()))
                .build();
    }
}
