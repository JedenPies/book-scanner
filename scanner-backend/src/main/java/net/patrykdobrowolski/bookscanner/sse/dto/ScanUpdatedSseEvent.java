package net.patrykdobrowolski.bookscanner.sse.dto;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class ScanUpdatedSseEvent {

    private final ScanDto scan;
}
