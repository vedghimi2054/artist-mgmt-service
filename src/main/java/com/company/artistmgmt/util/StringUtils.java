package com.company.artistmgmt.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$");

    private StringUtils() {
    }

    public static String getNotNullString(String str) {
        return null == str ? "" : str;
    }

    public static boolean isBlank(String data) {
        return org.apache.commons.lang3.StringUtils.isBlank(data);
    }

    public static boolean isNotBlank(String data) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(data);
    }

    public static boolean isValidEmail(String email) {
        if (isBlank(email)) {
            return false;
        } else {
            Matcher matcher = EMAIL_PATTERN.matcher(email);
            return matcher.matches();
        }
    }

}
