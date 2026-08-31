package net.patrykdobrowolski.bookscanner.adapter.sse.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder @Getter
public class ScansDeletedSseEvent {

    private final List<ScanDto> scans;
    private final int count;
}
