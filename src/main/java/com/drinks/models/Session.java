package com.drinks.models;

import javafx.beans.property.*;

public class Session {
    private final StringProperty name;
    private final StringProperty branch;
    private final StringProperty login;
    private final StringProperty lastActivity;

    public Session(String name, String branch, String login, String lastActivity) {
        this.name = new SimpleStringProperty(name);
        this.branch = new SimpleStringProperty(branch);
        this.login = new SimpleStringProperty(login);
        this.lastActivity = new SimpleStringProperty(lastActivity);
    }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public String getBranch() { return branch.get(); }
    public StringProperty branchProperty() { return branch; }
    public String getLogin() { return login.get(); }
    public StringProperty loginProperty() { return login; }
    public String getLastActivity() { return lastActivity.get(); }
    public StringProperty lastActivityProperty() { return lastActivity; }
}
