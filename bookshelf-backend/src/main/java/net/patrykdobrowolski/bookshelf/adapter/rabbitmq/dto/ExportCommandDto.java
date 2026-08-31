package net.patrykdobrowolski.bookshelf.adapter.rabbitmq.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Jacksonized
@Builder @Getter
@RequiredArgsConstructor(staticName = "of")
public class ExportCommandDto {

    private final UUID exportId;
}
