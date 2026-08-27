package net.patrykdobrowolski.bookscanner.adapter.sse.dto;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class ScanUpdatedSseEvent {

    private final ScanDto scan;
}
