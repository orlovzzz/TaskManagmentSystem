package org.example.entity.enumeration;

public enum Status {
    AWAITING,
    IN_PROCESSING,
    COMPLETED;

    public static boolean isPresent(String data) {
        try {
            Enum.valueOf(Status.class, data);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
