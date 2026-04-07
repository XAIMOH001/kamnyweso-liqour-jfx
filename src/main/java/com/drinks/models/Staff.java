package com.drinks.models;

import javafx.beans.property.*;

public class Staff {
    private final StringProperty name;
    private final StringProperty id;
    private final StringProperty branch;
    private final StringProperty role;
    private final StringProperty status;
    private final StringProperty lastLogin;

    public Staff(String name, String id, String branch, String role, String status, String lastLogin) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.branch = new SimpleStringProperty(branch);
        this.role = new SimpleStringProperty(role);
        this.status = new SimpleStringProperty(status);
        this.lastLogin = new SimpleStringProperty(lastLogin);
    }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }
    public String getBranch() { return branch.get(); }
    public StringProperty branchProperty() { return branch; }
    public String getRole() { return role.get(); }
    public StringProperty roleProperty() { return role; }
    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public String getLastLogin() { return lastLogin.get(); }
    public StringProperty lastLoginProperty() { return lastLogin; }
}
