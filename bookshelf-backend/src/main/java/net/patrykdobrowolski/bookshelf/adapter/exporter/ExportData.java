package net.patrykdobrowolski.bookshelf.adapter.exporter;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public record ExportData(
        String title,
        List<Header> headers,
        @Singular("row")
        List<Row> rows
) {
    public ExportData {
        headers = headers == null ? List.of() : List.copyOf(headers);
        rows = rows == null ? List.of() : List.copyOf(rows);
    }

    public record Header(String name) {}

    public record Row(
        List<String> values
    ) {
        public Row {
            values = values == null ? List.of() : List.copyOf(values);
        }

        public Row(String... values) {
            this(List.of(values));
        }
    }
}
