package net.patrykdobrowolski.bookshelf.adapter.sse.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder @Getter
public class DraftBooksDeletedSseEvent {

    private final List<DraftBookDto> draftBooks;
    private final int count;
}
