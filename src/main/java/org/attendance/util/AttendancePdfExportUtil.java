package org.attendance.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.attendance.dto.response.AttendanceRecordDTO;

import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;

public class AttendancePdfExportUtil {

    public static void exportToPdf(List<AttendanceRecordDTO> records, OutputStream out) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("📄 Attendance Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 2.5f, 2.5f, 2.5f, 3.5f, 2f});

        addHeader(table);

        int index = 1;
        for (AttendanceRecordDTO r : records) {
            table.addCell(String.valueOf(index++));
            table.addCell(String.valueOf(r.getDate()));
            table.addCell(r.getUsername() != null ? r.getUsername() : "-");
            table.addCell(r.getRollNumber() != null ? r.getRollNumber() : "-");
            table.addCell(r.getEmail() != null ? r.getEmail() : "-");
            table.addCell(r.getStatus().toString());
        }

        document.add(table);
        document.close();
    }

    private static void addHeader(PdfPTable table) {
        Stream.of("#", "Date", "Student", "Roll No", "Email", "Status").forEach(col -> {
            PdfPCell cell = new PdfPCell(new Phrase(col));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        });
    }
}