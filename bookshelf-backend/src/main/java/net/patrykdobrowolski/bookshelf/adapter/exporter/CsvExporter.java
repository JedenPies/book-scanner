package net.patrykdobrowolski.bookshelf.adapter.exporter;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportFormat;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Slf4j
@Named
public class CsvExporter implements Exporter {

    @Override
    public boolean supports(ExportFormat format) {
        return format == ExportFormat.CSV;
    }

    @Override
    public ExportResult export(ExportData exportData) throws ExportFailedException {
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream);
                CSVPrinter printer = new CSVPrinter(
                        printWriter,
                        CSVFormat.DEFAULT.builder()
                                .setHeader(exportData.headers().stream().map(ExportData.Header::name).toList().toArray(String[]::new))
                                .get())
        ) {
            // Dodanie BOM (Byte Order Mark) dla UTF-8
            outputStream.write(239);
            outputStream.write(187);
            outputStream.write(191);

            for (ExportData.Row row : exportData.rows()) {
                printer.printRecord(row.values());
            }
            printer.flush();
            return new ExportResult(outputStream.toByteArray());

        } catch (Exception e) {
            throw new ExportFailedException(e.getMessage(), e);
        }
    }

//    @Override
//    public ExportResult export(CatalogingSession catalogingSession) throws ExportFailedException {
//        try (
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                PrintWriter printWriter = new PrintWriter(outputStream);
//                CSVPrinter printer = new CSVPrinter(
//                        printWriter,
//                        CSVFormat.DEFAULT.builder()
//                                .setHeader("ISBN", "Status", "Title", "Authors", "Publication year", "Publisher", "Publication place", "Language", "Added date")
//                                .get())
//        ) {
//            // Dodanie BOM (Byte Order Mark) dla UTF-8
//            outputStream.write(239);
//            outputStream.write(187);
//            outputStream.write(191);
//
//            for (DraftBook draftBook : catalogingSession.getDraftBooks()) {
//                printer.printRecord(
//                        draftBook.getIsbn().value(),
//                        draftBook.getStatus().name(),
//                        Optional.ofNullable(draftBook.getBookDetails()).map(BookDetails::title).orElse(""),
//                        String.join(", ", Optional.ofNullable(draftBook.getBookDetails()).map(BookDetails::authors).orElseGet(Collections::emptyList)),
//                        Optional.ofNullable(draftBook.getBookDetails()).map(BookDetails::publicationYear).map(Year::value).orElse(""),
//                        Optional.ofNullable(draftBook.getBookDetails()).map(BookDetails::publisher).orElse(""),
//                        Optional.ofNullable(draftBook.getBookDetails()).map(BookDetails::publicationPlace).orElse(""),
//                        Optional.ofNullable(draftBook.getBookDetails()).map(BookDetails::language).orElse(""),
//                        Optional.ofNullable(draftBook.getCreatedAt()).map(Object::toString).orElse(""));
//            }
//            printer.flush();
//            return new ExportResult(outputStream.toByteArray());
//
//        } catch (Exception e) {
//            throw new ExportFailedException(e.getMessage(), e);
//        }
//    }
}
