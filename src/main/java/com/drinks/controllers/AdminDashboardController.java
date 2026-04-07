package com.drinks.controllers;

import com.drinks.components.Icons;
import com.drinks.components.StatCard;
import com.drinks.components.StatusBadge;
import com.drinks.models.Order;
import com.drinks.models.StockItem;
import com.drinks.services.MockDataService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class AdminDashboardController {
    private final VBox view;
    private final AdminLayoutController parentLayout;

    public AdminDashboardController(AdminLayoutController parent) {
        this.parentLayout = parent;
        view = new VBox(24);

        // Stat cards
        HBox statsRow = new HBox(16);
        statsRow.getChildren().addAll(
            new StatCard("Total Revenue Today", "KES 142,850", "\u2191 12% vs yesterday", Color.web("#2563EB")),
            new StatCard("Net Profit", "KES 48,200", "\u2191 8% vs yesterday", Color.web("#059669")),
            new StatCard("Orders Today", "184", "\u2191 23 vs yesterday", Color.web("#D97706")),
            new StatCard("Losses / Returns", "KES 4,200", "\u2191 2 damaged returns", Color.web("#DC2626"))
        );
        for (Node card : statsRow.getChildren()) {
            HBox.setHgrow(card, Priority.ALWAYS);
        }
        view.getChildren().add(statsRow);

        // Revenue by Branch + Profit/Loss
        HBox midRow = new HBox(24);
        midRow.getChildren().addAll(createRevenueChart(), createProfitLossTable());
        for (Node n : midRow.getChildren()) {
            HBox.setHgrow(n, Priority.ALWAYS);
        }
        view.getChildren().add(midRow);

        // Orders dispatched + Stock alerts
        HBox bottomRow = new HBox(24);
        bottomRow.getChildren().addAll(createOrdersDispatchedTable(), createStockAlerts());
        for (Node n : bottomRow.getChildren()) {
            HBox.setHgrow(n, Priority.ALWAYS);
        }
        view.getChildren().add(bottomRow);
    }

    private VBox createRevenueChart() {
        VBox card = new VBox(12);
        card.getStyleClass().add("ds-card");

        Label title = new Label("Revenue by Branch");
        title.getStyleClass().add("ds-heading");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("KES");
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setPrefHeight(200);
        chart.getStyleClass().add("chart-transparent");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Nakuru", 45000));
        series.getData().add(new XYChart.Data<>("Mombasa", 75000));
        series.getData().add(new XYChart.Data<>("Kisumu", 30000));
        series.getData().add(new XYChart.Data<>("HQ", 90000));
        chart.getData().add(series);

        card.getChildren().addAll(title, chart);
        return card;
    }

    private VBox createProfitLossTable() {
        VBox card = new VBox(0);
        card.getStyleClass().add("ds-card");

        Label title = new Label("Profit / Loss per Branch");
        title.getStyleClass().add("ds-heading");
        title.setPadding(new Insets(12, 0, 12, 0));

        TableView<ProfitLossRow> table = new TableView<>();
        table.getStyleClass().add("table-view");

        TableColumn<ProfitLossRow, String> colBranch = new TableColumn<>("Branch");
        colBranch.setCellValueFactory(f -> f.getValue().branch);
        colBranch.setPrefWidth(80);

        TableColumn<ProfitLossRow, String> colRevenue = new TableColumn<>("Revenue");
        colRevenue.setCellValueFactory(f -> f.getValue().revenue);
        colRevenue.setPrefWidth(90);

        TableColumn<ProfitLossRow, String> colCost = new TableColumn<>("Cost");
        colCost.setCellValueFactory(f -> f.getValue().cost);
        colCost.setPrefWidth(80);

        TableColumn<ProfitLossRow, String> colProfit = new TableColumn<>("Profit");
        colProfit.setCellValueFactory(f -> f.getValue().profit);
        colProfit.setPrefWidth(90);

        TableColumn<ProfitLossRow, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(f -> f.getValue().status);
        colStatus.setPrefWidth(80);
        colStatus.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setGraphic(null); }
                else setGraphic(new StatusBadge(item));
            }
        });

        table.getColumns().addAll(colBranch, colRevenue, colCost, colProfit, colStatus);

        table.getItems().addAll(
            new ProfitLossRow("Nakuru", "45,000", "33,000", "+12,000", "OK"),
            new ProfitLossRow("Mombasa", "75,000", "53,000", "+22,000", "OK"),
            new ProfitLossRow("Kisumu", "30,000", "28,000", "+2,000", "Low"),
            new ProfitLossRow("HQ", "90,000", "95,000", "-5,000", "Critical")
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);

        card.getChildren().addAll(title, table);
        return card;
    }

    private VBox createOrdersDispatchedTable() {
        VBox card = new VBox(0);
        card.getStyleClass().add("ds-card");

        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(12, 0, 12, 0));
        header.getChildren().addAll(
            Icons.truck(16, Color.web("#2563EB")),
            new Label("Orders Dispatched Today") {{ getStyleClass().add("ds-heading"); }}
        );

        TableView<Order> table = new TableView<>();
        table.getStyleClass().add("table-view");

        TableColumn<Order, String> colId = new TableColumn<>("#");
        colId.setCellValueFactory(f -> f.getValue().idProperty());
        colId.setPrefWidth(90);
        colId.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); }
                else { setText(item); getStyleClass().add("mono"); }
            }
        });

        TableColumn<Order, String> colBranch = new TableColumn<>("To Branch");
        colBranch.setCellValueFactory(f -> f.getValue().branchProperty());
        colBranch.setPrefWidth(80);

        TableColumn<Order, String> colItems = new TableColumn<>("Items");
        colItems.setCellValueFactory(f -> f.getValue().itemsProperty());
        colItems.setPrefWidth(120);

        TableColumn<Order, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(f -> f.getValue().statusProperty());
        colStatus.setPrefWidth(100);
        colStatus.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setGraphic(null); }
                else setGraphic(new StatusBadge(item));
            }
        });

        TableColumn<Order, String> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(f -> f.getValue().dateProperty());
        colTime.setPrefWidth(120);

        table.getColumns().addAll(colId, colBranch, colItems, colStatus, colTime);

        var orders = MockDataService.getInstance().getAdminOrders().filtered(o ->
            o.getDate().startsWith("24 Oct"));
        table.getItems().addAll(orders);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);

        HBox viewLinkRow = new HBox(4);
        viewLinkRow.setAlignment(Pos.CENTER_LEFT);
        Label viewLink = new Label("View Full Tracking");
        viewLink.getStyleClass().add("dashboard-view-link");
        viewLinkRow.getChildren().addAll(viewLink, Icons.arrowRight(14, Color.web("#2563EB")));
        viewLinkRow.setCursor(javafx.scene.Cursor.HAND);
        viewLinkRow.setPadding(new Insets(12, 0, 0, 0));
        viewLinkRow.setOnMouseClicked(e -> {
            if (parentLayout != null) {
                parentLayout.navigateTo("Order Tracking");
            }
        });

        card.getChildren().addAll(header, table, viewLinkRow);
        return card;
    }

    private VBox createStockAlerts() {
        VBox card = new VBox(12);
        card.getStyleClass().add("ds-card");

        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(12, 0, 12, 0));
        header.getChildren().addAll(
            Icons.alertTriangle(16, Color.web("#D97706")),
            new Label("Stock Alerts") {{ getStyleClass().add("ds-heading"); }}
        );

        VBox alertBox = new VBox(8);
        alertBox.getStyleClass().add("alert-box");
        HBox alertContent = new HBox(6);
        alertContent.setAlignment(Pos.CENTER_LEFT);
        alertContent.getChildren().addAll(
            Icons.alertCircle(14, Color.web("#E11D48")),
            new Label("3 items require restocking") {{ getStyleClass().add("text"); }}
        );
        alertBox.getChildren().add(alertContent);

        VBox items = new VBox(8);
        for (StockItem s : MockDataService.getInstance().getStockItems().filtered(i -> !i.getStatus().equals("OK"))) {
            HBox row = new HBox();
            row.getStyleClass().add("stock-alert-row");
            row.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(row, Priority.ALWAYS);

            Label name = new Label(s.getName());
            name.getStyleClass().add("stock-alert-name");
            Label detail = new Label(s.getBranch() + " \u00B7 " + s.getStock() + " units");
            detail.getStyleClass().add("stock-alert-detail");
            VBox info = new VBox(2, name, detail);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            row.getChildren().addAll(info, spacer, new StatusBadge(s.getStatus()));
            items.getChildren().add(row);
        }

        card.getChildren().addAll(header, alertBox, items);
        return card;
    }

    public VBox getView() { return view; }

    public static class ProfitLossRow {
        public final javafx.beans.property.StringProperty branch, revenue, cost, profit, status;
        public ProfitLossRow(String b, String r, String c, String p, String s) {
            branch = new javafx.beans.property.SimpleStringProperty(b);
            revenue = new javafx.beans.property.SimpleStringProperty(r);
            cost = new javafx.beans.property.SimpleStringProperty(c);
            profit = new javafx.beans.property.SimpleStringProperty(p);
            status = new javafx.beans.property.SimpleStringProperty(s);
        }
    }
}
