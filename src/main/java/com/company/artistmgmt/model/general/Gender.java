package com.company.artistmgmt.model.general;

import lombok.Getter;

@Getter
public enum Gender {
    GENDER_UNSPECIFIED(0),
    MALE(1),
    FEMALE(2),
    OTHER(3);

    private final int value;

    Gender(int value) {
        this.value = value;
    }

    public static boolean isValid(String genderName) {
        if (genderName == null) {
            return true;
        }
        try {
            Gender.valueOf(genderName.toUpperCase());
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    public static Gender fromValue(int value) {
        for (Gender gender : Gender.values()) {
            if (gender.getValue() == value) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid Gender value: " + value);
    }
}
