package com.drinks.controllers;

import com.drinks.components.StatusBadge;
import com.drinks.models.Order;
import com.drinks.services.MockDataService;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class OrderManagementController {
    private final VBox view;
    private final TableView<Order> table;
    private final ObservableList<Order> allOrders;

    public OrderManagementController() {
        view = new VBox(16);
        allOrders = MockDataService.getInstance().getAdminOrders();

        Label title = new Label("Order Management");
        title.getStyleClass().add("ds-title");

        // Filters
        HBox filters = createFilters();

        // Table
        table = createTable();

        view.getChildren().addAll(title, filters, table);
    }

    private HBox createFilters() {
        HBox row = new HBox(12);
        row.getStyleClass().add("ds-card");
        row.setAlignment(Pos.CENTER_LEFT);

        Label searchL = new Label("Search");
        searchL.getStyleClass().add("ds-label");
        TextField searchField = new TextField();
        searchField.setPromptText("Search by customer, order ID...");
        VBox searchBox = new VBox(4, searchL, searchField);
        HBox.setHgrow(searchBox, Priority.ALWAYS);

        Label branchL = new Label("Branch");
        branchL.getStyleClass().add("ds-label");
        ComboBox<String> branchField = new ComboBox<>();
        branchField.getItems().addAll("All Branches", "Nakuru", "Mombasa", "Kisumu", "HQ");
        branchField.setValue("All Branches");
        VBox branchBox = new VBox(4, branchL, branchField);

        Label statusL = new Label("Status");
        statusL.getStyleClass().add("ds-label");
        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("All Statuses", "Pending", "Processing", "Dispatched", "In Transit", "Confirmed", "Received", "Cancelled");
        statusField.setValue("All Statuses");
        VBox statusBox = new VBox(4, statusL, statusField);

        Label dateL = new Label("Date Range");
        dateL.getStyleClass().add("ds-label");
        DatePicker dateField = new DatePicker();
        VBox dateBox = new VBox(4, dateL, dateField);

        row.getChildren().addAll(searchBox, branchBox, statusBox, dateBox);

        searchField.textProperty().addListener((obs, o, n) -> applyFilters(searchField, branchField, statusField));
        branchField.valueProperty().addListener((obs, o, n) -> applyFilters(searchField, branchField, statusField));
        statusField.valueProperty().addListener((obs, o, n) -> applyFilters(searchField, branchField, statusField));

        return row;
    }

    private void applyFilters(TextField search, ComboBox<String> branch, ComboBox<String> status) {
        String s = search.getText().toLowerCase();
        String b = branch.getValue();
        String st = status.getValue();

        table.getItems().clear();
        for (Order o : allOrders) {
            boolean matchSearch = s.isEmpty() || o.getCustomer().toLowerCase().contains(s) || o.getId().toLowerCase().contains(s) || o.getItems().toLowerCase().contains(s);
            boolean matchBranch = b.equals("All Branches") || o.getBranch().equals(b);
            boolean matchStatus = st.equals("All Statuses") || o.getStatus().equals(st);
            if (matchSearch && matchBranch && matchStatus) {
                table.getItems().add(o);
            }
        }
    }

    private TableView<Order> createTable() {
        TableView<Order> tbl = new TableView<>();
        tbl.getStyleClass().add("table-view");

        TableColumn<Order, String> colId = new TableColumn<>("Order ID");
        colId.setCellValueFactory(f -> f.getValue().idProperty());
        colId.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("mono"); }
            }
        });

        TableColumn<Order, String> colCust = new TableColumn<>("Customer");
        colCust.setCellValueFactory(f -> f.getValue().customerProperty());

        TableColumn<Order, String> colBranch = new TableColumn<>("Branch");
        colBranch.setCellValueFactory(f -> f.getValue().branchProperty());

        TableColumn<Order, String> colItems = new TableColumn<>("Items");
        colItems.setCellValueFactory(f -> f.getValue().itemsProperty());
        colItems.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        TableColumn<Order, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(f -> f.getValue().totalProperty());
        colTotal.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText("KES " + item); getStyleClass().add("amount"); }
            }
        });

        TableColumn<Order, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(f -> f.getValue().statusProperty());
        colStatus.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else setGraphic(new StatusBadge(item));
            }
        });

        TableColumn<Order, String> colDate = new TableColumn<>("Date/Time");
        colDate.setCellValueFactory(f -> f.getValue().dateProperty());
        colDate.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        TableColumn<Order, Void> colActions = new TableColumn<>("Actions");
        colActions.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Button btn = new Button("View");
                    btn.getStyleClass().add("btn-link");
                    setGraphic(btn);
                }
            }
        });

        tbl.getColumns().addAll(colId, colCust, colBranch, colItems, colTotal, colStatus, colDate, colActions);
        tbl.setItems(allOrders);
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbl.setPrefHeight(400);

        return tbl;
    }

    public VBox getView() { return view; }
}
