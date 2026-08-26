package net.patrykdobrowolski.bookscanner.adapter.exporter;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.domain.model.ExportFormat;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;

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
            String[] columns = {"ISBN", "Status", "Tytuł", "Autorzy", "Data Zeskanowania"};

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
                row.createCell(3).setCellValue(hasDetails ? String.join(", ", scan.getBookDetails().getAuthors()) : "");
                row.createCell(4).setCellValue(scan.getCreatedAt().toString());
            }

            // 4. Estetyka: Automatyczne dopasowanie szerokości kolumn do tekstu
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 5. Zapis do strumienia i zwrot jako tablica bajtów
            workbook.write(out);
            return new ExportResult(out.toByteArray());

        } catch (Exception e) {
            throw new ExportFailedException();
        }
    }
}
