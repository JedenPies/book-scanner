package net.patrykdobrowolski.bookscanner.domain.exception;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "of")
public class SessionNotFoundException extends Exception {

    private final UUID sessionId;
}
