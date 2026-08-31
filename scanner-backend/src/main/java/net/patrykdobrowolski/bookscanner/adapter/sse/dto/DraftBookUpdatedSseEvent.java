package net.patrykdobrowolski.bookscanner.adapter.sse.dto;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class DraftBookUpdatedSseEvent {

    private final DraftBookDto scan;
}
