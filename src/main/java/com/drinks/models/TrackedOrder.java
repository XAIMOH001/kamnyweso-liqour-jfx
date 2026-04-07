package com.drinks.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TrackedOrder {
    private final StringProperty id;
    private final StringProperty branch;
    private final StringProperty items;
    private final StringProperty status;
    private final ObservableList<TimelineStep> timeline;

    public TrackedOrder(String id, String branch, String items, String status, ObservableList<TimelineStep> timeline) {
        this.id = new SimpleStringProperty(id);
        this.branch = new SimpleStringProperty(branch);
        this.items = new SimpleStringProperty(items);
        this.status = new SimpleStringProperty(status);
        this.timeline = timeline;
    }

    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }
    public String getBranch() { return branch.get(); }
    public StringProperty branchProperty() { return branch; }
    public String getItems() { return items.get(); }
    public StringProperty itemsProperty() { return items; }
    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public ObservableList<TimelineStep> getTimeline() { return timeline; }
}
