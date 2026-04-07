package com.drinks.models;

import javafx.beans.property.*;

public class Drink {
    private final StringProperty name;
    private final StringProperty brand;
    private final IntegerProperty price;
    private final IntegerProperty stock;
    private final StringProperty image;
    private final IntegerProperty casePrice;
    private final IntegerProperty caseUnits;

    public Drink(String name, String brand, int price, int stock, String image) {
        this(name, brand, price, stock, image, 0, 24);
    }

    public Drink(String name, String brand, int price, int stock, String image, int casePrice, int caseUnits) {
        this.name = new SimpleStringProperty(name);
        this.brand = new SimpleStringProperty(brand);
        this.price = new SimpleIntegerProperty(price);
        this.stock = new SimpleIntegerProperty(stock);
        this.image = new SimpleStringProperty(image);
        this.casePrice = new SimpleIntegerProperty(casePrice);
        this.caseUnits = new SimpleIntegerProperty(caseUnits);
    }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public String getBrand() { return brand.get(); }
    public StringProperty brandProperty() { return brand; }
    public int getPrice() { return price.get(); }
    public IntegerProperty priceProperty() { return price; }
    public int getStock() { return stock.get(); }
    public IntegerProperty stockProperty() { return stock; }
    public String getImage() { return image.get(); }
    public StringProperty imageProperty() { return image; }
    public int getCasePrice() { return casePrice.get(); }
    public IntegerProperty casePriceProperty() { return casePrice; }
    public int getCaseUnits() { return caseUnits.get(); }
    public IntegerProperty caseUnitsProperty() { return caseUnits; }
}
