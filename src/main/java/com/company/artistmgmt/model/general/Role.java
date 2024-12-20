package com.company.artistmgmt.model.general;

import lombok.Getter;

@Getter
public enum Role {
    SUPER_ADMIN(1),
    ARTIST_MANAGER(2),
    ARTIST(3);

    private final int value;

    Role(int value) {
        this.value = value;
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
}
