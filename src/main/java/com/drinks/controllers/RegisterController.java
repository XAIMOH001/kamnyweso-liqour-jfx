package com.drinks.controllers;

import com.drinks.services.NavigationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class RegisterController {
    private final VBox view;
    private final int[] step = {1};
    private VBox stepContainer;

    public RegisterController() {
        view = new VBox(24);
        view.setAlignment(Pos.CENTER);
        view.getStyleClass().add("auth-page");

        VBox card = new VBox(20);
        card.getStyleClass().add("auth-card");
        card.setMaxWidth(400);

        Label logo = new Label("KLS");
        logo.getStyleClass().addAll("auth-logo", "small");
        logo.setAlignment(Pos.CENTER);

        Label title = new Label("Create Account");
        title.getStyleClass().add("auth-title");

        Label subtitle = new Label("Join Kamnyweso Liqour Store Management System");
        subtitle.getStyleClass().add("auth-subtitle");

        VBox header = new VBox(8, logo, title, subtitle);
        header.setAlignment(Pos.CENTER);

        HBox dots = new HBox(8);
        dots.setAlignment(Pos.CENTER);
        for (int i = 1; i <= 3; i++) {
            Region dot = new Region();
            dot.getStyleClass().addAll("progress-dot", "pending");
            dots.getChildren().add(dot);
        }

        stepContainer = new VBox(16);
        buildStep1(stepContainer);

        Separator sep = new Separator();
        Label loginPrompt = new Label("Already have an account? ");
        Button loginLink = new Button("Sign In");
        loginLink.getStyleClass().add("btn-link");
        loginLink.setOnAction(e -> NavigationManager.getInstance().navigate("login", new LoginController().getView()));
        HBox loginRow = new HBox(4, loginPrompt, loginLink);
        loginRow.setAlignment(Pos.CENTER);
        VBox footer = new VBox(8, sep, loginRow);
        footer.setAlignment(Pos.CENTER);

        card.getChildren().addAll(header, dots, stepContainer, footer);
        view.getChildren().add(card);
    }

    private void updateDots(int currentStep) {
        VBox rootCard = (VBox) view.getChildren().get(0);
        for (Node child : rootCard.getChildren()) {
            if (child instanceof HBox) {
                HBox dots = (HBox) child;
                for (int i = 0; i < dots.getChildren().size(); i++) {
                    Region dot = (Region) dots.getChildren().get(i);
                    dot.getStyleClass().removeAll("active", "done", "pending");
                    String cls = (i + 1) == currentStep ? "active" : (i + 1) < currentStep ? "done" : "pending";
                    dot.getStyleClass().add(cls);
                }
                break;
            }
        }
    }

    private void buildStep1(VBox container) {
        container.getChildren().clear();
        String[] fields = {"Full Name", "Phone Number", "Email"};
        String[] prompts = {"John Doe", "+254 7XX XXX XXX", "john@example.com"};
        for (int i = 0; i < fields.length; i++) {
            Label label = new Label(fields[i]);
            label.getStyleClass().add("ds-label");
            TextField field = new TextField();
            field.setPromptText(prompts[i]);
            container.getChildren().addAll(label, field);
        }

        Label branchLabel = new Label("Branch");
        branchLabel.getStyleClass().add("ds-label");
        ComboBox<String> branchField = new ComboBox<>();
        branchField.getItems().addAll("Nairobi HQ", "Nakuru", "Mombasa", "Kisumu");
        branchField.setValue("Nairobi HQ");
        container.getChildren().addAll(branchLabel, branchField);

        Label roleLabel = new Label("Role");
        roleLabel.getStyleClass().add("ds-label");
        ComboBox<String> roleField = new ComboBox<>();
        roleField.getItems().addAll("Cashier", "Branch Manager", "Administrator");
        roleField.setValue("Cashier");
        container.getChildren().addAll(roleLabel, roleField);

        Button continueBtn = new Button("Continue \u2192");
        continueBtn.setMaxWidth(Double.MAX_VALUE);
        continueBtn.setOnAction(e -> {
            step[0] = 2;
            updateDots(2);
            buildStep2(container);
        });
        container.getChildren().add(continueBtn);
    }

    private void buildStep2(VBox container) {
        container.getChildren().clear();

        Label passLabel = new Label("Password");
        passLabel.getStyleClass().add("ds-label");
        PasswordField passField = new PasswordField();
        passField.setPromptText("\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022");
        container.getChildren().addAll(passLabel, passField);

        Label confirmLabel = new Label("Confirm Password");
        confirmLabel.getStyleClass().add("ds-label");
        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022");
        container.getChildren().addAll(confirmLabel, confirmField);

        VBox rules = new VBox(6);
        String[] ruleTexts = {"At least 8 characters", "One uppercase letter", "One number", "One special symbol"};
        for (String rule : ruleTexts) {
            Label ruleLabel = new Label(rule);
            ruleLabel.getStyleClass().add("rule-unmet");
            rules.getChildren().add(ruleLabel);
        }
        container.getChildren().add(rules);

        HBox buttons = new HBox(12);
        Button backBtn = new Button("\u2190 Back");
        backBtn.getStyleClass().add("btn-secondary");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(backBtn, Priority.ALWAYS);
        backBtn.setOnAction(e -> {
            step[0] = 1;
            updateDots(1);
            buildStep1(container);
        });

        Button nextBtn = new Button("Continue \u2192");
        nextBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nextBtn, Priority.ALWAYS);
        nextBtn.setOnAction(e -> {
            step[0] = 3;
            updateDots(3);
            buildStep3(container);
        });

        buttons.getChildren().addAll(backBtn, nextBtn);
        container.getChildren().add(buttons);
    }

    private void buildStep3(VBox container) {
        container.getChildren().clear();
        container.setAlignment(Pos.CENTER);

        Label msg = new Label("OTP sent to ");
        msg.getStyleClass().add("register-msg");
        Label phone = new Label("+254 7XX XXX XXX");
        phone.getStyleClass().add("register-phone");
        HBox otpLine = new HBox(4, msg, phone);
        otpLine.setAlignment(Pos.CENTER);

        com.drinks.components.OTPInput otpInput = new com.drinks.components.OTPInput(6);
        container.getChildren().addAll(otpLine, otpInput);

        Label resend = new Label("Resend code in 04:59");
        resend.getStyleClass().add("register-msg");
        container.getChildren().add(resend);

        HBox buttons = new HBox(12);
        Button backBtn = new Button("\u2190 Back");
        backBtn.getStyleClass().add("btn-secondary");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(backBtn, Priority.ALWAYS);
        backBtn.setOnAction(e -> {
            step[0] = 2;
            updateDots(2);
            buildStep2(container);
        });

        Button completeBtn = new Button("Complete Registration");
        completeBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(completeBtn, Priority.ALWAYS);
        completeBtn.setOnAction(e -> NavigationManager.getInstance().navigate("login", new LoginController().getView()));

        buttons.getChildren().addAll(backBtn, completeBtn);
        container.getChildren().add(buttons);

        Label approval = new Label("Awaiting manager approval before account is activated");
        approval.getStyleClass().add("warning-text");
        container.getChildren().add(approval);
    }

    public VBox getView() {
        return view;
    }
}
