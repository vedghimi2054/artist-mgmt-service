package com.company.artistmgmt.repository;

public class QueryConst {
    public static final String QUERY = "Query:{}";
    public static final String USER_FETCH_COLUMN_QUERY = " id, first_name, last_name, email, password, phone, dob, gender, address, role,created_at, updated_at";
    public static final String ARTIST_FETCH_COLUMN_QUERY = " id, name, dob, gender, address, first_release_year, no_of_albums_released, created_at, updated_at";
    public static final String MUSIC_FETCH_COLUMN_QUERY = " id, artist_id, title, album_name, genre, created_at, updated_at";
    public static final String USER_TABLE = " user ";
    public static final String ARTIST_TABLE = " artist ";
    public static final String MUSIC_TABLE = " music ";
    public static final String INSERT = " INSERT INTO  ";
    public static final String SELECT = " SELECT  ";
    public static final String FROM = " FROM  ";
    public static final String UPDATE = " UPDATE  ";
    public static final String DELETE = " DELETE  ";
    public static final String WHERE = " WHERE  ";

}
