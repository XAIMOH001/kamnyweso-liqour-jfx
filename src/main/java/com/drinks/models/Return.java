package com.drinks.models;

import javafx.beans.property.*;

public class Return {
    private final StringProperty id;
    private final StringProperty branch;
    private final StringProperty item;
    private final IntegerProperty qty;
    private final StringProperty reason;
    private final StringProperty status;
    private final StringProperty date;
    private final StringProperty response;

    public Return(String id, String branch, String item, int qty, String reason, String status, String date) {
        this(id, branch, item, qty, reason, status, date, "—");
    }

    public Return(String id, String branch, String item, int qty, String reason, String status, String date, String response) {
        this.id = new SimpleStringProperty(id);
        this.branch = new SimpleStringProperty(branch);
        this.item = new SimpleStringProperty(item);
        this.qty = new SimpleIntegerProperty(qty);
        this.reason = new SimpleStringProperty(reason);
        this.status = new SimpleStringProperty(status);
        this.date = new SimpleStringProperty(date);
        this.response = new SimpleStringProperty(response);
    }

    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }
    public String getBranch() { return branch.get(); }
    public StringProperty branchProperty() { return branch; }
    public String getItem() { return item.get(); }
    public StringProperty itemProperty() { return item; }
    public int getQty() { return qty.get(); }
    public IntegerProperty qtyProperty() { return qty; }
    public String getReason() { return reason.get(); }
    public StringProperty reasonProperty() { return reason; }
    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public String getDate() { return date.get(); }
    public StringProperty dateProperty() { return date; }
    public String getResponse() { return response.get(); }
    public StringProperty responseProperty() { return response; }
}
