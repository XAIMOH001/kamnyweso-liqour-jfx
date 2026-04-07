package com.drinks.controllers;

import com.drinks.components.Icons;
import com.drinks.services.NavigationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BranchLayoutController {
    private final VBox view;
    private final VBox contentArea;
    private final HBox navBar;
    private final Map<String, Supplier<VBox>> viewFactories = new LinkedHashMap<>();
    private final Map<String, VBox> viewCache = new HashMap<>();

    public BranchLayoutController() {
        view = new VBox();
        view.getStyleClass().add("branch-root");

        // Header
        HBox header = new HBox(20);
        header.getStyleClass().add("branch-header");
        header.setPadding(new Insets(0, 24, 0, 24));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(64);
        header.setMinHeight(64);

        Label logo = new Label();
        try {
            Image logoImg = new Image(getClass().getResourceAsStream("/com/drinks/assets/images/kls-logo.png"));
            ImageView logoView = new ImageView(logoImg);
            logoView.setFitWidth(32);
            logoView.setFitHeight(32);
            logoView.setPreserveRatio(true);
            logo.setGraphic(logoView);
        } catch (Exception e) {
            logo.setText("KLS");
        }
        logo.getStyleClass().add("branch-logo");

        Label storeName = new Label("Kamnyweso Liqour Store");
        storeName.getStyleClass().add("branch-store-name");
        HBox brandBox = new HBox(12, logo, storeName);
        brandBox.setAlignment(Pos.CENTER_LEFT);

        navBar = new HBox(8);
        navBar.setAlignment(Pos.CENTER);
        
        // View factories for caching
        viewFactories.put("POS", () -> new PlaceOrderController().getView());
        viewFactories.put("Order to HQ", () -> new OrderToHQController().getView());
        viewFactories.put("Order History", () -> new OrderHistoryController().getView());
        viewFactories.put("Track Stock", () -> new TrackStockOrdersController().getView());
        viewFactories.put("Loyalty", () -> new LoyaltyController().getView());
        viewFactories.put("Returns", () -> new StockReturnsController().getView());

        int idx = 0;
        for (Map.Entry<String, Supplier<VBox>> entry : viewFactories.entrySet()) {
            String name = entry.getKey();
            Button tabBtn = new Button(name);
            tabBtn.getStyleClass().addAll("branch-nav-item");
            tabBtn.setOnAction(e -> {
                setActiveTab(tabBtn);
                navigateTo(name);
            });
            if (idx == 0) setActiveTab(tabBtn);
            navBar.getChildren().add(tabBtn);
            idx++;
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Region liveDot = new Region();
        liveDot.getStyleClass().add("live-dot");
        Label liveText = new Label("Connected");
        liveText.getStyleClass().add("branch-live-text");
        HBox liveBox = new HBox(6, liveDot, liveText);
        liveBox.setAlignment(Pos.CENTER);

        Separator vSep = new Separator();
        vSep.setOrientation(javafx.geometry.Orientation.VERTICAL);
        vSep.getStyleClass().add("branch-vsep");
        vSep.setPrefHeight(24);
        vSep.setPadding(new Insets(0, 8, 0, 8));

        Label userName = new Label("Sarah J.");
        userName.getStyleClass().add("branch-user-name");
        Label userBranch = new Label("Nakuru");
        userBranch.getStyleClass().add("branch-user-branch");
        VBox userInfo = new VBox(2, userName, userBranch);
        userInfo.setAlignment(Pos.CENTER_RIGHT);

        Label avatar = new Label("SJ");
        avatar.getStyleClass().add("avatar");

        Button logoutBtn = new Button();
        logoutBtn.setGraphic(Icons.logOut(16, Color.web("#F1F5F9")));
        logoutBtn.getStyleClass().add("btn-link");
        logoutBtn.setOnAction(e -> NavigationManager.getInstance().navigate("login", new LoginController().getView()));

        header.getChildren().addAll(brandBox, navBar, spacer, liveBox, userInfo, avatar, logoutBtn);

        // Content
        contentArea = new VBox();
        contentArea.setPadding(new Insets(24));
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.getStyleClass().add("content-scroll");
        scrollPane.setFitToWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        view.getChildren().addAll(header, scrollPane);

        navigateTo("POS");
    }

    private void navigateTo(String viewName) {
        VBox cachedView = viewCache.get(viewName);
        if (cachedView == null) {
            Supplier<VBox> factory = viewFactories.get(viewName);
            if (factory != null) {
                cachedView = factory.get();
                viewCache.put(viewName, cachedView);
            }
        }
        if (cachedView != null) {
            contentArea.getChildren().setAll(cachedView);
        }
    }

    private void setActiveTab(Button activeBtn) {
        for (Node child : navBar.getChildren()) {
            if (child instanceof Button) {
                child.getStyleClass().remove("active");
            }
        }
        activeBtn.getStyleClass().add("active");
    }

    public VBox getView() {
        return view;
    }
}
