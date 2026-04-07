package com.drinks.models;

import javafx.beans.property.*;

public class PendingApproval {
    private final StringProperty name;
    private final StringProperty branch;
    private final StringProperty role;
    private final StringProperty phone;
    private final StringProperty email;
    private final StringProperty date;

    public PendingApproval(String name, String branch, String role, String phone, String email, String date) {
        this.name = new SimpleStringProperty(name);
        this.branch = new SimpleStringProperty(branch);
        this.role = new SimpleStringProperty(role);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.date = new SimpleStringProperty(date);
    }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public String getBranch() { return branch.get(); }
    public StringProperty branchProperty() { return branch; }
    public String getRole() { return role.get(); }
    public StringProperty roleProperty() { return role; }
    public String getPhone() { return phone.get(); }
    public StringProperty phoneProperty() { return phone; }
    public String getEmail() { return email.get(); }
    public StringProperty emailProperty() { return email; }
    public String getDate() { return date.get(); }
    public StringProperty dateProperty() { return date; }
}
