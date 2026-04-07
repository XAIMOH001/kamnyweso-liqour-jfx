package com.drinks.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class StatCard extends VBox {
    public StatCard(String title, String value, String trend, Color accentColor) {
        setStyle("-fx-padding: 20px;");
        getStyleClass().add("stat-card");
        setSpacing(8);

        Region accentBar = new Region();
        accentBar.setPrefWidth(4);
        accentBar.setStyle("-fx-background-color: " + toHex(accentColor) + "; -fx-background-radius: 12px 0 0 12px;");
        VBox.setVgrow(accentBar, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("title");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("value");

        Label trendLabel = new Label(trend.replace("↑ ", "").replace("↓ ", ""));
        boolean isUp = trend.contains("↑");
        trendLabel.getStyleClass().add(isUp ? "trend-up" : "trend-down");

        HBox topRow = new HBox(8, titleLabel);
        topRow.setAlignment(Pos.TOP_LEFT);

        VBox content = new VBox(4, valueLabel, trendLabel);

        HBox container = new HBox(12, accentBar, content);
        HBox.setHgrow(content, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.ALWAYS);

        getChildren().addAll(topRow, container);
    }

    private String toHex(Color c) {
        return String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
    }
}
