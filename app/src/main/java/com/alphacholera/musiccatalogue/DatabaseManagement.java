package com.alphacholera.musiccatalogue;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alphacholera.musiccatalogue.UtilityClasses.Album;
import com.alphacholera.musiccatalogue.UtilityClasses.Artist;
import com.alphacholera.musiccatalogue.UtilityClasses.ArtistAndSong;
import com.alphacholera.musiccatalogue.UtilityClasses.Song;

import java.util.ArrayList;

public class DatabaseManagement extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MusicCatalogueDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String SONGS_TABLE = "SongsTable";
    private static final String SONGS_COLUMN_0= "SongID";
    private static final String SONGS_COLUMN_1 = "SongName";
    private static final String SONGS_COLUMN_2 = "AlbumID";
    private static final String SONGS_COLUMN_3 = "Language";
    private static final String SONGS_COLUMN_4 = "Duration";

    private static final String ALBUM_TABLE = "AlbumTable";
    private static final String ALBUM_COLUMN_0 = "AlbumID";
    private static final String ALBUM_COLUMN_1 = "AlbumName";
    private static final String ALBUM_COLUMN_2 = "Image";
    private static final String ALBUM_COLUMN_3 = "yearOfRelease";

    private static final String ARTIST_TABLE = "ArtistTable";
    private static final String ARTIST_COLUMN_0 = "ArtistID";
    private static final String ARTIST_COLUMN_1 = "ArtistName";
    private static final String ARTIST_COLUMN_2 = "Gender";
    private static final String ARTIST_COLUMN_3 = "Image";

    private static final String ARTIST_AND_SONG_TABLE = "ArtistAndSong";
    private static final String ARTIST_AND_SONG_COLUMN_0 = "SongID";
    private static final String ARTIST_AND_SONG_COLUMN_1 = "ArtistID";

    private static final String USER_TABLE = "UserTable";
    private static final String USER_COLUMN_0 = "Song_ID";
    private static final String USER_COLUMN_1 = "Frequency";

    public DatabaseManagement(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + SONGS_TABLE + " (" + SONGS_COLUMN_0 + " varchar(5) primary key, " + SONGS_COLUMN_1
                + " varchar(40), " + SONGS_COLUMN_2 + " varchar(5), " + SONGS_COLUMN_3 + " varchar(10), " + SONGS_COLUMN_4
                + " number(3), foreign key (" + SONGS_COLUMN_2 + ") references " +
                ALBUM_TABLE + "(" + ALBUM_COLUMN_0 + "))");

        db.execSQL("create table " + ALBUM_TABLE + " (" + ALBUM_COLUMN_0 + " varchar(5) primary key, " + ALBUM_COLUMN_1
                + " varchar(40), " + ALBUM_COLUMN_2 + " varchar(100), " + ALBUM_COLUMN_3 + " number(4))");

        db.execSQL("create table " + ARTIST_TABLE + " (" + ARTIST_COLUMN_0 + " varchar(5) primary key, " + ARTIST_COLUMN_1
                + " varchar(40), " + ARTIST_COLUMN_2 + " varchar(7), " + ARTIST_COLUMN_3 + " varchar(100))");

        db.execSQL("create table " + ARTIST_AND_SONG_TABLE + " (" + ARTIST_AND_SONG_COLUMN_0 + " varchar(5), " + ARTIST_AND_SONG_COLUMN_1
                + " varchar(5), foreign key (" + ARTIST_AND_SONG_COLUMN_0 + ") references " + SONGS_TABLE + "(" + SONGS_COLUMN_0 +
                "), foreign key (" + ARTIST_AND_SONG_COLUMN_1 + ") references " + ARTIST_TABLE + "(" + ARTIST_COLUMN_0 + "))");

        db.execSQL("create table " + USER_TABLE + " (" + USER_COLUMN_0 + " varchar(5), " + USER_COLUMN_1 + " number(4)," +
                " foreign key (" + USER_COLUMN_0 + ") references " + SONGS_TABLE + "(" + SONGS_COLUMN_0 + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addAllDataIntoTables(ArrayList<Song> songsList, ArrayList<Album> albumsList, ArrayList<Artist> artistsList, ArrayList<ArtistAndSong> compositionsList) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Album album : albumsList) {
            db.execSQL("insert into " + ALBUM_TABLE + " values ('" + album.getAlbumID() + "', '" + album.getAlbumName() +
                    "', '" + album.getImageURL() + "', " + album.getYearOfRelease() + ")");
        }

        for (Artist artist : artistsList) {
            db.execSQL("insert into " + ARTIST_TABLE + " values ( '" + artist.getArtistID() + "', '" + artist.getArtistName()
                    +"', '" + artist.getGender() + "', '" + artist.getImageURL() + "')");
        }

        for (Song song : songsList) {
            db.execSQL("insert into " + SONGS_TABLE + " values('" + song.getSongId()+ "', '" + song.getSongName() +
                    "', '" + song.getAlbumID() + "', '" + song.getLanguage() + "', " + song.getDuration() + ")");
        }

        for (ArtistAndSong composition : compositionsList) {
            db.execSQL("insert into " + ARTIST_AND_SONG_TABLE + " values ('" + composition.getSongID() + "', '" +
                    composition.getArtistID() + "')");
        }

    }

    public ArrayList<Song> getAllSongs() {
        Cursor cursor = getReadableDatabase().rawQuery("select * from " + SONGS_TABLE, null);
        ArrayList<Song> songs = new ArrayList<>();
        cursor.moveToFirst();
        songs.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getInt(4)));
        while (cursor.moveToNext()) {
            songs.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4)));
        }
        return songs;
    }

    public ArrayList<Album> getAllAlbums() {
        Cursor cursor = getReadableDatabase().rawQuery("select * from " + ALBUM_TABLE, null);
        ArrayList<Album> albums = new ArrayList<>();
        cursor.moveToFirst();
        albums.add(new Album(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getInt(3)));
        while (cursor.moveToNext()) {
            albums.add(new Album(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3)));
        }
        return albums;
    }

    public ArrayList<Artist> getAllArtists() {
        Cursor cursor = getReadableDatabase().rawQuery("select * from " + ARTIST_TABLE, null);
        ArrayList<Artist> artists = new ArrayList<>();
        cursor.moveToFirst();
        artists.add(new Artist(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3)));
        while (cursor.moveToNext()) {
            artists.add(new Artist(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3)));
        }
        return artists;
    }

    public String getAlbumName(String albumID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + ALBUM_COLUMN_1 + " from " + ALBUM_TABLE + " where " + ALBUM_COLUMN_0 + " = '" + albumID + "'", null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public String getAlbumURL(String albumID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.rawQuery("select " + ALBUM_COLUMN_2 + " from " + ALBUM_TABLE + " where "
                + ALBUM_COLUMN_0 + " = '" + albumID + "'", null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public ArrayList<String> getArtistsOfASong(String songID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + ARTIST_COLUMN_1 + " from " + ARTIST_TABLE +" where " +
                ARTIST_COLUMN_0 + " in (select distinct " + ARTIST_AND_SONG_COLUMN_1 + " from " + ARTIST_AND_SONG_TABLE
                + " where " + ARTIST_AND_SONG_COLUMN_0 + " = '" + songID + "')", null);
        ArrayList<String> artists = new ArrayList<>();
        while (cursor.moveToNext())
            artists.add(cursor.getString(0));
        return artists;
    }

    public int getFrequencyOfSong(String songID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + USER_COLUMN_1 + " from " + USER_TABLE + " where "
                + USER_COLUMN_0 + " = '" + songID + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
            return cursor.getInt(0);
        return 0;

    }

    public ArrayList<Song> getSongsOfAnAlbum(String albumID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Song> songs = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + SONGS_TABLE + " where " + SONGS_COLUMN_2 + " = '" + albumID + "'", null);
        cursor.moveToFirst();
        songs.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getInt(4)));
        while (cursor.moveToNext()) {
            songs.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4)));
        }
        return songs;
    }

    public ArrayList<Song> getSongsOfAnArtist(String artistID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Song> songs = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + SONGS_TABLE + " where " + SONGS_COLUMN_0 + " in ("
                + "select distinct " + ARTIST_AND_SONG_COLUMN_0 + " from " + ARTIST_AND_SONG_TABLE + " where "
                + ARTIST_AND_SONG_COLUMN_1 + " = '" + artistID + "')", null);
        cursor.moveToFirst();
        songs.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getInt(4)));
        while (cursor.moveToNext()) {
            songs.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4)));
        }
        return songs;
    }

    public int getNumberOfSongsOfAnAlbum(String albumID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + SONGS_TABLE + " where " + SONGS_COLUMN_2 + " = '" + albumID + "'", null);
        cursor.moveToNext();
        return cursor.getInt(0);
    }

    public int getNumberOfSongsOfAnArtist(String artistID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + ARTIST_AND_SONG_TABLE + " where " + ARTIST_AND_SONG_COLUMN_1
        + " = '" + artistID + "'", null);
        cursor.moveToNext();
        return cursor.getInt(0);
    }

    public String[] getSongInfo(String songID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] strings = new String[5];
        Cursor cursor = db.rawQuery("select * from " + SONGS_TABLE + " where " + SONGS_COLUMN_0 + " = '" + songID + "'", null);
        if (cursor.moveToFirst()) {
            for (int i = 0; i <= 3; i++)
                strings[i] = cursor.getString(i);
            strings[4] = String.valueOf(cursor.getInt(4));
        }
        return strings;
    }

    public String[] getAlbumInfo(String albumID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] strings = new String[4];
        Cursor cursor = db.rawQuery("select * from " + ALBUM_TABLE + " where " + ALBUM_COLUMN_0 + " = '" + albumID + "'", null);
        if (cursor.moveToFirst()) {
            for (int i = 0; i <= 2; i++)
                strings[i] = cursor.getString(i);
            strings[3] = String.valueOf(cursor.getInt(3));
        }
        return strings;
    }

    public String[] getArtistInfo(String artistID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] strings = new String[4];
        Cursor cursor = db.rawQuery("select * from " + ARTIST_TABLE + " where " + ARTIST_COLUMN_0 + " = '" + artistID + "'", null);
        if (cursor.moveToFirst()) {
            for (int i = 0; i <= 3; i++)
                strings[i] = cursor.getString(i);
        }
        return strings;
    }

    public int updateUserData(String songID) {
        SQLiteDatabase db1 = getReadableDatabase();
        SQLiteDatabase db2 = getWritableDatabase();
        int frequency;
        Cursor cursor1 = db1.rawQuery("select " + USER_COLUMN_1 + " from " + USER_TABLE + " where "
                + USER_COLUMN_0 + " = '" + songID + "'", null);
        if (cursor1.getCount() > 0) {
            Cursor cursor2 = db1.rawQuery("select " + USER_COLUMN_1 + " from " + USER_TABLE
                    + " where " + USER_COLUMN_0 + " = '" + songID + "'", null);
            cursor2.moveToFirst();
            frequency = cursor2.getInt(0) + 1;
            db2.execSQL("update " + USER_TABLE + " set " + USER_COLUMN_1 + " = " + frequency + " where " + USER_COLUMN_0 + " = '" + songID + "'");
        } else {
            frequency = 1;
            db2.execSQL("insert into " + USER_TABLE + " values('" + songID + "', " + frequency + ")");
        }
        return frequency;
    }

    public void insertIntoUserTable(Long frequency, String songID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into " + USER_TABLE + " values( '" + songID + "', " + frequency + ")");
    }
}
