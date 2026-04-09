package com.drinks.utils;

public class PinValidator {
    private static final String SUPERVISOR_PIN = "1234";
    private static int failedAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;

    public static boolean validate(String pin) {
        if (pin == null || pin.isEmpty()) return false;
        if (failedAttempts >= MAX_ATTEMPTS) return false;

        if (pin.equals(SUPERVISOR_PIN)) {
            failedAttempts = 0;
            return true;
        } else {
            failedAttempts++;
            return false;
        }
    }

    public static int getFailedAttempts() {
        return failedAttempts;
    }

    public static int getMaxAttempts() {
        return MAX_ATTEMPTS;
    }

    public static int getRemainingAttempts() {
        return Math.max(0, MAX_ATTEMPTS - failedAttempts);
    }

    public static boolean isLockedOut() {
        return failedAttempts >= MAX_ATTEMPTS;
    }

    public static void resetAttempts() {
        failedAttempts = 0;
    }
}
