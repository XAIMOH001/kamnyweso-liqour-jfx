package com.drinks.controllers;

import com.drinks.components.StatusBadge;
import com.drinks.models.TimelineStep;
import com.drinks.models.TrackedOrder;
import com.drinks.services.MockDataService;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class OrderTrackingController {
    private final VBox view;
    private final FlowPane ordersContainer;
    private final ObservableList<TrackedOrder> allOrders;

    public OrderTrackingController() {
        view = new VBox(16);
        allOrders = MockDataService.getInstance().getAdminTrackedOrders();

        Label title = new Label("Order Tracking \u2014 HQ to Branches");
        title.getStyleClass().add("ds-title");

        HBox filters = createFilters();
        ordersContainer = new FlowPane();
        ordersContainer.setHgap(16);
        ordersContainer.setVgap(16);

        view.getChildren().addAll(title, filters, ordersContainer);
        renderOrders(allOrders);
    }

    private HBox createFilters() {
        HBox row = new HBox(12);
        row.getStyleClass().add("ds-card");
        row.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> branchField = new ComboBox<>();
        branchField.getItems().addAll("All Branches", "Nakuru", "Mombasa", "Kisumu");
        branchField.setValue("All Branches");

        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("All Statuses", "Pending", "Dispatched", "In Transit", "Received");
        statusField.setValue("All Statuses");

        DatePicker dateField = new DatePicker();

        row.getChildren().addAll(branchField, statusField, dateField);

        branchField.valueProperty().addListener((obs, o, n) -> applyFilters(branchField, statusField));
        statusField.valueProperty().addListener((obs, o, n) -> applyFilters(branchField, statusField));

        return row;
    }

    private void applyFilters(ComboBox<String> branch, ComboBox<String> status) {
        String b = branch.getValue();
        String s = status.getValue();
        ordersContainer.getChildren().clear();
        for (TrackedOrder o : allOrders) {
            boolean matchBranch = b.equals("All Branches") || o.getBranch().equals(b);
            boolean matchStatus = s.equals("All Statuses") || o.getStatus().equals(s);
            if (matchBranch && matchStatus) ordersContainer.getChildren().add(createOrderCard(o));
        }
    }

    private void renderOrders(ObservableList<TrackedOrder> orders) {
        ordersContainer.getChildren().clear();
        for (TrackedOrder o : orders) ordersContainer.getChildren().add(createOrderCard(o));
    }

    private VBox createOrderCard(TrackedOrder order) {
        VBox card = new VBox(12);
        card.getStyleClass().add("ds-card");
        card.setPrefWidth(300);
        card.setMaxWidth(300);

        HBox header = new HBox();
        header.setAlignment(Pos.TOP_LEFT);
        Label id = new Label(order.getId());
        id.getStyleClass().addAll("ds-text-muted", "ds-mono");
        Label branch = new Label(order.getBranch());
        branch.getStyleClass().add("ds-heading");
        Label items = new Label(order.getItems());
        items.getStyleClass().add("ds-text-muted");
        VBox info = new VBox(2, id, branch, items);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(info, spacer, new StatusBadge(order.getStatus()));

        VBox timeline = new VBox(0);
        for (int i = 0; i < order.getTimeline().size(); i++) {
            TimelineStep step = order.getTimeline().get(i);
            HBox row = new HBox(10);
            Region dot = new Region();
            dot.getStyleClass().add("timeline-dot-" + step.getStatus());
            VBox dotBox = new VBox(dot);
            dotBox.setAlignment(Pos.TOP_CENTER);
            if (i < order.getTimeline().size() - 1) {
                Region line = new Region();
                line.getStyleClass().add("timeline-line-" + step.getStatus());
                line.setMinHeight(24);
                line.setPrefWidth(2);
                dotBox.getChildren().add(line);
            }

            Label label = new Label(step.getLabel());
            label.getStyleClass().add("timeline-label-" + step.getStatus());
            VBox stepInfo = new VBox(2, label);
            if (!step.getTime().isEmpty()) {
                Label time = new Label(step.getTime());
                time.getStyleClass().add("timeline-time");
                stepInfo.getChildren().add(time);
            }
            if (!step.getNote().isEmpty()) {
                Label note = new Label(step.getNote());
                note.getStyleClass().add("timeline-note");
                stepInfo.getChildren().add(note);
            }

            row.getChildren().addAll(dotBox, stepInfo);
            timeline.getChildren().add(row);
        }

        card.getChildren().addAll(header, timeline);

        if (order.getStatus().equals("Pending")) {
            Separator sep = new Separator();
            HBox btns = new HBox(8);
            Button approve = new Button("Approve & Dispatch");
            approve.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(approve, Priority.ALWAYS);
            Button reject = new Button("Reject");
            reject.getStyleClass().add("btn-danger");
            reject.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(reject, Priority.ALWAYS);
            btns.getChildren().addAll(approve, reject);
            card.getChildren().addAll(sep, btns);
        }

        return card;
    }

    public VBox getView() { return view; }
}
