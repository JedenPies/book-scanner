package net.patrykdobrowolski.bookscanner.sse.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScanDeletedSseEvent {

    private final ScanDto scan;
}
