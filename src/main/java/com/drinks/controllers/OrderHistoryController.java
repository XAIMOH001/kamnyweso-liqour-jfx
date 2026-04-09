package com.drinks.controllers;

import com.drinks.components.StatusBadge;
import com.drinks.models.Order;
import com.drinks.services.MockDataService;
import com.drinks.utils.PrintUtils;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class OrderHistoryController {
    private final VBox view;
    private final TableView<Order> table;
    private final ObservableList<Order> allOrders;

    public OrderHistoryController() {
        view = new VBox(16);
        allOrders = MockDataService.getInstance().getBranchOrders();

        Label title = new Label("Order History \u2014 Nakuru Branch");
        title.getStyleClass().add("ds-title");

        HBox filters = createFilters();
        table = createTable();

        view.getChildren().addAll(title, filters, table);
    }

    private HBox createFilters() {
        HBox row = new HBox(12);
        row.getStyleClass().add("ds-card");
        row.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by customer, order ID...");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("All Statuses", "Pending", "Processing", "Confirmed", "Received");
        statusField.setValue("All Statuses");

        DatePicker dateField = new DatePicker();

        row.getChildren().addAll(searchField, statusField, dateField);

        searchField.textProperty().addListener((obs, o, n) -> applyFilters(searchField, statusField));
        statusField.valueProperty().addListener((obs, o, n) -> applyFilters(searchField, statusField));

        return row;
    }

    private void applyFilters(TextField search, ComboBox<String> status) {
        String s = search.getText().toLowerCase();
        String st = status.getValue();
        table.getItems().clear();
        for (Order o : allOrders) {
            boolean matchSearch = s.isEmpty() || o.getCustomer().toLowerCase().contains(s) || o.getId().toLowerCase().contains(s) || o.getItems().toLowerCase().contains(s);
            boolean matchStatus = st.equals("All Statuses") || o.getStatus().equals(st);
            if (matchSearch && matchStatus) table.getItems().add(o);
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

        TableColumn<Order, String> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(f -> f.getValue().dateProperty());
        colTime.setCellFactory(c -> new TableCell<>() {
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
                    Button btn = new Button("View Receipt");
                    btn.getStyleClass().add("btn-link");
                    btn.setOnAction(e -> showReceipt(getTableView().getItems().get(getIndex())));
                    setGraphic(btn);
                }
            }
        });

        tbl.getColumns().addAll(colId, colCust, colItems, colTotal, colStatus, colTime, colActions);
        tbl.setItems(allOrders);
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbl.setPrefHeight(400);

        return tbl;
    }

    private void showReceipt(Order order) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Receipt");
        VBox content = new VBox(12);
        content.setPadding(new Insets(8));

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleInfo = new VBox(2,
            new Label("Receipt") {{ getStyleClass().add("ds-heading"); }},
            new Label(order.getId()) {{ getStyleClass().addAll("ds-text-muted", "ds-mono"); }}
        );
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(titleInfo, spacer);

        Separator sep = new Separator();

        VBox details = new VBox(8,
            createRow("Branch", "Nakuru"),
            createRow("Staff", order.getStaff()),
            createRow("Customer", order.getCustomer()),
            sep,
            new Label("Items: " + order.getItems()),
            sep,
            new HBox(new Label("Total") {{ getStyleClass().add("ds-text-muted"); }}, new Region() {{ HBox.setHgrow(this, Priority.ALWAYS); }}, new Label("KES " + order.getTotal()) {{ getStyleClass().add("ds-value"); }}),
            new HBox(new Label("Loyalty Points") {{ getStyleClass().add("ds-text-small"); }}, new Label("+" + order.getLoyalty() + " pts") {{ getStyleClass().add("badge-loyalty"); }}),
            new Label(order.getDate()) {{ getStyleClass().add("ds-text-muted"); setAlignment(Pos.CENTER); }}
        );

        HBox actionBtns = new HBox(12);
        actionBtns.setAlignment(Pos.CENTER_RIGHT);
        Button printBtn = new Button("Print Receipt");
        printBtn.getStyleClass().add("btn-outline");
        printBtn.setOnAction(e -> {
            VBox receiptNode = new VBox(12, 
                new Label("Kamnyweso Liquor Store") {{ getStyleClass().add("ds-heading"); }},
                new Label(order.getId()) {{ getStyleClass().addAll("ds-text-muted", "ds-mono"); }},
                new Separator(),
                createRow("Branch", "Nakuru"),
                createRow("Staff", order.getStaff()),
                createRow("Customer", order.getCustomer()),
                new Label("Items: " + order.getItems()),
                new Separator(),
                new HBox(new Label("Total") {{ getStyleClass().add("ds-text-muted"); }}, new Region() {{ HBox.setHgrow(this, Priority.ALWAYS); }}, new Label("KES " + order.getTotal()) {{ getStyleClass().add("ds-value"); }}),
                new HBox(new Label("Loyalty Points") {{ getStyleClass().add("ds-text-small"); }}, new Label("+" + order.getLoyalty() + " pts") {{ getStyleClass().add("badge-loyalty"); }}),
                new Label(order.getDate()) {{ getStyleClass().add("ds-text-muted"); setAlignment(Pos.CENTER); }}
            );
            receiptNode.setPadding(new Insets(16));
            PrintUtils.printReceipt(receiptNode);
        });
        actionBtns.getChildren().add(printBtn);

        content.getChildren().addAll(header, details, actionBtns);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private HBox createRow(String label, String value) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        Label l = new Label(label);
        l.getStyleClass().add("ds-text-muted");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label v = new Label(value);
        v.getStyleClass().add("ds-heading-sm");
        row.getChildren().addAll(l, spacer, v);
        return row;
    }

    public VBox getView() { return view; }
}
