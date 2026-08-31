package net.patrykdobrowolski.bookshelf.domain.model.value;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Year {

    private final String value;

    public static Year parse(String value) {
        if (value.matches("^[0-9]{4}$")) {
            return new Year(value);
        }
        throw new NumberFormatException("Invalid value: " + value);
    }

    public String value() {
        return value;
    }

}
