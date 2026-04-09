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
import javafx.util.Duration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AdminLayoutController {
    private final BorderPane view;
    private final VBox sidebar;
    private final VBox contentArea;
    private final VBox navContainer;
    private final VBox footerBox;
    private final Map<String, Supplier<VBox>> viewFactories = new LinkedHashMap<>();
    private final Map<String, VBox> viewCache = new HashMap<>();
    private final Map<Button, Label> navButtonLabels = new HashMap<>();
    private final Label logoText;
    private final Button collapseBtn;
    private final Label settingsBtnLabel;
    private boolean collapsed = false;
    private static final double SIDEBAR_EXPANDED = 220;
    private static final double SIDEBAR_COLLAPSED = 64;

    public AdminLayoutController() {
        view = new BorderPane();
        view.getStyleClass().add("admin-root");

        sidebar = new VBox(4);
        sidebar.getStyleClass().add("admin-sidebar");
        sidebar.setPrefWidth(SIDEBAR_EXPANDED);
        sidebar.setMinWidth(SIDEBAR_EXPANDED);
        sidebar.setPadding(new Insets(24, 12, 12, 12));

        HBox logoRow = new HBox(10);
        logoRow.setAlignment(Pos.CENTER_LEFT);
        logoRow.setPadding(new Insets(0, 0, 16, 0));
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
        logo.getStyleClass().add("sidebar-logo");
        logoText = new Label("Kamnyweso HQ");
        logoText.getStyleClass().add("sidebar-logo-text");
        logoRow.getChildren().addAll(logo, logoText);
        sidebar.getChildren().add(logoRow);

        navContainer = new VBox(4);
        navContainer.setPadding(new Insets(8, 0, 8, 0));
        sidebar.getChildren().add(navContainer);

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
            HBox btnContent = new HBox(10);
            btnContent.setAlignment(Pos.CENTER_LEFT);
            btnContent.getChildren().add(icons[idx]);
            Label textLabel = new Label(name);
            btnContent.getChildren().add(textLabel);
            
            Button navBtn = new Button();
            navBtn.setGraphic(btnContent);
            navBtn.getStyleClass().addAll("nav-item");
            navBtn.setMaxWidth(Double.MAX_VALUE);
            navBtn.setAlignment(Pos.CENTER_LEFT);
            navBtn.setOnAction(e -> {
                setActiveNav(navBtn);
                navigateTo(name);
            });
            if (idx == 0) setActiveNav(navBtn);
            
            navButtonLabels.put(navBtn, textLabel);
            navContainer.getChildren().add(navBtn);
            idx++;
        }

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);
        sidebar.getChildren().add(sidebarSpacer);

        footerBox = new VBox(4);
        footerBox.getStyleClass().add("footer-box");
        footerBox.setPadding(new Insets(16, 0, 0, 0));
        
        Button settingsBtn = new Button();
        HBox settingsContent = new HBox(10, Icons.settings(16, navColor));
        settingsContent.setAlignment(Pos.CENTER_LEFT);
        settingsBtnLabel = new Label("Settings");
        settingsContent.getChildren().add(settingsBtnLabel);
        settingsBtn.setGraphic(settingsContent);
        settingsBtn.getStyleClass().add("nav-item");
        settingsBtn.setOnAction(e -> navigateToSettings());
        
        collapseBtn = new Button();
        HBox collapseContent = new HBox(10);
        collapseContent.setAlignment(Pos.CENTER_LEFT);
        Node collapseIcon = Icons.createIcon("M15 18l-6-6 6-6", 16, navColor);
        collapseContent.getChildren().add(collapseIcon);
        Label collapseLabel = new Label("Collapse Sidebar");
        collapseContent.getChildren().add(collapseLabel);
        collapseBtn.setGraphic(collapseContent);
        collapseBtn.getStyleClass().add("nav-item");
        collapseBtn.setMaxWidth(Double.MAX_VALUE);
        collapseBtn.setAlignment(Pos.CENTER_LEFT);
        collapseBtn.setOnAction(e -> toggleCollapse());
        
        footerBox.getChildren().addAll(settingsBtn, collapseBtn);
        sidebar.getChildren().add(footerBox);

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

    private void toggleCollapse() {
        collapsed = !collapsed;
        
        if (collapsed) {
            sidebar.setPrefWidth(SIDEBAR_COLLAPSED);
            sidebar.setMinWidth(SIDEBAR_COLLAPSED);
            logoText.setVisible(false);
            logoText.setManaged(false);
            settingsBtnLabel.setVisible(false);
            settingsBtnLabel.setManaged(false);
            for (Label label : navButtonLabels.values()) {
                label.setVisible(false);
                label.setManaged(false);
            }
            ((HBox) collapseBtn.getGraphic()).getChildren().clear();
            ((HBox) collapseBtn.getGraphic()).getChildren().add(Icons.createIcon("M9 18l6-6-6-6", 16, Color.web("#F1F5F9")));
            ((HBox) collapseBtn.getGraphic()).getChildren().add(new Label("Expand"));
        } else {
            sidebar.setPrefWidth(SIDEBAR_EXPANDED);
            sidebar.setMinWidth(SIDEBAR_EXPANDED);
            logoText.setVisible(true);
            logoText.setManaged(true);
            settingsBtnLabel.setVisible(true);
            settingsBtnLabel.setManaged(true);
            for (Label label : navButtonLabels.values()) {
                label.setVisible(true);
                label.setManaged(true);
            }
            ((HBox) collapseBtn.getGraphic()).getChildren().clear();
            ((HBox) collapseBtn.getGraphic()).getChildren().add(Icons.createIcon("M15 18l-6-6 6-6", 16, Color.web("#F1F5F9")));
            ((HBox) collapseBtn.getGraphic()).getChildren().add(new Label("Collapse Sidebar"));
        }
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

    private void navigateToSettings() {
        clearActiveStates(sidebar);
        contentArea.getChildren().setAll(new SettingsController().getView());
    }

    private void setActiveNav(Button activeBtn) {
        // Clear all active states in sidebar children recursively
        clearActiveStates(sidebar);
        activeBtn.getStyleClass().add("active");
    }

    private void clearActiveStates(Pane container) {
        for (Node child : container.getChildren()) {
            if (child instanceof Button) {
                child.getStyleClass().remove("active");
            } else if (child instanceof Pane) {
                clearActiveStates((Pane) child);
            }
        }
    }

    public BorderPane getView() {
        return view;
    }
}
