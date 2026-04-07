package com.drinks.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class StatusBadge extends Label {
    public StatusBadge(String status) {
        super(status);
        getStyleClass().addAll("status-badge", statusToClass(status));
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setAlignment(Pos.CENTER);
    }

    private String statusToClass(String status) {
        return switch (status.toLowerCase()) {
            case "pending" -> "pending";
            case "processing" -> "processing";
            case "dispatched" -> "dispatched";
            case "in transit" -> "in-transit";
            case "confirmed" -> "confirmed";
            case "received" -> "received";
            case "cancelled" -> "cancelled";
            case "active" -> "active";
            case "inactive" -> "inactive";
            case "suspended" -> "suspended";
            case "low" -> "low";
            case "critical" -> "critical";
            case "out of stock" -> "out-of-stock";
            case "ok" -> "ok";
            case "pending review" -> "pending-review";
            case "approved" -> "approved";
            case "rejected" -> "rejected";
            case "replacement sent" -> "replacement-sent";
            default -> "";
        };
    }
}
