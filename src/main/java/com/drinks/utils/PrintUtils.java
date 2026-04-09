package com.drinks.utils;

import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PrintUtils {

    public static void printReceipt(Node receiptNode) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(receiptNode.getScene().getWindow())) {
            boolean success = job.printPage(receiptNode);
            if (success) {
                job.endJob();
                showInfo("Print Successful", "Receipt has been sent to the printer.");
            } else {
                showError("Print Failed", "Failed to print the receipt. Please try again.");
            }
        }
    }

    public static void exportToPDF(Stage owner, String fileName, String title, String[] columns, List<String[]> rows) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Report as PDF");
        fileChooser.setInitialFileName(fileName + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(owner);
        if (file == null) return;

        try (PDDocument document = new PDDocument()) {
            float margin = 50;
            float tableWidth = PDRectangle.A4.getWidth() - 2 * margin;
            float rowHeight = 20;
            float headerHeight = 25;
            float colWidth = tableWidth / columns.length;

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float y = writePageHeader(document, page, contentStream, title, columns, colWidth, margin, headerHeight);

            contentStream.setNonStrokingColor(0.95f, 0.95f, 0.97f);
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);

            for (int r = 0; r < rows.size(); r++) {
                if (y < 50) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    y = page.getMediaBox().getHeight() - 50;

                    y = writePageHeader(document, page, contentStream, title, columns, colWidth, margin, headerHeight);
                    contentStream.setNonStrokingColor(0.95f, 0.95f, 0.97f);
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
                }

                if (r % 2 == 0) {
                    contentStream.setNonStrokingColor(0.96f, 0.97f, 0.98f);
                    contentStream.addRect(margin, y - rowHeight, tableWidth, rowHeight);
                    contentStream.fill();
                    contentStream.setNonStrokingColor(0.06f, 0.09f, 0.16f);
                }

                for (int i = 0; i < columns.length; i++) {
                    String cell = rows.get(r)[i];
                    if (cell.length() > 25) cell = cell.substring(0, 22) + "...";
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + i * colWidth + 5, y - 14);
                    contentStream.showText(cell);
                    contentStream.newLineAtOffset(-(margin + i * colWidth + 5), -(y - 14));
                    contentStream.endText();
                }
                y -= rowHeight;
            }
            contentStream.endText();
            contentStream.close();

            document.save(file);
            showInfo("Export Successful", "Report exported to:\n" + file.getAbsolutePath());
        } catch (IOException e) {
            showError("Export Failed", "Failed to export PDF:\n" + e.getMessage());
        }
    }

    private static float writePageHeader(PDDocument document, PDPage page, PDPageContentStream contentStream,
                                          String title, String[] columns, float colWidth, float margin, float headerHeight) throws IOException {
        float y = page.getMediaBox().getHeight() - 50;

        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
        contentStream.newLineAtOffset(margin, y);
        contentStream.showText(title);
        contentStream.endText();
        y -= 35;

        contentStream.setNonStrokingColor(0.15f, 0.15f, 0.3f);
        contentStream.addRect(margin, y - headerHeight, columns.length * colWidth, headerHeight);
        contentStream.fill();

        contentStream.setNonStrokingColor(1f, 1f, 1f);
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
        for (int i = 0; i < columns.length; i++) {
            contentStream.newLineAtOffset(margin + i * colWidth + 5, y - 15);
            contentStream.showText(columns[i]);
            contentStream.newLineAtOffset(-(margin + i * colWidth + 5), -(y - 15));
        }
        contentStream.endText();
        y -= headerHeight + 5;

        contentStream.setNonStrokingColor(0.06f, 0.09f, 0.16f);
        return y;
    }

    public static void exportToCSV(Stage owner, String fileName, String[] columns, List<String[]> rows) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Report as CSV");
        fileChooser.setInitialFileName(fileName + ".csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(owner);
        if (file == null) return;

        try (PrintWriter writer = new PrintWriter(file)) {
            for (int i = 0; i < columns.length; i++) {
                writer.print(escapeCsv(columns[i]));
                if (i < columns.length - 1) writer.print(",");
            }
            writer.println();

            for (String[] row : rows) {
                for (int i = 0; i < row.length; i++) {
                    writer.print(escapeCsv(row[i]));
                    if (i < row.length - 1) writer.print(",");
                }
                writer.println();
            }

            showInfo("Export Successful", "Report exported to:\n" + file.getAbsolutePath());
        } catch (IOException e) {
            showError("Export Failed", "Failed to export CSV:\n" + e.getMessage());
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
