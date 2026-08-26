package net.patrykdobrowolski.bookscanner.adapter.exporter;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.ExportFormat;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Optional;

@Named
public class CsvSessionExporter implements SessionExporter {

    @Override
    public boolean supports(ExportFormat format) {
        return format == ExportFormat.CSV;
    }

    @Override
    public ExportResult export(Session session) throws ExportFailedException {
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream);
                CSVPrinter printer = new CSVPrinter(
                        printWriter,
                        CSVFormat.DEFAULT.builder()
                                .setHeader("ISBN", "Status", "Tytuł", "Autorzy", "Źródło", "Data Zeskanowania")
                                .get())
        ) {
            // Dodanie BOM (Byte Order Mark) dla UTF-8
            outputStream.write(239);
            outputStream.write(187);
            outputStream.write(191);

            for (Scan scan : session.getScans()) {
                printer.printRecord(
                        scan.getIsbn().value(),
                        scan.getStatus().name(),
                        Optional.ofNullable(scan.getBookDetails()).map(BookDetails::getTitle).orElse(""),
                        String.join(", ", Optional.ofNullable(scan.getBookDetails()).map(BookDetails::getAuthors).orElseGet(Collections::emptyList)),
                        Optional.ofNullable(scan.getBookDetails()).map(BookDetails::getSource).orElse(""),
                        Optional.ofNullable(scan.getCreatedAt()).map(Object::toString).orElse(""));
            }
            printer.flush();
            return new ExportResult(outputStream.toByteArray());

        } catch (Exception e) {
            throw new ExportFailedException();
        }
    }
}
