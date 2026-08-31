package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.model.Session;

public interface ExportCreatorAsyncPort {

    void exportSession(Session session);
}
