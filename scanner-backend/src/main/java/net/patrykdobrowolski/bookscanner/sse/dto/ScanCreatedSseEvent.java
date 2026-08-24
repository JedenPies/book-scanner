package net.patrykdobrowolski.bookscanner.sse.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScanCreatedSseEvent {

    private final ScanDto scan;
}
