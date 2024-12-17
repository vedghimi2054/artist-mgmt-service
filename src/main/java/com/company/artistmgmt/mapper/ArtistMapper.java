package com.company.artistmgmt.mapper;

import com.company.artistmgmt.dto.ArtistDto;
import com.company.artistmgmt.model.Artist;

public class ArtistMapper {
    public static ArtistDto toArtistDto(Artist artist) {
        ArtistDto dto = new ArtistDto();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        dto.setDob(artist.getDob());
        dto.setGender(artist.getGender());
        dto.setAddress(artist.getAddress());
        dto.setFirstReleaseYear(artist.getFirstReleaseYear());
        dto.setNoOfAlbumsReleased(artist.getNoOfAlbumsReleased());
        return dto;
    }

    public static Artist toArtistEntity(ArtistDto dto) {
        Artist artist = new Artist();
        artist.setId(dto.getId());
        artist.setName(dto.getName());
        artist.setDob(dto.getDob());
        artist.setGender(dto.getGender());
        artist.setAddress(dto.getAddress());
        artist.setFirstReleaseYear(dto.getFirstReleaseYear());
        artist.setNoOfAlbumsReleased(dto.getNoOfAlbumsReleased());
        return artist;

    }
}
