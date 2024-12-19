package com.company.artistmgmt.repository;

import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.Music;

import java.util.List;

public interface MusicRepo {
    List<Music> getSongsByArtist(int artistId, int pageNo, int pageSize) throws ArtistException;

    boolean createSongForArtist(int artistId, Music musicEntity) throws ArtistException;

    boolean checkMusicExistsById(int id, int artistId) throws ArtistException;

    boolean updateSongForArtist(int artistId, int id, Music musicEntity) throws ArtistException;

    boolean deleteSongForArtist(int artistId, int id) throws ArtistException;

    Music getSongById(int id, int artistId) throws ArtistException;

}
