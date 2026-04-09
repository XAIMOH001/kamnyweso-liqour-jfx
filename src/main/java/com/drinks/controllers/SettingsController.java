package com.drinks.controllers;

import com.drinks.components.Icons;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SettingsController {
    private final VBox view;

    public SettingsController() {
        view = new VBox(20);

        Label title = new Label("Settings");
        title.getStyleClass().add("ds-title");

        VBox generalCard = createGeneralCard();
        VBox notificationsCard = createNotificationsCard();
        VBox securityCard = createSecurityCard();

        view.getChildren().addAll(title, generalCard, notificationsCard, securityCard);
    }

    private VBox createGeneralCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("ds-card");

        Label header = new Label("General");
        header.getStyleClass().add("ds-heading");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(8);

        Label storeNameL = new Label("Store Name"); storeNameL.getStyleClass().add("ds-label");
        TextField storeNameF = new TextField("Kamnyweso Liquor Store");

        Label currencyL = new Label("Currency"); currencyL.getStyleClass().add("ds-label");
        ComboBox<String> currencyF = new ComboBox<>();
        currencyF.getItems().addAll("KES - Kenyan Shilling", "USD - US Dollar", "EUR - Euro", "GBP - British Pound");
        currencyF.setValue("KES - Kenyan Shilling");

        Label timezoneL = new Label("Timezone"); timezoneL.getStyleClass().add("ds-label");
        ComboBox<String> timezoneF = new ComboBox<>();
        timezoneF.getItems().addAll("Africa/Nairobi (EAT, UTC+3)", "UTC", "Europe/London (GMT)", "America/New_York (EST)");
        timezoneF.setValue("Africa/Nairobi (EAT, UTC+3)");

        Label sessionTimeoutL = new Label("Session Timeout (minutes)"); sessionTimeoutL.getStyleClass().add("ds-label");
        Spinner<Integer> sessionTimeoutF = new Spinner<>(5, 120, 30, 5);
        sessionTimeoutF.setEditable(true);

        form.add(storeNameL, 0, 0); form.add(storeNameF, 0, 1);
        form.add(currencyL, 1, 0); form.add(currencyF, 1, 1);
        form.add(timezoneL, 0, 2); form.add(timezoneF, 0, 3);
        form.add(sessionTimeoutL, 1, 2); form.add(sessionTimeoutF, 1, 3);
        GridPane.setColumnSpan(storeNameF, 1);

        Button saveGeneralBtn = new Button("Save General Settings");
        saveGeneralBtn.getStyleClass().add("ds-btn-primary");
        saveGeneralBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Settings Saved");
            alert.setHeaderText(null);
            alert.setContentText("General settings have been saved successfully.");
            alert.showAndWait();
        });

        card.getChildren().addAll(header, form, saveGeneralBtn);
        return card;
    }

    private VBox createNotificationsCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("ds-card");

        Label header = new Label("Notifications");
        header.getStyleClass().add("ds-heading");

        VBox toggles = new VBox(12);

        ToggleGroup group = new ToggleGroup();
        
        String[][] notificationPrefs = {
            {"Low stock alerts", "Get notified when items fall below minimum threshold"},
            {"Order status changes", "Receive updates when orders change status"},
            {"Return request updates", "Get notified when HQ responds to return requests"},
            {"Daily sales summary", "Receive end-of-day sales report via email"},
            {"New staff registration", "Get notified when new staff accounts are created"}
        };

        for (String[] pref : notificationPrefs) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            CheckBox cb = new CheckBox();
            cb.setSelected(true);
            Label textLabel = new Label(pref[0]);
            textLabel.getStyleClass().add("font-medium");
            Label descLabel = new Label(pref[1]);
            descLabel.getStyleClass().add("muted");
            VBox textGroup = new VBox(2, textLabel, descLabel);
            row.getChildren().addAll(cb, textGroup);
            toggles.getChildren().add(row);
        }

        Button saveNotifBtn = new Button("Save Notification Preferences");
        saveNotifBtn.getStyleClass().add("ds-btn-primary");
        saveNotifBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Settings Saved");
            alert.setHeaderText(null);
            alert.setContentText("Notification preferences have been saved successfully.");
            alert.showAndWait();
        });

        card.getChildren().addAll(header, toggles, saveNotifBtn);
        return card;
    }

    private VBox createSecurityCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("ds-card");

        Label header = new Label("Security");
        header.getStyleClass().add("ds-heading");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(8);

        Label twoFactorL = new Label("Two-Factor Authentication"); twoFactorL.getStyleClass().add("ds-label");
        CheckBox twoFactorF = new CheckBox("Require OTP on login");
        twoFactorF.setSelected(true);

        Label failedAttemptsL = new Label("Max Failed Login Attempts"); failedAttemptsL.getStyleClass().add("ds-label");
        Spinner<Integer> failedAttemptsF = new Spinner<>(3, 10, 3, 1);
        failedAttemptsF.setEditable(true);

        Label pinLengthL = new Label("Supervisor PIN Length"); pinLengthL.getStyleClass().add("ds-label");
        ComboBox<Integer> pinLengthF = new ComboBox<>();
        pinLengthF.getItems().addAll(4, 6);
        pinLengthF.setValue(4);

        Label autoLockL = new Label("Auto-lock on Inactivity (minutes)"); autoLockL.getStyleClass().add("ds-label");
        Spinner<Integer> autoLockF = new Spinner<>(5, 60, 30, 5);
        autoLockF.setEditable(true);

        form.add(twoFactorL, 0, 0); form.add(twoFactorF, 0, 1);
        form.add(failedAttemptsL, 1, 0); form.add(failedAttemptsF, 1, 1);
        form.add(pinLengthL, 0, 2); form.add(pinLengthF, 0, 3);
        form.add(autoLockL, 1, 2); form.add(autoLockF, 1, 3);

        HBox btnRow = new HBox(12);
        Button saveSecurityBtn = new Button("Save Security Settings");
        saveSecurityBtn.getStyleClass().add("ds-btn-primary");
        saveSecurityBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Settings Saved");
            alert.setHeaderText(null);
            alert.setContentText("Security settings have been saved successfully.");
            alert.showAndWait();
        });

        Button resetPinBtn = new Button("Reset All Staff PINs");
        resetPinBtn.getStyleClass().add("btn-destructive");
        resetPinBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Reset");
            confirm.setHeaderText("Reset All Staff Supervisor PINs?");
            confirm.setContentText("This will invalidate all existing supervisor PINs. Staff will need to set new PINs on next login.");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("PINs Reset");
                    success.setHeaderText(null);
                    success.setContentText("All staff supervisor PINs have been reset successfully.");
                    success.showAndWait();
                }
            });
        });

        btnRow.getChildren().addAll(saveSecurityBtn, resetPinBtn);

        card.getChildren().addAll(header, form, btnRow);
        return card;
    }

    public VBox getView() {
        return view;
    }
}
