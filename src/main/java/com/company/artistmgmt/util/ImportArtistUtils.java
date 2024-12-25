package com.company.artistmgmt.util;

import com.company.artistmgmt.model.general.Gender;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ImportArtistUtils {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static int parseNumber(String id) {
        id = getValidteString(id);
        if (NumberUtils.isNumber(id)) {
            return Integer.parseInt(id);
        }
        return 0;
    }

    public static Timestamp parseDOB(String dob) {
        try {
            dob = getValidteString(dob);
            LocalDateTime localDateTime = LocalDateTime.parse(dob, DATE_TIME_FORMATTER);
            return Timestamp.valueOf(localDateTime);        } catch (DateTimeException ex) {
            log.error("Error while parsing date.{}", dob);
            return null;
        }
    }

    public static String getValidteString(String dob) {
        dob = dob.replace("\"", "").trim();
        return dob;
    }

    public static Gender parseGender(String gender) {
        gender = getValidteString(gender);
        return switch (gender) {
            case "MALE" -> Gender.MALE;
            case "FEMALE" -> Gender.FEMALE;
            case "OTHER" -> Gender.OTHER;
            default -> Gender.GENDER_UNSPECIFIED;
        };
    }
}
