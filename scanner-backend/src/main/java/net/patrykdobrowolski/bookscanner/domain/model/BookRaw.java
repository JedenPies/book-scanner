package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.Instant;

@Getter
@Builder @AllArgsConstructor
public class BookRaw {

    @With
    private String source;
    private String value;
    private Instant createdAt;
    private Instant modifiedAt;

    private BookRaw(String value) {
        this.value = value;
    }

    public static BookRaw from(String rawResult) {
        return new BookRaw(rawResult);
    }
}
