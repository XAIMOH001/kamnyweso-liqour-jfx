package com.drinks.controllers;

import com.drinks.components.Icons;
import com.drinks.components.OTPInput;
import com.drinks.models.Drink;
import com.drinks.services.MockDataService;
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

public class AdminPOSController {
    private final VBox view;
    private final ObservableList<CartItem> cart = FXCollections.observableArrayList();
    private final Map<String, Drink> drinkMap = new HashMap<>();

    public AdminPOSController() {
        view = new VBox(20);

        Label title = new Label("Point of Sale \u2014 HQ Nairobi");
        title.getStyleClass().add("ds-title");

        // Progress bar
        HBox progress = createProgressBar(2);

        // Customer details
        VBox customerCard = createCustomerCard();

        // Search
        TextField searchField = new TextField();
        searchField.setPromptText("Search drinks by name or brand...");
        searchField.getStyleClass().add("text-field");

        // Drink grid
        GridPane drinkGrid = new GridPane();
        drinkGrid.setHgap(12);
        drinkGrid.setVgap(12);

        var drinks = MockDataService.getInstance().getDrinks();
        int col = 0, row = 0;
        for (Drink d : drinks) {
            drinkMap.put(d.getName(), d);
            drinkGrid.add(createDrinkCard(d), col, row);
            col++;
            if (col >= 3) { col = 0; row++; }
        }

        // Cart
        VBox cartPanel = createCartPanel();

        // Layout: left (customer + search + drinks) and right (cart)
        VBox leftPanel = new VBox(16, customerCard, searchField, drinkGrid);
        HBox mainContent = new HBox(24, leftPanel, cartPanel);
        HBox.setHgrow(leftPanel, Priority.ALWAYS);

        view.getChildren().addAll(title, progress, mainContent);

        // Search filter
        searchField.textProperty().addListener((obs, old, newVal) -> {
            drinkGrid.getChildren().clear();
            int c = 0, r = 0;
            String lower = newVal.toLowerCase();
            for (Drink d : drinks) {
                if (d.getName().toLowerCase().contains(lower) || d.getBrand().toLowerCase().contains(lower)) {
                    drinkGrid.add(createDrinkCard(d), c, r);
                    c++;
                    if (c >= 3) { c = 0; r++; }
                }
            }
        });
    }

    private HBox createProgressBar(int currentStep) {
        HBox bar = new HBox(16);
        bar.getStyleClass().add("ds-card");
        bar.setAlignment(Pos.CENTER);
        String[] steps = {"Customer", "Select Drinks", "Review Cart", "Confirm"};
        for (int i = 0; i < steps.length; i++) {
            Label num = new Label(String.valueOf(i + 1));
            num.getStyleClass().addAll("progress-step-num", i + 1 <= currentStep ? "active" : "inactive");
            Label label = new Label(steps[i]);
            label.getStyleClass().addAll("progress-step-label", i + 1 <= currentStep ? "active" : "inactive");
            HBox stepBox = new HBox(6, num, label);
            stepBox.setAlignment(Pos.CENTER);
            bar.getChildren().add(stepBox);
            if (i < steps.length - 1) {
                Region line = new Region();
                line.getStyleClass().addAll("progress-step-line", i + 1 < currentStep ? "active" : "inactive");
                bar.getChildren().add(line);
            }
        }
        return bar;
    }

    private VBox createCustomerCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("ds-card");
        HBox titleRow = new HBox(8);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.getChildren().addAll(
            Icons.users(16, Color.web("#2563EB")),
            new Label("Customer Details") {{ getStyleClass().add("ds-heading"); }}
        );

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(8);

        Label nameL = new Label("Full Name"); nameL.getStyleClass().add("ds-label");
        TextField nameF = new TextField(); nameF.setPromptText("Full Name");
        Label phoneL = new Label("Phone Number"); phoneL.getStyleClass().add("ds-label");
        TextField phoneF = new TextField(); phoneF.setPromptText("+254 7XX");
        Label loyaltyL = new Label("Loyalty Card ID"); loyaltyL.getStyleClass().add("ds-label");
        HBox loyaltyBox = new HBox(8, new TextField() {{ setPromptText("LC-XXXXX"); }}, new Button("Scan") {{ getStyleClass().add("btn-outline"); }});

        form.add(nameL, 0, 0); form.add(nameF, 0, 1);
        form.add(phoneL, 1, 0); form.add(phoneF, 1, 1);
        form.add(loyaltyL, 2, 0); form.add(loyaltyBox, 2, 1);

        card.getChildren().addAll(titleRow, form);
        return card;
    }

    private VBox createDrinkCard(Drink drink) {
        VBox card = new VBox(0); 
        card.getStyleClass().add("drink-card");
        card.setMaxWidth(220);

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
        bottom.setAlignment(Pos.BOTTOM_LEFT); 
        bottom.setSpacing(8);
        Label price = new Label("KES " + drink.getPrice()); 
        price.getStyleClass().add("drink-price");
        
        Region spacer = new Region(); 
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label stock = new Label(drink.getStock() == 0 ? "Out of stock" : drink.getStock() + " units");
        stock.getStyleClass().addAll("drink-stock", drink.getStock() == 0 ? "out" : drink.getStock() < 10 ? "low" : "ok");
        
        bottom.getChildren().addAll(price, spacer, stock);
        content.getChildren().addAll(name, brand, bottom);
        
        card.getChildren().addAll(imgContainer, content);
        
        if (drink.getStock() > 0) {
            card.setOnMouseClicked(e -> addToCart(drink));
        } else {
            card.setDisable(true);
            card.setOpacity(0.5);
        }
        return card;
    }

    private void addToCart(Drink drink) {
        for (CartItem item : cart) {
            if (item.name.equals(drink.getName())) {
                item.qty++;
                return;
            }
        }
        cart.add(new CartItem(drink.getName(), 1, drink.getPrice()));
    }

    private VBox createCartPanel() {
        VBox panel = new VBox(0);
        panel.getStyleClass().add("ds-card");
        panel.setPadding(new Insets(0));
        panel.setPrefWidth(320);
        panel.setMaxWidth(320);

        // Header
        HBox header = new HBox();
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setAlignment(Pos.CENTER_LEFT);
        Label headerTitle = new Label("Current Cart");
        headerTitle.getStyleClass().add("ds-heading");
        Label badge = new Label(cart.size() + " Items");
        badge.getStyleClass().add("badge-primary");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(headerTitle, spacer, badge);

        // Items
        VBox itemsBox = new VBox(8);
        itemsBox.setPadding(new Insets(16, 20, 16, 20));
        itemsBox.setMaxHeight(300);

        ScrollPane scroll = new ScrollPane(itemsBox);
        scroll.getStyleClass().add("content-scroll");
        scroll.setFitToWidth(true);
        scroll.setMaxHeight(300);

        // Footer
        VBox footer = new VBox(12);
        footer.getStyleClass().add("cart-footer");

        Label subtotalLabel = new Label();
        subtotalLabel.getStyleClass().add("cart-subtotal");
        Label totalLabel = new Label();
        totalLabel.getStyleClass().add("cart-total");
        Button completeBtn = new Button("Complete Sale");
        completeBtn.setMaxWidth(Double.MAX_VALUE);

        footer.getChildren().addAll(subtotalLabel, totalLabel, completeBtn);

        panel.getChildren().addAll(header, scroll, footer);

        // Bind updates
        cart.addListener((javafx.collections.ListChangeListener<CartItem>) c -> updateCart(itemsBox, subtotalLabel, totalLabel, badge));
        updateCart(itemsBox, subtotalLabel, totalLabel, badge);

        return panel;
    }

    private void updateCart(VBox itemsBox, Label subtotalLabel, Label totalLabel, Label badge) {
        itemsBox.getChildren().clear();
        int subtotal = 0;
        for (CartItem item : cart) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);

            Button deleteBtn = new Button();
            deleteBtn.setGraphic(Icons.trash(14, Color.web("#9CA3AF")));
            deleteBtn.getStyleClass().add("btn-link");
            deleteBtn.setOnAction(e -> {
                cart.remove(item);
            });

            Label name = new Label(item.name);
            name.getStyleClass().add("cart-item");
            name.getStyleClass().add("name");
            Label detail = new Label(item.qty + " x KES " + item.unitPrice);
            detail.getStyleClass().add("cart-item");
            detail.getStyleClass().add("detail");
            VBox info = new VBox(2, name, detail);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label total = new Label("KES " + (item.qty * item.unitPrice));
            total.getStyleClass().add("cart-item");
            total.getStyleClass().add("total");

            row.getChildren().addAll(deleteBtn, info, spacer, total);
            itemsBox.getChildren().add(row);
            subtotal += item.qty * item.unitPrice;
        }

        if (cart.isEmpty()) {
            Label empty = new Label("Cart is empty");
            empty.getStyleClass().add("cart-empty");
            empty.setAlignment(Pos.CENTER);
            itemsBox.getChildren().add(empty);
        }

        subtotalLabel.setText("Subtotal: KES " + String.format("%,d", subtotal));
        totalLabel.setText("Total: KES " + String.format("%,d", subtotal));
        badge.setText(cart.size() + " Items");
    }

    public VBox getView() { return view; }

    public static class CartItem {
        public String name;
        public int qty;
        public int unitPrice;
        public CartItem(String name, int qty, int unitPrice) {
            this.name = name;
            this.qty = qty;
            this.unitPrice = unitPrice;
        }
    }
}
