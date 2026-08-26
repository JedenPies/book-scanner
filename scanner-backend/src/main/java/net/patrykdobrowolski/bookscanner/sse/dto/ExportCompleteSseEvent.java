package net.patrykdobrowolski.bookscanner.sse.dto;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class ExportCompleteSseEvent {

    private final ExportDto export;
}
