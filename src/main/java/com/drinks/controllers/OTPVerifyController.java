package com.drinks.controllers;

import com.drinks.components.Icons;
import com.drinks.components.OTPInput;
import com.drinks.services.NavigationManager;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class OTPVerifyController {
    private final VBox view;

    public OTPVerifyController() {
        view = new VBox(24);
        view.setAlignment(Pos.CENTER);
        view.getStyleClass().add("auth-page");

        VBox card = new VBox(20);
        card.getStyleClass().add("auth-card");
        card.setMaxWidth(400);
        card.setAlignment(Pos.CENTER);

        Label icon = new Label();
        icon.setGraphic(Icons.shieldCheck(24, Color.web("#2563EB")));
        icon.getStyleClass().add("otp-icon");

        Label title = new Label("Two-factor authentication");
        title.getStyleClass().add("otp-title");

        Label desc = new Label("Enter the 6-digit code sent to +254 7XX XXX 882");
        desc.getStyleClass().add("otp-desc");

        Label branchLbl = new Label("Branch: ");
        branchLbl.getStyleClass().add("otp-branch-label");
        Label branchVal = new Label("Nairobi HQ");
        branchVal.getStyleClass().add("otp-branch-value");
        HBox branchRow = new HBox(4, branchLbl, branchVal);

        OTPInput otpInput = new OTPInput(6);

        Label resend = new Label("Resend code in 04:59");
        resend.getStyleClass().add("otp-resend");

        Button verifyBtn = new Button("Verify & Enter System");
        verifyBtn.setMaxWidth(Double.MAX_VALUE);
        verifyBtn.setOnAction(e -> {
            String portal = NavigationManager.getInstance().getPortal();
            if (portal.equals("admin")) {
                NavigationManager.getInstance().navigate("admin-layout", new AdminLayoutController().getView());
            } else {
                NavigationManager.getInstance().navigate("branch-layout", new BranchLayoutController().getView());
            }
        });

        Label warning = new Label("3 failed attempts locks account for 15 minutes");
        warning.getStyleClass().add("danger-text");

        card.getChildren().addAll(icon, title, desc, branchRow, otpInput, resend, verifyBtn, warning);
        view.getChildren().add(card);
    }

    public VBox getView() {
        return view;
    }
}
