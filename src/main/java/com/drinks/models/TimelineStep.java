package com.drinks.models;

import javafx.beans.property.*;

public class TimelineStep {
    private final StringProperty label;
    private final StringProperty time;
    private final StringProperty note;
    private final StringProperty status;

    public TimelineStep(String label, String time, String note, String status) {
        this.label = new SimpleStringProperty(label);
        this.time = new SimpleStringProperty(time != null ? time : "");
        this.note = new SimpleStringProperty(note != null ? note : "");
        this.status = new SimpleStringProperty(status);
    }

    public String getLabel() { return label.get(); }
    public StringProperty labelProperty() { return label; }
    public String getTime() { return time.get(); }
    public StringProperty timeProperty() { return time; }
    public String getNote() { return note.get(); }
    public StringProperty noteProperty() { return note; }
    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
}
