package net.patrykdobrowolski.bookscanner.domain.model.command;

import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookscanner.domain.model.Year;

import java.util.List;

@Builder
@Getter
public class UpdateScanCommand {

    private final String title;
    private final List<String> authors;
    private final String publisher;
    private final Year publicationYear;
    private final String publicationPlace;
    private final String language;
    private final List<String> genres;
}
