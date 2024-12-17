package com.company.artistmgmt.model.general;

public enum Role {
    SUPER_ADMIN("super_admin"),
    ARTIST_MANAGER("artist_manager"),
    ARTIST("artist");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.getValue().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
}
