package net.patrykdobrowolski.bookshelf.adapter.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;


@Jacksonized
@Builder @Getter
public class CreateShareCodeRequestDto {

    @NotBlank(message = "Session ID cannot be empty")
    @NotNull
    private final UUID sessionId;
}
