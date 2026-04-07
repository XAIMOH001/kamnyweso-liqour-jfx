package com.drinks.utils;

public class Formatters {
    public static String formatKES(int amount) {
        return "KES " + String.format("%,d", amount);
    }

    public static String formatKES(String amount) {
        return "KES " + amount;
    }

    public static String formatPoints(int points) {
        return String.format("%,d", points);
    }

    public static String calculateLoyalty(int subtotal) {
        int pts = subtotal / 10;
        return "+" + pts + " Points";
    }
}
