package net.patrykdobrowolski.bookscanner.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.ISBN;

@Jacksonized
@Builder @Getter
public class CreateScanRequestDto {

    @NotBlank(message = "ISBN cannot be empty")
    @ISBN(message = "Invalid ISBN format")
    private final String isbn;
}
