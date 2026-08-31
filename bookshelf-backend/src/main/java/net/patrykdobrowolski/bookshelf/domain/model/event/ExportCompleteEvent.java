package net.patrykdobrowolski.bookshelf.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.export.Export;

@RequiredArgsConstructor(staticName = "of") @Getter
public class ExportCompleteEvent extends BusinessEvent {

    private final Export export;
}
