package net.patrykdobrowolski.bookscanner.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;


@Jacksonized
@Builder @Getter
public class CreateShareCodeRequestDto {

    @NotBlank(message = "Session ID cannot be empty")
    private final UUID sessionId;
}
