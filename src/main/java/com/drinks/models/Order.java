package com.drinks.models;

import javafx.beans.property.*;

public class Order {
    private final StringProperty id;
    private final StringProperty customer;
    private final StringProperty branch;
    private final StringProperty items;
    private final StringProperty total;
    private final StringProperty status;
    private final StringProperty date;
    private final StringProperty staff;
    private final IntegerProperty loyalty;

    public Order(String id, String customer, String branch, String items, String total, String status, String date) {
        this(id, customer, branch, items, total, status, date, null, 0);
    }

    public Order(String id, String customer, String branch, String items, String total, String status, String date, String staff, int loyalty) {
        this.id = new SimpleStringProperty(id);
        this.customer = new SimpleStringProperty(customer);
        this.branch = new SimpleStringProperty(branch);
        this.items = new SimpleStringProperty(items);
        this.total = new SimpleStringProperty(total);
        this.status = new SimpleStringProperty(status);
        this.date = new SimpleStringProperty(date);
        this.staff = new SimpleStringProperty(staff != null ? staff : "");
        this.loyalty = new SimpleIntegerProperty(loyalty);
    }

    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }
    public String getCustomer() { return customer.get(); }
    public StringProperty customerProperty() { return customer; }
    public String getBranch() { return branch.get(); }
    public StringProperty branchProperty() { return branch; }
    public String getItems() { return items.get(); }
    public StringProperty itemsProperty() { return items; }
    public String getTotal() { return total.get(); }
    public StringProperty totalProperty() { return total; }
    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public String getDate() { return date.get(); }
    public StringProperty dateProperty() { return date; }
    public String getStaff() { return staff.get(); }
    public StringProperty staffProperty() { return staff; }
    public int getLoyalty() { return loyalty.get(); }
    public IntegerProperty loyaltyProperty() { return loyalty; }
}
