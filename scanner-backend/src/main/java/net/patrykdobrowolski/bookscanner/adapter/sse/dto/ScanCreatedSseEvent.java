package net.patrykdobrowolski.bookscanner.adapter.sse.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScanCreatedSseEvent {

    private final ScanDto scan;
}
