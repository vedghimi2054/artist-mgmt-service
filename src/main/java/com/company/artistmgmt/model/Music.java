package com.company.artistmgmt.model;

import com.company.artistmgmt.model.general.Genre;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Music {
    private int id;
    private int artistId;
    private String title;
    private String albumName;
    private Genre genre;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
