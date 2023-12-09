package org.example.entity.enumeration;

public enum Priority {
    HIGH,
    MEDIUM,
    LOW;
    public static boolean isPresent(String data) {
        try {
            Enum.valueOf(Priority.class, data);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
