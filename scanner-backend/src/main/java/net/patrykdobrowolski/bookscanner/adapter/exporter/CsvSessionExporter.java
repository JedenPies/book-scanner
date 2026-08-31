package net.patrykdobrowolski.bookscanner.adapter.exporter;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.model.ExportFormat;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.domain.model.Year;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Optional;

@Slf4j
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
                                .setHeader("ISBN", "Status", "Title", "Authors", "Publication year", "Publisher", "Publication place", "Language", "Scanned date")
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
                        Optional.ofNullable(scan.getBookDetails()).map(BookDetails::title).orElse(""),
                        String.join(", ", Optional.ofNullable(scan.getBookDetails()).map(BookDetails::authors).orElseGet(Collections::emptyList)),
                        Optional.ofNullable(scan.getBookDetails()).map(BookDetails::publicationYear).map(Year::value).orElse(""),
                        Optional.ofNullable(scan.getBookDetails()).map(BookDetails::publisher).orElse(""),
                        Optional.ofNullable(scan.getBookDetails()).map(BookDetails::publicationPlace).orElse(""),
                        Optional.ofNullable(scan.getBookDetails()).map(BookDetails::language).orElse(""),
                        Optional.ofNullable(scan.getCreatedAt()).map(Object::toString).orElse(""));
            }
            printer.flush();
            return new ExportResult(outputStream.toByteArray());

        } catch (Exception e) {
            throw new ExportFailedException(e.getMessage(), e);
        }
    }
}
