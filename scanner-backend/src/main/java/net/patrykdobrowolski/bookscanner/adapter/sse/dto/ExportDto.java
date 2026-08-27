package net.patrykdobrowolski.bookscanner.adapter.sse.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExportDto {

    private final String id;
    private final String format;
    private final String status;

}
