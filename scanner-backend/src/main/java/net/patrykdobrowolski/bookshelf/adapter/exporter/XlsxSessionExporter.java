package net.patrykdobrowolski.bookshelf.adapter.exporter;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookshelf.domain.model.DraftBook;
import net.patrykdobrowolski.bookshelf.domain.model.ExportFormat;
import net.patrykdobrowolski.bookshelf.domain.model.Session;
import net.patrykdobrowolski.bookshelf.domain.model.Year;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Optional;

@Named
public class XlsxSessionExporter implements SessionExporter {

    @Override
    public boolean supports(ExportFormat format) {
        return ExportFormat.XLSX == format;
    }

    @Override
    public ExportResult export(Session session) throws ExportFailedException {
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Scanned books");
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            String[] columns = {"ISBN", "Status", "Title", "Authors", "Publication year", "Publisher", "Publication place", "Language", "Scanned date"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (DraftBook draftBook : session.getDraftBooks()) {
                Row row = sheet.createRow(rowNum++);
                boolean hasDetails = draftBook.getBookDetails() != null;

                row.createCell(0).setCellValue(draftBook.getIsbn().value());
                row.createCell(1).setCellValue(draftBook.getStatus().name());
                row.createCell(2).setCellValue(hasDetails ? draftBook.getBookDetails().title() : "");
                row.createCell(3).setCellValue(hasDetails ? String.join(", ", Optional.ofNullable(draftBook.getBookDetails().authors()).orElseGet(Collections::emptyList)) : "");
                row.createCell(4).setCellValue(hasDetails ? Optional.ofNullable(draftBook.getBookDetails().publicationYear()).map(Year::value).orElse("") : "");
                row.createCell(5).setCellValue(hasDetails ? draftBook.getBookDetails().publisher() : "");
                row.createCell(6).setCellValue(hasDetails ? draftBook.getBookDetails().publicationPlace() : "");
                row.createCell(7).setCellValue(hasDetails ? draftBook.getBookDetails().language() : "");
                row.createCell(8).setCellValue(draftBook.getCreatedAt().toString());
            }

            // 4. Estetyka: Automatyczne dopasowanie szerokości kolumn do tekstu
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 5. Zapis do strumienia i zwrot jako tablica bajtów
            workbook.write(out);
            return new ExportResult(out.toByteArray());

        } catch (Exception e) {
            throw new ExportFailedException("XLSX export failed", e);
        }
    }
}
