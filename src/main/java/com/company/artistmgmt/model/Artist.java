package com.company.artistmgmt.model;

import com.company.artistmgmt.model.general.Gender;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Artist {
    private int id;
    private String name;
    private LocalDateTime dob;
    private Gender gender;
    private String address;
    private Integer firstReleaseYear;
    private Integer noOfAlbumsReleased;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
