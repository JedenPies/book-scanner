package net.patrykdobrowolski.bookscanner.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class ScansDeletedEvent extends BusinessEvent {

    private final Session session;
    private final List<Scan> scans;
}
