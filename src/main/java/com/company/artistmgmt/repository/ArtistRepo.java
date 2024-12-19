package com.company.artistmgmt.repository;

import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.Artist;

import java.util.List;

public interface ArtistRepo {
    List<Artist> getAllArtists(int pageNo, int pageSize) throws ArtistException;

    Artist createArtist(Artist artistEntity) throws ArtistException;

    boolean checkArtistExistsById(int id) throws ArtistException;

    boolean updateArtist(int id, Artist ArtistEntity);

    boolean deleteArtist(int id);

    Artist getArtistById(int id) throws ArtistException;

}
