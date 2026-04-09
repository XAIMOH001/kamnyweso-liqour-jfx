package com.drinks.controllers;

import com.drinks.components.Icons;
import com.drinks.services.MockDataService;
import com.drinks.utils.PrintUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ReportsController {
    private final VBox view;
    private final TableView<ReportRow> table;
    private final String[] reportTypes = {"Sales Report", "Profit & Loss", "Order Report", "Stock Report", "Loyalty Report"};
    private String selectedReport = "Sales Report";

    public ReportsController() {
        view = new VBox(16);
        table = new TableView<>();

        Label title = new Label("Reports");
        title.getStyleClass().add("ds-title");

        HBox mainRow = new HBox(24);

        // Left: report type selector
        VBox selector = new VBox(4);
        selector.getStyleClass().add("ds-card");
        selector.setPadding(new Insets(8));
        selector.setPrefWidth(200);
        for (String rt : reportTypes) {
            Button btn = new Button(rt);
            btn.setGraphic(Icons.fileText(14, Color.web(rt.equals(selectedReport) ? "#FFFFFF" : "#6B7280")));
            btn.getStyleClass().addAll("report-type-btn");
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.CENTER_LEFT);
            if (rt.equals(selectedReport)) btn.getStyleClass().add("selected");
            btn.setOnAction(e -> {
                selectedReport = rt;
                updateSelector(selector);
                loadReportData();
            });
            selector.getChildren().add(btn);
        }

        // Right: config + preview
        VBox rightPanel = new VBox(16);
        rightPanel.getChildren().addAll(createConfigPanel(), createTable());

        mainRow.getChildren().addAll(selector, rightPanel);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        view.getChildren().addAll(title, mainRow);
        loadReportData();
    }

    private void updateSelector(VBox selector) {
        for (Node child : selector.getChildren()) {
            if (child instanceof Button) {
                child.getStyleClass().remove("selected");
            }
        }
        for (Node child : selector.getChildren()) {
            if (child instanceof Button btn && btn.getText().contains(selectedReport)) {
                btn.getStyleClass().add("selected");
                break;
            }
        }
    }

    private VBox createConfigPanel() {
        VBox card = new VBox(12);
        card.getStyleClass().add("ds-card");

        Label title = new Label("Generate " + selectedReport);
        title.getStyleClass().add("ds-heading");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(8);

        Label fromL = new Label("From Date"); fromL.getStyleClass().add("ds-label");
        DatePicker fromF = new DatePicker();
        Label toL = new Label("To Date"); toL.getStyleClass().add("ds-label");
        DatePicker toF = new DatePicker();

        Label branchesL = new Label("Branches"); branchesL.getStyleClass().add("ds-label");
        HBox branchesBox = new HBox(12);
        for (String b : new String[]{"All", "Nakuru", "Mombasa", "Kisumu", "HQ"}) {
            CheckBox cb = new CheckBox(b);
            if (b.equals("All")) cb.setSelected(true);
            branchesBox.getChildren().add(cb);
        }

        Label formatL = new Label("Format"); formatL.getStyleClass().add("ds-label");
        ToggleGroup formatGroup = new ToggleGroup();
        HBox formatBox = new HBox(16);
        RadioButton pdfRadio = new RadioButton("PDF");
        pdfRadio.setToggleGroup(formatGroup);
        pdfRadio.setSelected(true);
        RadioButton csvRadio = new RadioButton("CSV");
        csvRadio.setToggleGroup(formatGroup);
        formatBox.getChildren().addAll(pdfRadio, csvRadio);

        form.add(fromL, 0, 0); form.add(fromF, 0, 1);
        form.add(toL, 1, 0); form.add(toF, 1, 1);
        form.add(branchesL, 0, 2); form.add(branchesBox, 0, 3);
        GridPane.setColumnSpan(branchesBox, 2);
        form.add(formatL, 0, 4); form.add(formatBox, 0, 5);

        Button genBtn = new Button("Generate Report");
        genBtn.setPrefWidth(160);

        card.getChildren().addAll(title, form, genBtn);
        return card;
    }

    private VBox createTable() {
        VBox card = new VBox(0);
        card.getStyleClass().add("ds-card");

        HBox header = new HBox();
        header.setPadding(new Insets(12, 0, 12, 0));
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Preview: " + selectedReport);
        title.getStyleClass().add("ds-heading");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button pdfBtn = new Button("Export PDF");
        pdfBtn.setGraphic(Icons.download(14, Color.web("#2563EB")));
        pdfBtn.getStyleClass().add("btn-link");
        pdfBtn.setOnAction(e -> exportPDF());
        Button csvBtn = new Button("Export CSV");
        csvBtn.setGraphic(Icons.download(14, Color.web("#2563EB")));
        csvBtn.getStyleClass().add("btn-link");
        csvBtn.setOnAction(e -> exportCSV());
        header.getChildren().addAll(title, spacer, pdfBtn, csvBtn);

        table.getStyleClass().add("table-view");
        table.setPrefHeight(300);

        card.getChildren().addAll(header, table);
        return card;
    }

    private void loadReportData() {
        table.getColumns().clear();
        table.getItems().clear();

        MockDataService svc = MockDataService.getInstance();
        String[] cols = svc.getReportColumns(selectedReport);
        for (String col : cols) {
            TableColumn<ReportRow, String> tc = new TableColumn<>(col);
            int idx = table.getColumns().size();
            tc.setCellValueFactory(f -> f.getValue().cells[idx]);
            if (idx == 0) tc.setCellFactory(c -> new TableCell<>() {
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setText(null);
                    else { setText(item); getStyleClass().add("amount"); }
                }
            });
            table.getColumns().add(tc);
        }

        for (String[] row : svc.getReportData(selectedReport)) {
            table.getItems().add(new ReportRow(row));
        }

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void exportPDF() {
        String[] columns = table.getColumns().stream()
            .map(c -> ((TableColumn<ReportRow, ?>) c).getText())
            .toArray(String[]::new);
        List<String[]> rows = new ArrayList<>();
        for (ReportRow row : table.getItems()) {
            String[] values = new String[row.cells.length];
            for (int i = 0; i < row.cells.length; i++) {
                values[i] = row.cells[i].get();
            }
            rows.add(values);
        }
        String fileName = selectedReport.toLowerCase().replace(" ", "-") + "-" + java.time.LocalDate.now();
        javafx.stage.Stage stage = (javafx.stage.Stage) view.getScene().getWindow();
        PrintUtils.exportToPDF(stage, fileName, selectedReport, columns, rows);
    }

    private void exportCSV() {
        String[] columns = table.getColumns().stream()
            .map(c -> ((TableColumn<ReportRow, ?>) c).getText())
            .toArray(String[]::new);
        List<String[]> rows = new ArrayList<>();
        for (ReportRow row : table.getItems()) {
            String[] values = new String[row.cells.length];
            for (int i = 0; i < row.cells.length; i++) {
                values[i] = row.cells[i].get();
            }
            rows.add(values);
        }
        String fileName = selectedReport.toLowerCase().replace(" ", "-") + "-" + java.time.LocalDate.now();
        javafx.stage.Stage stage = (javafx.stage.Stage) view.getScene().getWindow();
        PrintUtils.exportToCSV(stage, fileName, columns, rows);
    }

    public VBox getView() { return view; }

    public static class ReportRow {
        public final javafx.beans.property.StringProperty[] cells;
        public ReportRow(String[] values) {
            cells = new javafx.beans.property.StringProperty[values.length];
            for (int i = 0; i < values.length; i++) {
                cells[i] = new javafx.beans.property.SimpleStringProperty(values[i]);
            }
        }
    }
}
