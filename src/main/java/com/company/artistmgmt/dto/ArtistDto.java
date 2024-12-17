package com.company.artistmgmt.dto;


import com.company.artistmgmt.model.general.Gender;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArtistDto {
    private int id;
    private String name;
    private LocalDateTime dob;
    private Gender gender;
    private String address;
    private int firstReleaseYear;
    private int noOfAlbumsReleased;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}