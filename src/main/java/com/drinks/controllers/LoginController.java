package com.drinks.controllers;

import com.drinks.services.NavigationManager;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class LoginController {
    private final VBox view;
    private int failedAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public LoginController() {
        view = new VBox(24);
        view.setAlignment(Pos.CENTER);
        view.getStyleClass().add("auth-page");

        VBox card = new VBox(16);
        card.getStyleClass().add("auth-card");
        card.setMaxWidth(400);

        Label logo = new Label();
        try {
            Image logoImg = new Image(getClass().getResourceAsStream("/com/drinks/assets/images/kls-logo.png"));
            ImageView logoView = new ImageView(logoImg);
            logoView.setFitWidth(56);
            logoView.setFitHeight(56);
            logoView.setPreserveRatio(true);
            logo.setGraphic(logoView);
        } catch (Exception e) {
            logo.setText("KLS");
        }
        logo.getStyleClass().addAll("auth-logo", "medium");
        logo.setAlignment(Pos.CENTER);

        Label title = new Label("Kamnyweso Liqour Store");
        title.getStyleClass().add("auth-title");

        Label subtitle = new Label("Distributed Management System");
        subtitle.getStyleClass().add("auth-subtitle");

        VBox header = new VBox(8, logo, title, subtitle);
        header.setAlignment(Pos.CENTER);

        ComboBox<String> branchField = new ComboBox<>();
        branchField.getItems().addAll("Nairobi HQ", "Nakuru", "Mombasa", "Kisumu");
        branchField.setValue("Nairobi HQ");

        Label branchLabel = new Label("Branch");
        branchLabel.getStyleClass().add("ds-label");
        VBox branchBox = new VBox(6, branchLabel, branchField);

        TextField staffField = new TextField();
        staffField.setPromptText("e.g. DS-4421");
        Label staffLabel = new Label("Staff ID / Email");
        staffLabel.getStyleClass().add("ds-label");
        VBox staffBox = new VBox(6, staffLabel, staffField);

        PasswordField passField = new PasswordField();
        passField.setPromptText("\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022");
        Label passLabel = new Label("Password");
        passLabel.getStyleClass().add("ds-label");

        Button forgotBtn = new Button("Forgot?");
        forgotBtn.getStyleClass().add("btn-link");

        HBox passHeader = new HBox(passLabel, new Region(), forgotBtn);
        HBox.setHgrow(passHeader.getChildren().get(1), Priority.ALWAYS);
        passHeader.setAlignment(Pos.CENTER_LEFT);

        VBox passBox = new VBox(6, passHeader, passField);

        CheckBox rememberBox = new CheckBox("Remember this device");
        rememberBox.getStyleClass().add("auth-remember");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("danger-text");
        errorLabel.setVisible(false);

        Button signInBtn = new Button("Sign In \u2192");
        signInBtn.setMaxWidth(Double.MAX_VALUE);
        signInBtn.setOnAction(e -> {
            String staff = staffField.getText().trim();
            String pass = passField.getText();

            if (staff.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Staff ID and password are required");
                errorLabel.setVisible(true);
                return;
            }

            if (failedAttempts >= MAX_LOGIN_ATTEMPTS) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account Locked");
                alert.setHeaderText(null);
                alert.setContentText("Too many failed login attempts. Your account has been locked. Please contact an administrator.");
                alert.showAndWait();
                return;
            }

            if (staff.length() < 3) {
                errorLabel.setText("Invalid Staff ID format");
                errorLabel.setVisible(true);
                return;
            }

            if (pass.length() < 4) {
                errorLabel.setText("Password must be at least 4 characters");
                errorLabel.setVisible(true);
                return;
            }

            errorLabel.setVisible(false);
            NavigationManager.getInstance().navigate("otp", new OTPVerifyController().getView());
        });

        Separator sep = new Separator();

        Label registerPrompt = new Label("Don't have an account? ");
        Button registerLink = new Button("Register");
        registerLink.getStyleClass().add("btn-link");
        registerLink.setOnAction(e -> {
            NavigationManager.getInstance().navigate("register", new RegisterController().getView());
        });

        HBox registerRow = new HBox(4, registerPrompt, registerLink);
        registerRow.setAlignment(Pos.CENTER);

        Label sessionNote = new Label("Sessions expire after 30 min of inactivity");
        sessionNote.getStyleClass().add("auth-session-note");

        VBox footer = new VBox(16, sep, registerRow, sessionNote);
        footer.setAlignment(Pos.CENTER);

        card.getChildren().addAll(header, branchBox, staffBox, passBox, rememberBox, errorLabel, signInBtn, footer);
        view.getChildren().add(card);
    }

    public VBox getView() {
        return view;
    }
}
