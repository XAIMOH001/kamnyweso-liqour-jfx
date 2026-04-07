package com.drinks.controllers;

import com.drinks.components.StatusBadge;
import com.drinks.models.Customer;
import com.drinks.services.MockDataService;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LoyaltyController {
    private final VBox view;
    private final ObservableList<Customer> customers;
    private Customer selectedCustomer;

    public LoyaltyController() {
        view = new VBox(20);
        customers = MockDataService.getInstance().getCustomers();
        selectedCustomer = customers.get(0);

        Label title = new Label("Loyalty Programme");
        title.getStyleClass().add("ds-title");

        HBox mainRow = new HBox(24);
        VBox leftPanel = new VBox(16);
        leftPanel.getChildren().addAll(createSearchCard(), createLoyaltyCard(), createPurchaseHistory());
        VBox rightPanel = new VBox(16);
        rightPanel.getChildren().addAll(createRegisterCard(), createPointsRules());

        mainRow.getChildren().addAll(leftPanel, rightPanel);
        HBox.setHgrow(leftPanel, Priority.ALWAYS);

        view.getChildren().addAll(title, mainRow);
    }

    private VBox createSearchCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("ds-card");

        HBox row = new HBox(12);
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name, phone or Card ID...");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> {
            String s = searchField.getText().toLowerCase();
            for (Customer c : customers) {
                if (c.getName().toLowerCase().contains(s) || c.getPhone().contains(s) || c.getCardId().toLowerCase().contains(s)) {
                    selectedCustomer = c;
                    refreshLoyaltyCard();
                    break;
                }
            }
        });
        row.getChildren().addAll(searchField, searchBtn);
        card.getChildren().add(row);
        return card;
    }

    private VBox loyaltyCardContainer;

    private VBox createLoyaltyCard() {
        VBox card = new VBox(16);
        card.getStyleClass().add("loyalty-card");

        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label tierBadge = new Label(selectedCustomer.getTier() + " Tier");
        tierBadge.getStyleClass().add("tier-badge");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        VBox cardIdInfo = new VBox(4,
            new Label("Card ID") {{ getStyleClass().add("card-id-label"); }},
            new Label(selectedCustomer.getCardId()) {{ getStyleClass().add("card-id"); }}
        );
        cardIdInfo.setAlignment(Pos.CENTER_RIGHT);
        topRow.getChildren().addAll(tierBadge, spacer, cardIdInfo);

        Label name = new Label(selectedCustomer.getName());
        name.getStyleClass().add("name");
        Label phone = new Label(selectedCustomer.getPhone());
        phone.getStyleClass().add("phone");

        HBox bottomRow = new HBox();
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        VBox pointsInfo = new VBox(4,
            new Label("Points Balance") {{ getStyleClass().add("points-label"); }},
            new Label(String.format("%,d pts", selectedCustomer.getPoints())) {{ getStyleClass().add("points-value"); }}
        );

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        VBox progressInfo = new VBox(6);
        progressInfo.setPrefWidth(192);
        HBox progressHeader = new HBox();
        progressHeader.setAlignment(Pos.CENTER_LEFT);
        Label progressLabel = new Label("Progress to " + selectedCustomer.getNextTier());
        progressLabel.getStyleClass().add("progress-label");
        Label progressPct = new Label(selectedCustomer.getProgress() + "%");
        progressPct.getStyleClass().add("progress-label");
        Region pSpacer = new Region();
        HBox.setHgrow(pSpacer, Priority.ALWAYS);
        progressHeader.getChildren().addAll(progressLabel, pSpacer, progressPct);

        ProgressBar progressBar = new ProgressBar(selectedCustomer.getProgress() / 100.0);
        progressBar.setPrefHeight(8);

        Label remaining = new Label(String.format("%,d / %,d pts \u00B7 %,d pts to go",
            selectedCustomer.getPoints(), selectedCustomer.getTarget(),
            selectedCustomer.getTarget() - selectedCustomer.getPoints()));
        remaining.getStyleClass().add("loyalty-remaining");

        progressInfo.getChildren().addAll(progressHeader, progressBar, remaining);
        bottomRow.getChildren().addAll(pointsInfo, spacer2, progressInfo);

        loyaltyCardContainer = new VBox(16, topRow, name, phone, bottomRow);
        card.getChildren().add(loyaltyCardContainer);
        return card;
    }

    private void refreshLoyaltyCard() {
        if (loyaltyCardContainer == null) return;
        loyaltyCardContainer.getChildren().clear();

        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label tierBadge = new Label(selectedCustomer.getTier() + " Tier");
        tierBadge.getStyleClass().add("tier-badge");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        VBox cardIdInfo = new VBox(4,
            new Label("Card ID") {{ getStyleClass().add("card-id-label"); }},
            new Label(selectedCustomer.getCardId()) {{ getStyleClass().add("card-id"); }}
        );
        cardIdInfo.setAlignment(Pos.CENTER_RIGHT);
        topRow.getChildren().addAll(tierBadge, spacer, cardIdInfo);

        Label name = new Label(selectedCustomer.getName());
        name.getStyleClass().add("name");
        Label phone = new Label(selectedCustomer.getPhone());
        phone.getStyleClass().add("phone");

        HBox bottomRow = new HBox();
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        VBox pointsInfo = new VBox(4,
            new Label("Points Balance") {{ getStyleClass().add("points-label"); }},
            new Label(String.format("%,d pts", selectedCustomer.getPoints())) {{ getStyleClass().add("points-value"); }}
        );
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        VBox progressInfo = new VBox(6);
        progressInfo.setPrefWidth(192);
        HBox progressHeader = new HBox();
        progressHeader.setAlignment(Pos.CENTER_LEFT);
        Label progressLabel = new Label("Progress to " + selectedCustomer.getNextTier());
        progressLabel.getStyleClass().add("progress-label");
        Label progressPct = new Label(selectedCustomer.getProgress() + "%");
        progressPct.getStyleClass().add("progress-label");
        Region pSpacer = new Region();
        HBox.setHgrow(pSpacer, Priority.ALWAYS);
        progressHeader.getChildren().addAll(progressLabel, pSpacer, progressPct);
        ProgressBar progressBar = new ProgressBar(selectedCustomer.getProgress() / 100.0);
        progressBar.setPrefHeight(8);
        Label remaining = new Label(String.format("%,d / %,d pts \u00B7 %,d pts to go",
            selectedCustomer.getPoints(), selectedCustomer.getTarget(),
            selectedCustomer.getTarget() - selectedCustomer.getPoints()));
        remaining.getStyleClass().add("loyalty-remaining");
        progressInfo.getChildren().addAll(progressHeader, progressBar, remaining);
        bottomRow.getChildren().addAll(pointsInfo, spacer2, progressInfo);

        loyaltyCardContainer.getChildren().addAll(topRow, name, phone, bottomRow);
    }

    private VBox createPurchaseHistory() {
        VBox card = new VBox(0);
        card.getStyleClass().add("ds-card");

        Label header = new Label("Purchase History");
        header.getStyleClass().add("ds-heading");
        header.setPadding(new Insets(12, 0, 12, 0));

        TableView<PurchaseRow> table = new TableView<>();
        table.getStyleClass().add("table-view");

        TableColumn<PurchaseRow, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(f -> f.getValue().date);
        colDate.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        TableColumn<PurchaseRow, String> colItems = new TableColumn<>("Items Bought");
        colItems.setCellValueFactory(f -> f.getValue().items);

        TableColumn<PurchaseRow, String> colAmount = new TableColumn<>("Amount");
        colAmount.setCellValueFactory(f -> f.getValue().amount);
        colAmount.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText("KES " + item); getStyleClass().add("amount"); setAlignment(Pos.CENTER_RIGHT); }
            }
        });

        TableColumn<PurchaseRow, String> colPoints = new TableColumn<>("Points");
        colPoints.setCellValueFactory(f -> f.getValue().points);
        colPoints.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else {
                    Label badge = new Label(item + " pts");
                    badge.getStyleClass().add("badge-loyalty");
                    setGraphic(badge);
                    setAlignment(Pos.CENTER_RIGHT);
                }
            }
        });

        table.getColumns().addAll(colDate, colItems, colAmount, colPoints);
        table.getItems().addAll(
            new PurchaseRow("24 Oct 2023", "Tusker x 12, Guinness x 4", "4,800", "+480"),
            new PurchaseRow("20 Oct 2023", "Coke x 24, Water x 12", "3,200", "+320"),
            new PurchaseRow("15 Oct 2023", "White Cap x 6", "1,680", "+168"),
            new PurchaseRow("10 Oct 2023", "Stoney x 12, Fanta x 6", "1,800", "+180")
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);

        card.getChildren().addAll(header, table);
        return card;
    }

    private VBox createRegisterCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("ds-card");

        Label title = new Label("Register New Customer");
        title.getStyleClass().add("ds-heading");

        Label nameL = new Label("Full Name"); nameL.getStyleClass().add("ds-label");
        TextField nameF = new TextField(); nameF.setPromptText("Full Name");
        Label phoneL = new Label("Phone Number"); phoneL.getStyleClass().add("ds-label");
        TextField phoneF = new TextField(); phoneF.setPromptText("+254 7XX");
        Label tierL = new Label("Tier"); tierL.getStyleClass().add("ds-label");
        ComboBox<String> tierF = new ComboBox<>();
        tierF.getItems().addAll("Bronze Tier (Default)", "Silver Tier", "Gold Tier");
        tierF.setValue("Bronze Tier (Default)");

        Button registerBtn = new Button("Register & Issue Card");
        registerBtn.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(title, nameL, nameF, phoneL, phoneF, tierL, tierF, registerBtn);
        return card;
    }

    private VBox createPointsRules() {
        VBox card = new VBox(12);
        card.getStyleClass().add("loyalty-rules-card");

        Label title = new Label("How Points Are Earned");
        title.getStyleClass().add("loyalty-rules-title");

        String[][] rules = {
            {"Per KES 10 Spent", "+1 pt"},
            {"Premium Drinks", "+5 pts/item"},
            {"Soft Drinks", "+2 pts/item"},
            {"Water / Non-Alc", "+1 pt/item"}
        };

        VBox rulesBox = new VBox(0);
        for (String[] rule : rules) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("points-rule");
            Label label = new Label(rule[0]);
            label.getStyleClass().add("loyalty-rule-label");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            Label value = new Label(rule[1]);
            value.getStyleClass().add("loyalty-rule-value");
            row.getChildren().addAll(label, spacer, value);
            rulesBox.getChildren().add(row);
        }

        card.getChildren().addAll(title, rulesBox);
        return card;
    }

    public VBox getView() { return view; }

    public static class PurchaseRow {
        public final javafx.beans.property.StringProperty date, items, amount, points;
        public PurchaseRow(String d, String i, String a, String p) {
            date = new javafx.beans.property.SimpleStringProperty(d);
            items = new javafx.beans.property.SimpleStringProperty(i);
            amount = new javafx.beans.property.SimpleStringProperty(a);
            points = new javafx.beans.property.SimpleStringProperty(p);
        }
    }
}
