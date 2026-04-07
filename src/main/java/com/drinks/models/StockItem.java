package com.drinks.models;

import javafx.beans.property.*;

public class StockItem {
    private final StringProperty name;
    private final StringProperty brand;
    private final StringProperty branch;
    private final IntegerProperty stock;
    private final IntegerProperty min;
    private final StringProperty status;
    private final StringProperty restocked;

    public StockItem(String name, String brand, String branch, int stock, int min, String status, String restocked) {
        this.name = new SimpleStringProperty(name);
        this.brand = new SimpleStringProperty(brand);
        this.branch = new SimpleStringProperty(branch);
        this.stock = new SimpleIntegerProperty(stock);
        this.min = new SimpleIntegerProperty(min);
        this.status = new SimpleStringProperty(status);
        this.restocked = new SimpleStringProperty(restocked);
    }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public String getBrand() { return brand.get(); }
    public StringProperty brandProperty() { return brand; }
    public String getBranch() { return branch.get(); }
    public StringProperty branchProperty() { return branch; }
    public int getStock() { return stock.get(); }
    public IntegerProperty stockProperty() { return stock; }
    public int getMin() { return min.get(); }
    public IntegerProperty minProperty() { return min; }
    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public String getRestocked() { return restocked.get(); }
    public StringProperty restockedProperty() { return restocked; }
}
