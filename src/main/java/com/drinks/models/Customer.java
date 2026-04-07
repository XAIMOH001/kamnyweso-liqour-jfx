package com.drinks.models;

import javafx.beans.property.*;

public class Customer {
    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty cardId;
    private final StringProperty tier;
    private final IntegerProperty points;
    private final IntegerProperty progress;
    private final StringProperty nextTier;
    private final IntegerProperty target;

    public Customer(String name, String phone, String cardId, String tier, int points, int progress, String nextTier, int target) {
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.cardId = new SimpleStringProperty(cardId);
        this.tier = new SimpleStringProperty(tier);
        this.points = new SimpleIntegerProperty(points);
        this.progress = new SimpleIntegerProperty(progress);
        this.nextTier = new SimpleStringProperty(nextTier);
        this.target = new SimpleIntegerProperty(target);
    }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public String getPhone() { return phone.get(); }
    public StringProperty phoneProperty() { return phone; }
    public String getCardId() { return cardId.get(); }
    public StringProperty cardIdProperty() { return cardId; }
    public String getTier() { return tier.get(); }
    public StringProperty tierProperty() { return tier; }
    public int getPoints() { return points.get(); }
    public IntegerProperty pointsProperty() { return points; }
    public int getProgress() { return progress.get(); }
    public IntegerProperty progressProperty() { return progress; }
    public String getNextTier() { return nextTier.get(); }
    public StringProperty nextTierProperty() { return nextTier; }
    public int getTarget() { return target.get(); }
    public IntegerProperty targetProperty() { return target; }
}
