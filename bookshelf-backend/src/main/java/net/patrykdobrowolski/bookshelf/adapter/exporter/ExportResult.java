package net.patrykdobrowolski.bookshelf.adapter.exporter;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExportResult {

    private final byte[] data;
}
