package com.company.artistmgmt.repository;

public class QueryConst {
    public static final String QUERY = "Query:{}";
    public static final String TOTAL_COUNT = "totalCount";
    public static final String TOTAL_PAGES = "totalPages";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String PAGE_SIZE = "pageSize";
    public static final String USER_SUCCESS_MSG = "User Fetch successfully";
    public static final String ARTIST_SUCCESS_MSG = "Artist Fetch successfully";
    public static final String MUSIC_SUCCESS_MSG = "Music Fetch successfully";
    public static final String USER_FETCH_COLUMN_QUERY = "id, first_name, last_name, email, password, phone, dob, gender, address, role,created_at, updated_at";
    public static final String ARTIST_FETCH_COLUMN_QUERY = "a.id, a.name, a.dob, a.gender, a.address, a.first_release_year, a.no_of_albums_released, a.created_at, a.updated_at";
    public static final String MUSIC_FETCH_COLUMN_QUERY = "m.id, m.artist_id, m.title, m.album_name, m.genre, m.created_at, m.updated_at";
    public static final String USER_TABLE = " user as a";
    public static final String ARTIST_TABLE = " artist as a";
    public static final String MUSIC_TABLE = " music as m";
    public static final String INSERT = " INSERT INTO  ";
    public static final String SELECT = " SELECT  ";
    public static final String FROM = " FROM  ";
    public static final String UPDATE = " UPDATE  ";
    public static final String DELETE = " DELETE  ";
    public static final String WHERE = " WHERE  ";

}
