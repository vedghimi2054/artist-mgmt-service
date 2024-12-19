package com.company.artistmgmt.mapper;

import com.company.artistmgmt.dto.MusicDto;
import com.company.artistmgmt.model.Music;

public class MusicMapper {
    public static MusicDto toMusicDto(Music music) {
        MusicDto dto = new MusicDto();
        dto.setId(music.getId());
        dto.setArtistId(music.getArtistId());
        dto.setTitle(music.getTitle());
        dto.setAlbumName(music.getAlbumName());
        dto.setGenre(music.getGenre());
        dto.setCreatedAt(music.getCreatedAt());
        dto.setUpdatedAt(music.getUpdatedAt());
        return dto;
    }

    public static Music toMusicEntity(MusicDto dto) {
        Music music = new Music();
        music.setId(dto.getId());
        music.setArtistId(dto.getArtistId());
        music.setTitle(dto.getTitle());
        music.setAlbumName(dto.getAlbumName());
        music.setGenre(dto.getGenre());
        return music;
    }
}
