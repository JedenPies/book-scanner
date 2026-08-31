package net.patrykdobrowolski.bookshelf.domain.exception;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "of")
public class CatalogingSessionNotFoundException extends Exception {

    private final UUID catalogingSessionId;
}
