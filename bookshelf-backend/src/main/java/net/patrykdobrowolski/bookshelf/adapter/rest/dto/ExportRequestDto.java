package net.patrykdobrowolski.bookshelf.adapter.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import net.patrykdobrowolski.bookshelf.domain.model.ExportFormat;

@Jacksonized
@Builder @Getter
public class ExportRequestDto {

    private final ExportFormat format;
}
