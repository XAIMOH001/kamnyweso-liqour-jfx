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

public class TrackStockOrdersController {
    private final VBox view;
    private final FlowPane ordersContainer;
    private final ObservableList<TrackedOrder> allOrders;

    public TrackStockOrdersController() {
        view = new VBox(16);
        allOrders = MockDataService.getInstance().getBranchTrackedOrders();

        Label title = new Label("Stock Orders from HQ");
        title.getStyleClass().add("ds-title");
        Label subtitle = new Label("Showing orders addressed to Nakuru Branch");
        subtitle.getStyleClass().add("ds-subtitle");

        HBox filters = createFilters();
        ordersContainer = new FlowPane();
        ordersContainer.setHgap(16);
        ordersContainer.setVgap(16);

        view.getChildren().addAll(title, subtitle, filters, ordersContainer);
        renderOrders(allOrders);
    }

    private HBox createFilters() {
        HBox row = new HBox(12);
        row.getStyleClass().add("ds-card");
        row.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by ID or items...");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("All Statuses", "Dispatched", "In Transit", "Received");
        statusField.setValue("All Statuses");

        row.getChildren().addAll(searchField, statusField);

        searchField.textProperty().addListener((obs, o, n) -> applyFilters(searchField, statusField));
        statusField.valueProperty().addListener((obs, o, n) -> applyFilters(searchField, statusField));

        return row;
    }

    private void applyFilters(TextField search, ComboBox<String> status) {
        String s = search.getText().toLowerCase();
        String st = status.getValue();
        ordersContainer.getChildren().clear();
        for (TrackedOrder o : allOrders) {
            boolean matchSearch = s.isEmpty() || o.getId().toLowerCase().contains(s) || o.getItems().toLowerCase().contains(s);
            boolean matchStatus = st.equals("All Statuses") || o.getStatus().equals(st);
            if (matchSearch && matchStatus) ordersContainer.getChildren().add(createOrderCard(o));
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
        Label items = new Label(order.getItems());
        items.getStyleClass().add("ds-text-muted");
        VBox info = new VBox(2, id, items);
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

        if (order.getStatus().equals("In Transit")) {
            Separator sep = new Separator();
            Button confirmBtn = new Button("Confirm Receipt");
            confirmBtn.setMaxWidth(Double.MAX_VALUE);
            card.getChildren().addAll(sep, confirmBtn);
        }

        return card;
    }

    public VBox getView() { return view; }
}
