package net.patrykdobrowolski.bookscanner.domain.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class EditScanCommand {

    private final String title;
    private final String subtitle;
    private final List<String> authors;
}
