package com.company.artistmgmt.model;

import com.company.artistmgmt.model.general.Genre;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Music {
    private int id;
    private int artistId;
    private String title;
    private String albumName;
    private Genre genre; // Assuming `Genre` is another enum
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
