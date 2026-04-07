package com.drinks.controllers;

import com.drinks.components.Icons;
import com.drinks.services.NavigationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class PortalSelectorController {
    private final VBox view;

    public PortalSelectorController() {
        view = new VBox(32);
        view.setAlignment(Pos.CENTER);
        view.getStyleClass().add("auth-page");

        Label logo = new Label();
        try {
            Image logoImg = new Image(getClass().getResourceAsStream("/com/drinks/assets/images/kls-logo.png"));
            ImageView logoView = new ImageView(logoImg);
            logoView.setFitWidth(120);
            logoView.setFitHeight(120);
            logoView.setPreserveRatio(true);
            logo.setGraphic(logoView);
        } catch (Exception e) {
            logo.setText("KLS");
        }
        logo.getStyleClass().addAll("auth-logo", "large");

        Label title = new Label("Kamnyweso Liqour Store");
        title.getStyleClass().addAll("auth-title", "large");

        Label subtitle = new Label("Distributed Drinks Management System");
        subtitle.getStyleClass().add("auth-subtitle");

        GridPane cards = new GridPane();
        cards.setHgap(24);
        cards.setVgap(24);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        cards.getColumnConstraints().addAll(c1, c2);

        cards.add(createPortalCard("Admin Portal", "HQ Nairobi \u2014 Full system management, reports, stock control, and user management.", "blue", "Enter Admin Portal", "admin"), 0, 0);
        cards.add(createPortalCard("Branch Portal", "Place orders, track stock, manage loyalty, and handle returns at branch level.", "green", "Enter Branch Portal", "branch"), 1, 0);

        Label footer = new Label("4 Branches: Nairobi HQ \u00B7 Nakuru \u00B7 Mombasa \u00B7 Kisumu");
        footer.getStyleClass().add("auth-footer");

        VBox centerContent = new VBox(32, logo, title, subtitle, cards, footer);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setMaxWidth(700);
        view.getChildren().add(centerContent);
    }

    private Node createPortalCard(String heading, String desc, String accent, String btnText, String portal) {
        VBox card = new VBox(16);
        card.getStyleClass().add("portal-card");
        card.setOnMouseClicked(e -> {
            NavigationManager.getInstance().setPortal(portal);
            NavigationManager.getInstance().navigate("login", new LoginController().getView());
        });
        card.setCursor(javafx.scene.Cursor.HAND);

        Node iconGraphic = portal.equals("admin")
            ? Icons.building(24, Color.web(accent.equals("blue") ? "#2563EB" : "#059669"))
            : Icons.store(24, Color.web(accent.equals("blue") ? "#2563EB" : "#059669"));
        Label icon = new Label();
        icon.setGraphic(iconGraphic);
        icon.getStyleClass().addAll("portal-icon", accent);

        Label headingLabel = new Label(heading);
        headingLabel.getStyleClass().add("portal-heading");

        Label descLabel = new Label(desc);
        descLabel.getStyleClass().add("portal-desc");

        Label actionLabel = new Label(btnText + " \u2192");
        actionLabel.getStyleClass().addAll("portal-action", accent);

        card.getChildren().addAll(icon, headingLabel, descLabel, actionLabel);
        return card;
    }

    public VBox getView() {
        return view;
    }
}
