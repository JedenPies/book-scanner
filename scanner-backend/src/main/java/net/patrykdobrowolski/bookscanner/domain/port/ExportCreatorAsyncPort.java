package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.model.Session;

public interface ExportCreatorAsyncPort {

    void exportSession(Session session);
}
