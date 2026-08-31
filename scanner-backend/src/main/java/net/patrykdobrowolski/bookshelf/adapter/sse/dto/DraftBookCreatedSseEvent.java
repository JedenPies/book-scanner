package net.patrykdobrowolski.bookshelf.adapter.sse.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DraftBookCreatedSseEvent {

    private final DraftBookDto scan;
}
