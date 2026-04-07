package com.drinks.controllers;

import com.drinks.components.StatCard;
import com.drinks.components.StatusBadge;
import com.drinks.models.Return;
import com.drinks.models.StockItem;
import com.drinks.services.MockDataService;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class StockManagementController {
    private final VBox view;
    private final TableView<StockItem> stockTable;
    private final TableView<Return> returnsTable;
    private final ObservableList<StockItem> allStock;
    private final ObservableList<Return> allReturns;

    public StockManagementController() {
        view = new VBox(16);
        allStock = MockDataService.getInstance().getStockItems();
        allReturns = MockDataService.getInstance().getAdminReturns();

        Label title = new Label("Stock Management");
        title.getStyleClass().add("ds-title");

        HBox statsRow = new HBox(16);
        statsRow.getChildren().addAll(
            new StatCard("Total SKUs", "24", "\u2191 2 new items", Color.web("#2563EB")),
            new StatCard("Low Stock Items", "3", "\u2191 1 vs yesterday", Color.web("#D97706")),
            new StatCard("Out of Stock", "1", "\u2191 Guinness Nakuru", Color.web("#DC2626")),
            new StatCard("Pending Returns", "1", "\u2191 Awaiting review", Color.web("#7C3AED"))
        );
        for (Node card : statsRow.getChildren()) HBox.setHgrow(card, Priority.ALWAYS);

        HBox filters = createFilters();
        stockTable = createStockTable();
        returnsTable = createReturnsTable();

        view.getChildren().addAll(title, statsRow, filters, stockTable, returnsTable);
    }

    private HBox createFilters() {
        HBox row = new HBox(12);
        row.getStyleClass().add("ds-card");
        row.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search items...");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        ComboBox<String> branchField = new ComboBox<>();
        branchField.getItems().addAll("All Branches", "Nakuru", "Mombasa", "Kisumu");
        branchField.setValue("All Branches");

        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("All Statuses", "OK", "Low", "Critical", "Out of Stock");
        statusField.setValue("All Statuses");

        row.getChildren().addAll(searchField, branchField, statusField);

        searchField.textProperty().addListener((obs, o, n) -> applyFilters(searchField, branchField, statusField));
        branchField.valueProperty().addListener((obs, o, n) -> applyFilters(searchField, branchField, statusField));
        statusField.valueProperty().addListener((obs, o, n) -> applyFilters(searchField, branchField, statusField));

        return row;
    }

    private void applyFilters(TextField search, ComboBox<String> branch, ComboBox<String> status) {
        String s = search.getText().toLowerCase();
        String b = branch.getValue();
        String st = status.getValue();
        stockTable.getItems().clear();
        for (StockItem item : allStock) {
            boolean matchSearch = s.isEmpty() || item.getName().toLowerCase().contains(s) || item.getBrand().toLowerCase().contains(s);
            boolean matchBranch = b.equals("All Branches") || item.getBranch().equals(b);
            boolean matchStatus = st.equals("All Statuses") || item.getStatus().equals(st);
            if (matchSearch && matchBranch && matchStatus) stockTable.getItems().add(item);
        }
    }

    private TableView<StockItem> createStockTable() {
        VBox wrapper = new VBox(0);
        wrapper.getStyleClass().add("ds-card");

        Label header = new Label("Stock Inventory");
        header.getStyleClass().add("ds-heading");
        header.setPadding(new Insets(12, 0, 12, 0));

        TableView<StockItem> tbl = new TableView<>();
        tbl.getStyleClass().add("table-view");

        TableColumn<StockItem, String> colName = new TableColumn<>("Item Name");
        colName.setCellValueFactory(f -> f.getValue().nameProperty());

        TableColumn<StockItem, String> colBrand = new TableColumn<>("Brand");
        colBrand.setCellValueFactory(f -> f.getValue().brandProperty());
        colBrand.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        TableColumn<StockItem, String> colBranch = new TableColumn<>("Branch");
        colBranch.setCellValueFactory(f -> f.getValue().branchProperty());

        TableColumn<StockItem, Number> colStock = new TableColumn<>("Current Stock");
        colStock.setCellValueFactory(f -> f.getValue().stockProperty());
        colStock.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(String.valueOf(item.intValue())); getStyleClass().add("amount"); }
            }
        });

        TableColumn<StockItem, Number> colMin = new TableColumn<>("Min Threshold");
        colMin.setCellValueFactory(f -> f.getValue().minProperty());
        colMin.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(String.valueOf(item.intValue())); getStyleClass().add("muted"); }
            }
        });

        TableColumn<StockItem, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(f -> f.getValue().statusProperty());
        colStatus.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else setGraphic(new StatusBadge(item));
            }
        });

        TableColumn<StockItem, String> colRestocked = new TableColumn<>("Last Restocked");
        colRestocked.setCellValueFactory(f -> f.getValue().restockedProperty());
        colRestocked.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        TableColumn<StockItem, Void> colActions = new TableColumn<>("Actions");
        colActions.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Button btn = new Button("Restock");
                    btn.getStyleClass().add("btn-link");
                    btn.setOnAction(e -> showRestockDialog(getTableView().getItems().get(getIndex())));
                    setGraphic(btn);
                }
            }
        });

        tbl.getColumns().addAll(colName, colBrand, colBranch, colStock, colMin, colStatus, colRestocked, colActions);
        tbl.setItems(allStock);
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbl.setPrefHeight(250);

        wrapper.getChildren().addAll(header, tbl);
        return tbl;
    }

    private TableView<Return> createReturnsTable() {
        VBox wrapper = new VBox(0);
        wrapper.getStyleClass().add("ds-card");

        Label header = new Label("Damaged Stock Returns");
        header.getStyleClass().add("ds-heading");
        header.setPadding(new Insets(12, 0, 12, 0));

        TableView<Return> tbl = new TableView<>();
        tbl.getStyleClass().add("table-view");

        TableColumn<Return, String> colId = new TableColumn<>("Return ID");
        colId.setCellValueFactory(f -> f.getValue().idProperty());
        colId.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("mono"); }
            }
        });

        TableColumn<Return, String> colBranch = new TableColumn<>("Branch");
        colBranch.setCellValueFactory(f -> f.getValue().branchProperty());

        TableColumn<Return, String> colItem = new TableColumn<>("Item");
        colItem.setCellValueFactory(f -> f.getValue().itemProperty());

        TableColumn<Return, Number> colQty = new TableColumn<>("Qty");
        colQty.setCellValueFactory(f -> f.getValue().qtyProperty());

        TableColumn<Return, String> colReason = new TableColumn<>("Reason");
        colReason.setCellValueFactory(f -> f.getValue().reasonProperty());
        colReason.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        TableColumn<Return, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(f -> f.getValue().statusProperty());
        colStatus.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else setGraphic(new StatusBadge(item));
            }
        });

        TableColumn<Return, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(f -> f.getValue().dateProperty());
        colDate.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        TableColumn<Return, Void> colActions = new TableColumn<>("Actions");
        colActions.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Return r = getTableView().getItems().get(getIndex());
                    if ("Pending Review".equals(r.getStatus())) {
                        HBox btns = new HBox(8);
                        Button approve = new Button("Approve");
                        approve.getStyleClass().addAll("btn-link", "btn-approve");
                        Button reject = new Button("Reject");
                        reject.getStyleClass().addAll("btn-link", "btn-reject");
                        btns.getChildren().addAll(approve, reject);
                        setGraphic(btns);
                    } else setGraphic(null);
                }
            }
        });

        tbl.getColumns().addAll(colId, colBranch, colItem, colQty, colReason, colStatus, colDate, colActions);
        tbl.setItems(allReturns);
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbl.setPrefHeight(180);

        wrapper.getChildren().addAll(header, tbl);
        return tbl;
    }

    private void showRestockDialog(StockItem item) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Restock Item");
        VBox content = new VBox(12);
        content.setPadding(new Insets(8));

        Label itemLabel = new Label("Item: " + item.getName());
        itemLabel.getStyleClass().add("ds-heading");
        Label branchLabel = new Label("Branch: " + item.getBranch());
        Label stockLabel = new Label("Current Stock: " + item.getStock() + " units");

        Label qtyL = new Label("Quantity to Send");
        qtyL.getStyleClass().add("ds-label");
        TextField qtyF = new TextField();
        qtyF.setPromptText("e.g. 24");

        Button dispatchBtn = new Button("Dispatch from HQ");
        dispatchBtn.setMaxWidth(Double.MAX_VALUE);
        dispatchBtn.setOnAction(e -> dialog.close());

        content.getChildren().addAll(itemLabel, branchLabel, stockLabel, qtyL, qtyF, dispatchBtn);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    public VBox getView() { return view; }
}
