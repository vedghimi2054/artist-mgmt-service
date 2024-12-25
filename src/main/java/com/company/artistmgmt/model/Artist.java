package com.company.artistmgmt.model;

import com.company.artistmgmt.model.general.Gender;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Artist {
    private int id;
    private String name;
    private Timestamp dob;
    private Gender gender;
    private String address;
    private Integer firstReleaseYear;
    private Integer noOfAlbumsReleased;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
