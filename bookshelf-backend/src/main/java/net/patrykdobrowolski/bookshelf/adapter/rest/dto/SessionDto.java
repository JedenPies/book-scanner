package net.patrykdobrowolski.bookshelf.adapter.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Jacksonized
@Builder @Getter
public class SessionDto {

    private final UUID id;
}
