package com.company.artistmgmt.model.general;

public enum Role {
    SUPER_ADMIN(1),
    ARTIST_MANAGER(2),
    ARTIST(3);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static boolean isValid(String roleName) {
        if (roleName == null) {
            return false;
        }
        try {
            Role.valueOf(roleName.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static Role fromValue(int value) {
        for (Role role : Role.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role value: " + value);
    }

    public static Role parseRole(String roleStr) {
        if (roleStr == null) {
            throw new IllegalArgumentException("Role value cannot be null.");
        }
        return switch (roleStr.toUpperCase()) {
            case "SUPER_ADMIN" -> SUPER_ADMIN;
            case "ARTIST_MANAGER" -> ARTIST_MANAGER;
            case "ARTIST" -> ARTIST;
            default -> throw new IllegalArgumentException("Invalid role string: " + roleStr);
        };
    }
}
