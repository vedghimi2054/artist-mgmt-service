package com.company.artistmgmt.dto;

import com.company.artistmgmt.model.general.Genre;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MusicDto {
    private int id;
    private int artistId;
    private String title;
    private String albumName;
    private Genre genre;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}