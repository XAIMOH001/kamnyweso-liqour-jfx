package com.drinks.controllers;

import com.drinks.components.Icons;
import com.drinks.components.OTPInput;
import com.drinks.models.Drink;
import com.drinks.services.MockDataService;
import com.drinks.utils.PinValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class OrderToHQController {
    private final VBox view;
    private final ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
    private final Map<String, Drink> itemMap = new HashMap<>();

    public OrderToHQController() {
        view = new VBox(20);

        Label title = new Label("Order Stock from HQ");
        title.getStyleClass().add("ds-title");
        Label subtitle = new Label("Request stock replenishment for Nakuru Branch");
        subtitle.getStyleClass().add("ds-subtitle");

        TextField searchField = new TextField();
        searchField.setPromptText("Search items by name or brand...");

        GridPane itemGrid = new GridPane();
        itemGrid.setHgap(12);
        itemGrid.setVgap(12);
        var drinks = MockDataService.getInstance().getDrinks();
        int col = 0, row = 0;
        for (Drink d : drinks) {
            itemMap.put(d.getName(), d);
            itemGrid.add(createItemCard(d), col, row);
            col++;
            if (col >= 3) { col = 0; row++; }
        }

        VBox orderPanel = createOrderPanel();
        VBox leftPanel = new VBox(16, searchField, itemGrid);
        HBox mainContent = new HBox(24, leftPanel, orderPanel);
        HBox.setHgrow(leftPanel, Priority.ALWAYS);

        view.getChildren().addAll(title, subtitle, mainContent);

        searchField.textProperty().addListener((obs, old, newVal) -> {
            itemGrid.getChildren().clear();
            int c = 0, r = 0;
            String lower = newVal.toLowerCase();
            for (Drink d : drinks) {
                if (d.getName().toLowerCase().contains(lower) || d.getBrand().toLowerCase().contains(lower)) {
                    itemGrid.add(createItemCard(d), c, r);
                    c++;
                    if (c >= 3) { c = 0; r++; }
                }
            }
        });
    }

    private VBox createItemCard(Drink drink) {
        VBox card = new VBox(0); 
        card.getStyleClass().add("drink-card");
        card.setMaxWidth(220);
        card.setCursor(javafx.scene.Cursor.HAND);

        StackPane imgContainer = new StackPane();
        imgContainer.getStyleClass().add("drink-img-container");
        imgContainer.setPrefHeight(180);
        imgContainer.setMinHeight(180);

        try {
            String path = "/com/drinks/assets/images/" + drink.getImage().replace("/", "");
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView iv = new ImageView(img);
            iv.setFitHeight(140);
            iv.setFitWidth(140);
            iv.setPreserveRatio(true);
            imgContainer.getChildren().add(iv);
        } catch (Exception e) {
            Region placeholder = new Region();
            placeholder.getStyleClass().add("drink-img-placeholder");
            imgContainer.getChildren().add(placeholder);
        }

        VBox content = new VBox(4);
        content.setPadding(new Insets(12, 16, 16, 16));
        
        Label name = new Label(drink.getName()); 
        name.getStyleClass().add("drink-name");
        Label brand = new Label(drink.getBrand()); 
        brand.getStyleClass().add("drink-brand");
        
        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER_LEFT); 
        bottom.setSpacing(8);
        Label price = new Label("KES " + String.format("%,d", drink.getCasePrice())); 
        price.getStyleClass().add("drink-price");
        Label units = new Label("/case (" + drink.getCaseUnits() + ")"); 
        units.getStyleClass().add("drink-case-unit");
        
        Region spacer = new Region(); 
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        bottom.getChildren().addAll(price, spacer, units);
        content.getChildren().addAll(name, brand, bottom);
        
        card.getChildren().addAll(imgContainer, content);
        card.setOnMouseClicked(e -> addItem(drink));
        
        return card;
    }

    private void addItem(Drink drink) {
        for (OrderItem item : orderItems) {
            if (item.name.equals(drink.getName())) {
                item.cases++;
                return;
            }
        }
        orderItems.add(new OrderItem(drink.getName(), 1, drink.getCasePrice(), drink.getCaseUnits()));
    }

    private VBox createOrderPanel() {
        VBox panel = new VBox(0);
        panel.getStyleClass().add("ds-card");
        panel.setPadding(new Insets(0));
        panel.setPrefWidth(320);
        panel.setMaxWidth(320);

        HBox header = new HBox();
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setAlignment(Pos.CENTER_LEFT);
        Label headerTitle = new Label("Stock Order");
        headerTitle.setGraphic(Icons.pkg(16, Color.web("#0F172A")));
        headerTitle.getStyleClass().add("ds-heading");
        Label badge = new Label(orderItems.size() + " Items");
        badge.getStyleClass().add("badge-primary");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(headerTitle, spacer, badge);

        VBox itemsBox = new VBox(8);
        itemsBox.setPadding(new Insets(16, 20, 16, 20));
        ScrollPane scroll = new ScrollPane(itemsBox);
        scroll.getStyleClass().add("content-scroll");
        scroll.setFitToWidth(true);
        scroll.setMaxHeight(300);

        VBox footer = new VBox(12);
        footer.getStyleClass().add("cart-footer");

        Label casesLabel = new Label();
        casesLabel.getStyleClass().add("cart-subtotal");
        Label unitsLabel = new Label();
        unitsLabel.getStyleClass().add("cart-subtotal");
        Label totalLabel = new Label();
        totalLabel.getStyleClass().add("cart-total");

        Label notesL = new Label("Order Notes");
        notesL.getStyleClass().add("ds-label");
        TextArea notesF = new TextArea();
        notesF.setPromptText("Any special instructions for HQ...");
        notesF.setPrefRowCount(2);

        Button submitBtn = new Button("Submit Order to HQ");
        submitBtn.setGraphic(Icons.truck(14, Color.WHITE));
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        HBox lockRow = new HBox(4);
        lockRow.setAlignment(Pos.CENTER);
        lockRow.getChildren().addAll(
            Icons.lock(12, Color.web("#6B7280")),
            new Label("Removing items requires supervisor PIN") {{ getStyleClass().add("cart-lock-note"); }}
        );

        footer.getChildren().addAll(casesLabel, unitsLabel, totalLabel, notesL, notesF, submitBtn, lockRow);
        panel.getChildren().addAll(header, scroll, footer);

        orderItems.addListener((javafx.collections.ListChangeListener<OrderItem>) c -> updateOrder(itemsBox, casesLabel, unitsLabel, totalLabel, badge));
        updateOrder(itemsBox, casesLabel, unitsLabel, totalLabel, badge);

        return panel;
    }

    private void updateOrder(VBox itemsBox, Label casesLabel, Label unitsLabel, Label totalLabel, Label badge) {
        itemsBox.getChildren().clear();
        int totalCases = 0, totalUnits = 0, subtotal = 0;
        for (OrderItem item : orderItems) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);

            Button deleteBtn = new Button();
            deleteBtn.setGraphic(Icons.trash(14, Color.web("#9CA3AF")));
            deleteBtn.getStyleClass().add("btn-link");
            deleteBtn.setOnAction(e -> showPinModal(item));

            Label name = new Label(item.name);
            name.getStyleClass().add("name");
            Label detail = new Label(item.cases + " case(s) \u00D7 KES " + String.format("%,d", item.casePrice));
            detail.getStyleClass().add("detail");
            VBox info = new VBox(2, name, detail);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label total = new Label("KES " + String.format("%,d", item.cases * item.casePrice));
            total.getStyleClass().add("total");

            row.getChildren().addAll(deleteBtn, info, spacer, total);
            itemsBox.getChildren().add(row);

            totalCases += item.cases;
            totalUnits += item.cases * item.caseUnits;
            subtotal += item.cases * item.casePrice;
        }

        if (orderItems.isEmpty()) {
            Label empty = new Label("No items added yet");
            empty.getStyleClass().add("cart-empty");
            empty.setAlignment(Pos.CENTER);
            itemsBox.getChildren().add(empty);
        }

        casesLabel.setText("Total Cases: " + totalCases);
        unitsLabel.setText("Total Units: " + totalUnits);
        totalLabel.setText("Total Cost: KES " + String.format("%,d", subtotal));
        badge.setText(orderItems.size() + " Items");
    }

    private void showPinModal(OrderItem target) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Supervisor PIN");
        VBox content = new VBox(16);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(8));

        content.getChildren().add(Icons.shieldCheck(40, Color.web("#2563EB")));
        Label title = new Label("Supervisor PIN");
        title.getStyleClass().add("ds-heading");
        Label desc = new Label("Required to remove items from order");
        desc.getStyleClass().add("ds-text-muted");

        OTPInput otp = new OTPInput(4);
        Label warning = new Label("3 wrong attempts flags this action to admin");
        warning.getStyleClass().add("danger-text");
        Label attemptsLabel = new Label("Attempts remaining: " + PinValidator.getRemainingAttempts());
        attemptsLabel.getStyleClass().add("ds-text-muted");

        HBox btns = new HBox(8);
        Button cancel = new Button("Cancel");
        cancel.getStyleClass().add("btn-secondary");
        cancel.setMaxWidth(Double.MAX_VALUE); HBox.setHgrow(cancel, Priority.ALWAYS);
        cancel.setOnAction(e -> dialog.close());
        Button confirm = new Button("Confirm");
        confirm.setMaxWidth(Double.MAX_VALUE); HBox.setHgrow(confirm, Priority.ALWAYS);
        confirm.setOnAction(e -> {
            String pin = otp.getValue();
            if (PinValidator.isLockedOut()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Access Locked");
                alert.setHeaderText(null);
                alert.setContentText("Too many failed attempts. This action has been flagged to admin.");
                alert.showAndWait();
                return;
            }
            if (PinValidator.validate(pin)) {
                PinValidator.resetAttempts();
                orderItems.remove(target);
                dialog.setResult(true);
            } else {
                attemptsLabel.setText("Attempts remaining: " + PinValidator.getRemainingAttempts());
                if (PinValidator.isLockedOut()) {
                    warning.setText("ACCESS LOCKED - Action flagged to admin");
                    confirm.setDisable(true);
                }
                otp.clear();
                otp.requestFocus();
            }
        });
        btns.getChildren().addAll(cancel, confirm);

        content.getChildren().addAll(title, desc, otp, attemptsLabel, btns, warning);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.setResultConverter(b -> b == ButtonType.OK);
        dialog.showAndWait();
    }

    public VBox getView() { return view; }

    public static class OrderItem {
        public String name;
        public int cases;
        public int casePrice;
        public int caseUnits;
        public OrderItem(String name, int cases, int casePrice, int caseUnits) {
            this.name = name;
            this.cases = cases;
            this.casePrice = casePrice;
            this.caseUnits = caseUnits;
        }
    }
}
