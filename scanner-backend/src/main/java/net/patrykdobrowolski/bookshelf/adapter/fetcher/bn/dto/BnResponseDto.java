package net.patrykdobrowolski.bookshelf.adapter.fetcher.bn.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder
@Getter
public class BnResponseDto {

    private final List<BookDto> bibs;
}
