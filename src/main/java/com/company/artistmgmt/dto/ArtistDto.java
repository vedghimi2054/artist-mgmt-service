package com.company.artistmgmt.dto;


import com.company.artistmgmt.model.general.Gender;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ArtistDto {
    private int id;
    private String name;
    private Timestamp dob;
    private Gender gender;
    private String address;
    private int firstReleaseYear;
    private int noOfAlbumsReleased;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}