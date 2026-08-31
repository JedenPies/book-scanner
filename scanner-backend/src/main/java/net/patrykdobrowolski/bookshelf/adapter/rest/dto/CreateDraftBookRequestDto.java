package net.patrykdobrowolski.bookshelf.adapter.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.ISBN;

@Jacksonized
@Builder @Getter
public class CreateDraftBookRequestDto {

    @NotBlank(message = "ISBN cannot be empty")
    @ISBN(message = "Invalid ISBN format", type = ISBN.Type.ANY)
    private final String isbn;
}
