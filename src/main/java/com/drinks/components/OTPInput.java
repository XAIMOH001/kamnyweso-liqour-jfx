package com.drinks.components;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class OTPInput extends HBox {
    private final TextField[] fields;
    private Consumer<String> onComplete;

    public OTPInput(int length) {
        this(length, null);
    }

    public OTPInput(int length, Consumer<String> onComplete) {
        this.onComplete = onComplete;
        this.setSpacing(8);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("otp-input-container");

        fields = new TextField[length];
        for (int i = 0; i < length; i++) {
            final int index = i;
            TextField field = new TextField();
            field.getStyleClass().add("otp-field");
            field.setPrefWidth(48);
            field.setPrefHeight(56);
            field.setAlignment(Pos.CENTER);
            field.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            field.textProperty().addListener((obs, old, newVal) -> {
                if (!newVal.matches("\\d*")) {
                    field.setText(old);
                    return;
                }
                if (newVal.length() > 1) {
                    field.setText(newVal.substring(newVal.length() - 1));
                }
                if (!newVal.isEmpty() && index < length - 1) {
                    fields[index + 1].requestFocus();
                }
                checkComplete();
            });
            field.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.BACK_SPACE && field.getText().isEmpty() && index > 0) {
                    fields[index - 1].requestFocus();
                    fields[index - 1].clear();
                }
            });
            fields[i] = field;
            getChildren().add(field);
        }
    }

    private void checkComplete() {
        StringBuilder sb = new StringBuilder();
        for (TextField field : fields) {
            sb.append(field.getText());
        }
        String code = sb.toString();
        if (code.length() == fields.length && onComplete != null) {
            onComplete.accept(code);
        }
    }

    public void setOnComplete(Consumer<String> onComplete) {
        this.onComplete = onComplete;
    }

    public String getValue() {
        StringBuilder sb = new StringBuilder();
        for (TextField field : fields) {
            sb.append(field.getText());
        }
        return sb.toString();
    }

    public void clear() {
        for (TextField field : fields) {
            field.clear();
        }
        if (fields.length > 0) {
            fields[0].requestFocus();
        }
    }
}
