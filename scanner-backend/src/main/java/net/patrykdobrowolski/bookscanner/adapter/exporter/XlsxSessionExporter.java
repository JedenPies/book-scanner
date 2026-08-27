package net.patrykdobrowolski.bookscanner.adapter.exporter;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.domain.model.ExportFormat;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.domain.model.Year;
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
            String[] columns = {"ISBN", "Status", "Tytuł", "Autorzy", "Rok wydania", "Wydawca", "Miejsce wydania", "Język", "Data Zeskanowania"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Scan scan : session.getScans()) {
                Row row = sheet.createRow(rowNum++);
                boolean hasDetails = scan.getBookDetails() != null;

                row.createCell(0).setCellValue(scan.getIsbn().value());
                row.createCell(1).setCellValue(scan.getStatus().name());
                row.createCell(2).setCellValue(hasDetails ? scan.getBookDetails().getTitle() : "");
                row.createCell(3).setCellValue(hasDetails ? String.join(", ", Optional.ofNullable(scan.getBookDetails().getAuthors()).orElseGet(Collections::emptyList)) : "");
                row.createCell(4).setCellValue(hasDetails ? Optional.ofNullable(scan.getBookDetails().getPublicationYear()).map(Year::value).orElse("") : "");
                row.createCell(5).setCellValue(hasDetails ? scan.getBookDetails().getPublisher() : "");
                row.createCell(6).setCellValue(hasDetails ? scan.getBookDetails().getPublicationPlace() : "");
                row.createCell(7).setCellValue(hasDetails ? scan.getBookDetails().getLanguage() : "");
                row.createCell(8).setCellValue(scan.getCreatedAt().toString());
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
