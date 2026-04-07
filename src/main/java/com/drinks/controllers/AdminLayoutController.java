package com.drinks.controllers;

import com.drinks.components.Icons;
import com.drinks.services.NavigationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AdminLayoutController {
    private final BorderPane view;
    private final VBox sidebar;
    private final VBox contentArea;
    private final Map<String, Supplier<VBox>> viewFactories = new LinkedHashMap<>();
    private final Map<String, VBox> viewCache = new HashMap<>();

    public AdminLayoutController() {
        view = new BorderPane();
        view.getStyleClass().add("admin-root");

        sidebar = new VBox(4);
        sidebar.getStyleClass().add("admin-sidebar");
        sidebar.setPrefWidth(220);
        sidebar.setMinWidth(220);
        sidebar.setPadding(new Insets(24, 12, 12, 12));

        HBox logoRow = new HBox(10);
        logoRow.setAlignment(Pos.CENTER_LEFT);
        logoRow.setPadding(new Insets(0, 0, 16, 0));
        Label logo = new Label("KLS");
        logo.getStyleClass().add("sidebar-logo");
        Label logoText = new Label("Kamnyweso HQ");
        logoText.getStyleClass().add("sidebar-logo-text");
        logoRow.getChildren().addAll(logo, logoText);
        sidebar.getChildren().add(logoRow);

        Separator sep1 = new Separator();
        sep1.getStyleClass().add("sidebar-sep");
        sidebar.getChildren().add(sep1);

        // Register view factories — each creates a controller only on first access
        viewFactories.put("Dashboard", () -> new AdminDashboardController(this).getView());
        viewFactories.put("POS", () -> new AdminPOSController().getView());
        viewFactories.put("Orders", () -> new OrderManagementController().getView());
        viewFactories.put("Order Tracking", () -> new OrderTrackingController().getView());
        viewFactories.put("Stock Management", () -> new StockManagementController().getView());
        viewFactories.put("Reports", () -> new ReportsController().getView());
        viewFactories.put("User Management", () -> new UserManagementController().getView());

        // SVG icons matching Lucide icons from React AdminLayout
        Color navColor = Color.web("#F1F5F9");
        Node[] icons = {
            Icons.dashboard(16, navColor),
            Icons.shoppingCart(16, navColor),
            Icons.pkg(16, navColor),
            Icons.truck(16, navColor),
            Icons.refreshCw(16, navColor),
            Icons.barChart(16, navColor),
            Icons.users(16, navColor)
        };

        int idx = 0;
        for (Map.Entry<String, Supplier<VBox>> entry : viewFactories.entrySet()) {
            String name = entry.getKey();
            Button navBtn = new Button(name);
            navBtn.setGraphic(icons[idx]);
            navBtn.getStyleClass().addAll("nav-item");
            navBtn.setMaxWidth(Double.MAX_VALUE);
            navBtn.setAlignment(Pos.CENTER_LEFT);
            navBtn.setOnAction(e -> {
                setActiveNav(navBtn);
                navigateTo(name);
            });
            if (idx == 0) setActiveNav(navBtn);
            sidebar.getChildren().add(navBtn);
            idx++;
        }

        Separator sep2 = new Separator();
        sep2.getStyleClass().add("sidebar-sep");
        sidebar.getChildren().add(sep2);

        Button settingsBtn = new Button("Settings");
        settingsBtn.setGraphic(Icons.settings(16, navColor));
        settingsBtn.getStyleClass().add("nav-item");
        settingsBtn.setMaxWidth(Double.MAX_VALUE);
        settingsBtn.setAlignment(Pos.CENTER_LEFT);
        sidebar.getChildren().add(settingsBtn);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setGraphic(Icons.logOut(16, navColor));
        logoutBtn.getStyleClass().add("nav-item");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setOnAction(e -> NavigationManager.getInstance().navigate("login", new LoginController().getView()));
        sidebar.getChildren().add(logoutBtn);

        VBox.setVgrow(sidebar, Priority.ALWAYS);

        // Header
        HBox header = new HBox();
        header.getStyleClass().add("admin-header");
        header.setPadding(new Insets(0, 24, 0, 24));
        header.setPrefHeight(56);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(16);

        Label hqBadge = new Label("HQ NAIROBI");
        hqBadge.getStyleClass().add("hq-badge");

        Region liveDot = new Region();
        liveDot.getStyleClass().add("live-dot");
        Label liveText = new Label("System Live");
        liveText.getStyleClass().add("live-text");
        HBox liveBox = new HBox(6, liveDot, liveText);
        liveBox.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userName = new Label("Alex Mwakideu");
        userName.getStyleClass().add("user-name");
        Label userRole = new Label("Administrator");
        userRole.getStyleClass().add("user-role");
        VBox userInfo = new VBox(2, userName, userRole);

        Button logoutHeaderBtn = new Button();
        logoutHeaderBtn.setGraphic(Icons.logOut(16, Color.web("#F1F5F9")));
        logoutHeaderBtn.getStyleClass().add("btn-link");
        logoutHeaderBtn.setOnAction(e -> NavigationManager.getInstance().navigate("login", new LoginController().getView()));

        header.getChildren().addAll(hqBadge, liveBox, spacer, userInfo, logoutHeaderBtn);

        // Content area
        contentArea = new VBox();
        contentArea.setPadding(new Insets(24));
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.getStyleClass().add("content-scroll");
        scrollPane.setFitToWidth(true);

        view.setLeft(sidebar);
        view.setTop(header);
        view.setCenter(scrollPane);

        // Load default view
        navigateTo("Dashboard");
    }

    /**
     * Navigate to a named view within the admin layout.
     * Views are cached on first creation to prevent re-instantiation.
     */
    public void navigateTo(String viewName) {
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

    private void setActiveNav(Button activeBtn) {
        for (Node child : sidebar.getChildren()) {
            if (child instanceof Button) {
                child.getStyleClass().remove("active");
            }
        }
        activeBtn.getStyleClass().add("active");
    }

    public BorderPane getView() {
        return view;
    }
}
