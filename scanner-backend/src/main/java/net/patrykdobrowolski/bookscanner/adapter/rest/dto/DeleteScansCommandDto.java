package net.patrykdobrowolski.bookscanner.adapter.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Jacksonized
@Builder @Getter
public class DeleteScansCommandDto {

    private List<UUID> scanIds;
}
