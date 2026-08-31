package net.patrykdobrowolski.bookshelf.domain.model.value;

import java.util.Optional;

public record ISBN(String value) {

    public ISBN(String value) {

        String cleanValue = Optional.ofNullable(value)
                .map(v -> v.replace("-", "").trim())
                .orElseThrow(() -> new IllegalArgumentException("ISBN cannot be null or empty"));
        if (!cleanValue.matches("^[0-9]+[Xx]?$") || (cleanValue.length() != 10 && cleanValue.length() != 13)) {
            throw new IllegalArgumentException("ISBN must be exactly 10 or 13 characters long");
        }
        this.value = cleanValue;
    }
}
