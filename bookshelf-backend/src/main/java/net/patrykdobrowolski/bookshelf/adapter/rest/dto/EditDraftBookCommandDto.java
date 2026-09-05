package net.patrykdobrowolski.bookshelf.adapter.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import net.patrykdobrowolski.bookshelf.util.NullOrNotBlank;

import java.util.List;

@Jacksonized
@Builder @Getter
public class EditDraftBookCommandDto {

    @Size(max = 500)
    @NotBlank
    private final String title;
    private final List<@NotBlank String> authors;

    @NullOrNotBlank
    @Size(max = 200)
    private final String publisher;

    @NullOrNotBlank
    @Pattern(regexp = "^[0-9]{4}$")
    @Size(max = 4, min = 4)
    private final String publicationYear;

    @NullOrNotBlank
    @Size(max = 200)
    private final String publicationPlace;

    @NullOrNotBlank
    @Size(max = 50)
    private final String language;
    private final List<@NotBlank String> genres;
}
