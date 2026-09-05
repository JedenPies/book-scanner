package net.patrykdobrowolski.bookshelf.adapter.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Jacksonized
@Builder @Getter
public class DeleteDraftBookCommandDto {

    @Size(min = 1) @NotNull
    private List<@NotNull UUID> draftBooksIds;
}
