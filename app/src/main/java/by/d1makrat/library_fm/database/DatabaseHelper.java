package by.d1makrat.library_fm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.model.Album;
import by.d1makrat.library_fm.model.Artist;
import by.d1makrat.library_fm.model.Scrobble;
import by.d1makrat.library_fm.model.TopAlbums;
import by.d1makrat.library_fm.model.TopArtists;
import by.d1makrat.library_fm.model.TopTracks;
import by.d1makrat.library_fm.model.Track;

import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_ALBUM;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_ARTIST;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_DATE;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_ID;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_IMAGEURI;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_PERIOD;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_PLAYCOUNT;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_RANK;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_TRACK;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.DATABASE_SCROBBLES_TABLE;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.DATABASE_TOP_ALBUMS_TABLE;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.DATABASE_TOP_ARTISTS_TABLE;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.DATABASE_TOP_TRACKS_TABLE;
import static by.d1makrat.library_fm.Constants.EMPTY_STRING;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Last.fmLibraryViewer.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_SCROBBLES_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_SCROBBLES_TABLE + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TRACK + " TEXT COLLATE NOCASE, " +
                    COLUMN_ARTIST + " TEXT COLLATE NOCASE, " +
                    COLUMN_ALBUM + " TEXT COLLATE NOCASE, " +
                    COLUMN_DATE + " INT UNSIGNED, " +
                    COLUMN_IMAGEURI + " TEXT COLLATE NOCASE, " +
                    "CONSTRAINT uniqueScrobble UNIQUE (" + COLUMN_TRACK + ", " +  COLUMN_ARTIST + ", " + COLUMN_DATE +
                    "));";
    private static final String CREATE_TOP_ALBUMS_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TOP_ALBUMS_TABLE + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RANK + " INT UNSIGNED, " +
                    COLUMN_ARTIST + " TEXT, " +
                    COLUMN_ALBUM + " TEXT, " +
                    COLUMN_PLAYCOUNT + " TEXT, " +
                    COLUMN_IMAGEURI + " TEXT, " +
                    COLUMN_PERIOD + " TEXT, " +
                    "CONSTRAINT uniqueAlbum UNIQUE (" + COLUMN_RANK + ", " +  COLUMN_ARTIST + ", " + COLUMN_ALBUM + ", " + COLUMN_PERIOD +
                    "));";
    private static final String CREATE_TOP_ARTISTS_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TOP_ARTISTS_TABLE + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RANK + " INT UNSIGNED, " +
                    COLUMN_ARTIST + " TEXT, " +
                    COLUMN_PLAYCOUNT + " TEXT, " +
                    COLUMN_IMAGEURI + " TEXT, " +
                    COLUMN_PERIOD + " TEXT, " +
                    "CONSTRAINT uniqueArtist UNIQUE (" + COLUMN_RANK + ", " +  COLUMN_ARTIST + ", " + COLUMN_PERIOD +
                    "));";
    private static final String CREATE_TOP_TRACKS_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TOP_TRACKS_TABLE + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RANK + " INT UNSIGNED, " +
                    COLUMN_ARTIST + " TEXT, " +
                    COLUMN_TRACK + " TEXT, " +
                    COLUMN_PLAYCOUNT + " TEXT, " +
                    COLUMN_IMAGEURI + " TEXT, " +
                    COLUMN_PERIOD + " TEXT, " +
                    "CONSTRAINT uniqueTrack UNIQUE (" + COLUMN_RANK + ", " +  COLUMN_ARTIST + ", " + COLUMN_TRACK + ", " + COLUMN_PERIOD +
                    "));";

    private static final String SORTING_DATE_DESCENDING = COLUMN_DATE + " DESC";
    private static final String DATE_INTERVAL_CONDITION = COLUMN_DATE + " BETWEEN ? AND ?";
    private static final String ARTIST_EQUALS_CONDITION = COLUMN_ARTIST + " = ?";
    private static final String ARTIST_EQUALS_AND_DATE_INTERVAL_CONDITION = ARTIST_EQUALS_CONDITION + " AND " + DATE_INTERVAL_CONDITION;
    private static final String ARTIST_AND_TRACK_EQUALS_CONDITION = ARTIST_EQUALS_CONDITION + " AND " + COLUMN_TRACK + " = ?";
    private static final String ARTIST_AND_TRACK_EQUALS_AND_DATE_INTERVAL_CONDITION = ARTIST_AND_TRACK_EQUALS_CONDITION + " AND " + DATE_INTERVAL_CONDITION;
    private static final String ARTIST_AND_ALBUM_EQUALS_CONDITION = ARTIST_EQUALS_CONDITION + " AND " + COLUMN_ALBUM + " = ?";
    private static final String ARTIST_AND_ALBUM_EQUALS_AND_DATE_INTERVAL_CONDITION = ARTIST_AND_ALBUM_EQUALS_CONDITION + " AND " +DATE_INTERVAL_CONDITION;

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgradeScrobblesTable(db);
    }

    private void createTables(SQLiteDatabase database) throws SQLException {
        database.beginTransaction();
        try {
            database.execSQL(CREATE_SCROBBLES_TABLE_QUERY);
            database.execSQL(CREATE_TOP_TRACKS_TABLE_QUERY);
            database.execSQL(CREATE_TOP_ARTISTS_TABLE_QUERY);
            database.execSQL(CREATE_TOP_ALBUMS_TABLE_QUERY);

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    private void upgradeScrobblesTable(SQLiteDatabase database) throws SQLException {
        final String TEMP_TABLE = "Scrobbles_temp";
        final String CREATE_SECOND_SCROBBLES_TABLE_WITH_COLLATE_NOCASE_QUERY =
                "CREATE TABLE IF NOT EXISTS " + TEMP_TABLE + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TRACK + " TEXT COLLATE NOCASE, " +
                        COLUMN_ARTIST + " TEXT COLLATE NOCASE, " +
                        COLUMN_ALBUM + " TEXT COLLATE NOCASE, " +
                        COLUMN_DATE + " INT UNSIGNED, " +
                        COLUMN_IMAGEURI + " TEXT COLLATE NOCASE, " +
                        "CONSTRAINT uniqueScrobble UNIQUE (" + COLUMN_TRACK + ", " +  COLUMN_ARTIST + ", " + COLUMN_DATE +
                        "));";
        final String COPY_ROWS_INTO_TEMP_TABLE = "INSERT OR REPLACE INTO " + TEMP_TABLE + " SELECT _id, track, artist, album, date, imageUri FROM " + DATABASE_SCROBBLES_TABLE;
        final String DROP_FIRST_SCROBBLES_TABLE = "DROP TABLE " + DATABASE_SCROBBLES_TABLE;
        String CREATE_FIRST_SCROBBLES_TABLE_WITH_COLLATE_NOCASE_QUERY =
                "CREATE TABLE IF NOT EXISTS " + DATABASE_SCROBBLES_TABLE + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TRACK + " TEXT COLLATE NOCASE, " +
                        COLUMN_ARTIST + " TEXT COLLATE NOCASE, " +
                        COLUMN_ALBUM + " TEXT COLLATE NOCASE, " +
                        COLUMN_DATE + " INT UNSIGNED, " +
                        COLUMN_IMAGEURI + " TEXT COLLATE NOCASE, " +
                        "CONSTRAINT uniqueScrobble UNIQUE (" + COLUMN_TRACK + ", " +  COLUMN_ARTIST + ", " + COLUMN_DATE +
                        "));";
        String COPY_ROWS_INTO_SCROBBLES_TABLE = "INSERT OR REPLACE INTO " + DATABASE_SCROBBLES_TABLE + " SELECT _id, track, artist, album, date, imageUri FROM " + TEMP_TABLE;
        String DROP_SECOND_SCROBBLES_TABLE = "DROP TABLE " + TEMP_TABLE;

        database.beginTransaction();
        try {
            database.execSQL(CREATE_SECOND_SCROBBLES_TABLE_WITH_COLLATE_NOCASE_QUERY);
            database.execSQL(COPY_ROWS_INTO_TEMP_TABLE);
            database.execSQL(DROP_FIRST_SCROBBLES_TABLE);
            database.execSQL(CREATE_FIRST_SCROBBLES_TABLE_WITH_COLLATE_NOCASE_QUERY);
            database.execSQL(COPY_ROWS_INTO_SCROBBLES_TABLE);
            database.execSQL(DROP_SECOND_SCROBBLES_TABLE);

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    private void deleteRecordsFromTable(String pTableName, @Nullable String pPeriod) throws SQLException {
        final SQLiteDatabase database = getWritableDatabase();

        database.beginTransaction();

        try {
            if (pPeriod != null) {
                database.delete(pTableName, COLUMN_PERIOD + " = ?", new String[]{pPeriod});
            }
            else {
                database.delete(pTableName, null, null);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void insertScrobbles(final List<Scrobble> items) throws SQLException {
        final SQLiteDatabase database = getWritableDatabase();

        database.beginTransaction();

        try {
            for (Scrobble item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_TRACK, item.getTrackTitle());
                contentValues.put(COLUMN_ARTIST, item.getArtist());
                contentValues.put(COLUMN_ALBUM, item.getAlbum());
                contentValues.put(COLUMN_DATE, item.getRawDate());
                contentValues.put(COLUMN_IMAGEURI, item.getImageUrl());

                database.insertWithOnConflict(DATABASE_SCROBBLES_TABLE, EMPTY_STRING, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<Scrobble> getScrobbles(int pPage, Long pFrom, Long pTo) throws SQLException {
        final SQLiteDatabase database = getReadableDatabase();
        List<Scrobble> scrobbles = new ArrayList<>();
        Cursor cursor = null;

        try {
            if (pFrom == null && pTo == null)
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, null, null, null, null, SORTING_DATE_DESCENDING);
            else
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, DATE_INTERVAL_CONDITION, new String[]{String.valueOf(pFrom), String.valueOf(pTo)}, null, null, SORTING_DATE_DESCENDING);

            if (cursor != null) {
                if (cursor.moveToPosition((pPage - 1) * AppContext.getInstance().getLimit())){
                    int trackTitleColumn = cursor.getColumnIndexOrThrow(COLUMN_TRACK);
                    int artistColumn = cursor.getColumnIndexOrThrow(COLUMN_ARTIST);
                    int albumColumn = cursor.getColumnIndexOrThrow(COLUMN_ALBUM);
                    int unixDateColumn = cursor.getColumnIndexOrThrow(COLUMN_DATE);
                    int imageUriColumn = cursor.getColumnIndexOrThrow(COLUMN_IMAGEURI);
                    do {
                        String trackTitle = cursor.getString(trackTitleColumn);
                        String artist = cursor.getString(artistColumn);
                        String album = cursor.getString(albumColumn);
                        long unixDate = cursor.getLong(unixDateColumn);
                        String imageUri = cursor.getString(imageUriColumn);

                        scrobbles.add(new Scrobble(artist, trackTitle, album, imageUri, unixDate));
                    }
                    while (cursor.moveToNext() && scrobbles.size() < AppContext.getInstance().getLimit());
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return scrobbles;
    }

    public List<Scrobble> getScrobblesOfArtist(String pArtist, int pPage, Long pFrom, Long pTo) throws SQLException {
        final SQLiteDatabase database = getReadableDatabase();
        List<Scrobble> scrobbles = new ArrayList<>();
        Cursor cursor = null;

        try {
            if (pFrom == null && pTo == null)
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_EQUALS_CONDITION, new String[]{pArtist}, null, null, SORTING_DATE_DESCENDING);
            else
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_EQUALS_AND_DATE_INTERVAL_CONDITION, new String[]{pArtist, String.valueOf(pFrom), String.valueOf(pTo)}, null, null, SORTING_DATE_DESCENDING);


            if (cursor != null) {
                if (cursor.moveToPosition((pPage - 1) * AppContext.getInstance().getLimit())){
                    int trackTitleColumn = cursor.getColumnIndexOrThrow(COLUMN_TRACK);
                    int artistColumn = cursor.getColumnIndexOrThrow(COLUMN_ARTIST);
                    int albumColumn = cursor.getColumnIndexOrThrow(COLUMN_ALBUM);
                    int unixDateColumn = cursor.getColumnIndexOrThrow(COLUMN_DATE);
                    int imageUriColumn = cursor.getColumnIndexOrThrow(COLUMN_IMAGEURI);
                    do {
                        String trackTitle = cursor.getString(trackTitleColumn);
                        String artist = cursor.getString(artistColumn);
                        String album = cursor.getString(albumColumn);
                        long unixDate = cursor.getLong(unixDateColumn);
                        String imageUri = cursor.getString(imageUriColumn);

                        scrobbles.add(new Scrobble(artist, trackTitle, album, imageUri, unixDate));
                    }
                    while (cursor.moveToNext() && scrobbles.size() < AppContext.getInstance().getLimit());
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return scrobbles;
    }

    public List<Scrobble> getScrobblesOfTrack(String pArtist, String pTrack, Long pFrom, Long pTo) throws SQLException {
        final SQLiteDatabase database = getReadableDatabase();
        List<Scrobble> scrobbles = new ArrayList<>();
        Cursor cursor = null;

        try {
            if (pFrom == null && pTo == null)
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_AND_TRACK_EQUALS_CONDITION, new String[]{pArtist, pTrack}, null, null, SORTING_DATE_DESCENDING);
            else
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_AND_TRACK_EQUALS_AND_DATE_INTERVAL_CONDITION, new String[]{pArtist, pTrack, String.valueOf(pFrom), String.valueOf(pTo)}, null, null, SORTING_DATE_DESCENDING);

            if (cursor != null) {
                if (cursor.moveToFirst()){
                    int trackTitleColumn = cursor.getColumnIndexOrThrow(COLUMN_TRACK);
                    int artistColumn = cursor.getColumnIndexOrThrow(COLUMN_ARTIST);
                    int albumColumn = cursor.getColumnIndexOrThrow(COLUMN_ALBUM);
                    int unixDateColumn = cursor.getColumnIndexOrThrow(COLUMN_DATE);
                    int imageUriColumn = cursor.getColumnIndexOrThrow(COLUMN_IMAGEURI);
                    do {
                        String trackTitle = cursor.getString(trackTitleColumn);
                        String artist = cursor.getString(artistColumn);
                        String album = cursor.getString(albumColumn);
                        long unixDate = cursor.getLong(unixDateColumn);
                        String imageUri = cursor.getString(imageUriColumn);

                        scrobbles.add(new Scrobble(artist, trackTitle, album, imageUri, unixDate));
                    }
                    while (cursor.moveToNext());
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return scrobbles;
    }

    public List<Scrobble> getScrobblesOfAlbum(String pArtist, String pAlbum, Long pFrom, Long pTo) throws SQLException {
        final SQLiteDatabase database = getReadableDatabase();
        List<Scrobble> scrobbles = new ArrayList<>();
        Cursor cursor = null;

        try {
            if (pFrom == null && pTo == null)
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_AND_ALBUM_EQUALS_CONDITION, new String[]{pArtist, pAlbum}, null, null, SORTING_DATE_DESCENDING);
            else
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_AND_ALBUM_EQUALS_AND_DATE_INTERVAL_CONDITION, new String[]{pArtist, pAlbum, String.valueOf(pFrom), String.valueOf(pTo)}, null, null, SORTING_DATE_DESCENDING);

            if (cursor != null) {
                if (cursor.moveToFirst()){
                    int trackTitleColumn = cursor.getColumnIndexOrThrow(COLUMN_TRACK);
                    int artistColumn = cursor.getColumnIndexOrThrow(COLUMN_ARTIST);
                    int albumColumn = cursor.getColumnIndexOrThrow(COLUMN_ALBUM);
                    int unixDateColumn = cursor.getColumnIndexOrThrow(COLUMN_DATE);
                    int imageUriColumn = cursor.getColumnIndexOrThrow(COLUMN_IMAGEURI);
                    do {
                        String trackTitle = cursor.getString(trackTitleColumn);
                        String artist = cursor.getString(artistColumn);
                        String album = cursor.getString(albumColumn);
                        long unixDate = cursor.getLong(unixDateColumn);
                        String imageUri = cursor.getString(imageUriColumn);

                        scrobbles.add(new Scrobble(artist, trackTitle, album, imageUri, unixDate));
                    }
                    while (cursor.moveToNext());
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return scrobbles;
    }

    public void deleteScrobbles() throws SQLException {
        deleteRecordsFromTable(DATABASE_SCROBBLES_TABLE, null);
    }

    public void insertTopAlbums(final List<Album> items, final String pPeriod) throws SQLException {
        final SQLiteDatabase database = getWritableDatabase();

        database.beginTransaction();

        try {
            for (Album item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_RANK, item.getRank());
                contentValues.put(COLUMN_ARTIST, item.getArtistName());
                contentValues.put(COLUMN_ALBUM, item.getTitle());
                contentValues.put(COLUMN_PLAYCOUNT, item.getPlayCount());
                contentValues.put(COLUMN_IMAGEURI, item.getImageUrl());
                contentValues.put(COLUMN_PERIOD, pPeriod);

                database.insertWithOnConflict(DATABASE_TOP_ALBUMS_TABLE, EMPTY_STRING, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public TopAlbums getTopAlbums(String pPeriod, int pPage) throws SQLException {
        final SQLiteDatabase database = getReadableDatabase();
        List<Album> albums = new ArrayList<>();
        int albumsCount;
        Cursor cursor = null;

        try {
            cursor = database.query(DATABASE_TOP_ALBUMS_TABLE, null, COLUMN_PERIOD + " = ?", new String[]{pPeriod}, null, null, COLUMN_RANK);
            albumsCount = cursor.getCount();

            if (cursor.moveToFirst()) {
                if (cursor.moveToPosition((pPage - 1) * AppContext.getInstance().getLimit())){
                    int rankColumn = cursor.getColumnIndexOrThrow(COLUMN_RANK);
                    int artistColumn = cursor.getColumnIndexOrThrow(COLUMN_ARTIST);
                    int albumColumn = cursor.getColumnIndexOrThrow(COLUMN_ALBUM);
                    int playCountColumn = cursor.getColumnIndexOrThrow(COLUMN_PLAYCOUNT);
                    int imageUriColumn = cursor.getColumnIndexOrThrow(COLUMN_IMAGEURI);
                    do {
                        String rank = cursor.getString(rankColumn);
                        String artist = cursor.getString(artistColumn);
                        String album = cursor.getString(albumColumn);
                        int playCount = cursor.getInt(playCountColumn);
                        String imageUri = cursor.getString(imageUriColumn);

                        Album Album = new Album(album, artist, playCount, imageUri, rank);
                        albums.add(Album);
                    }
                    while (cursor.moveToNext() && albums.size() < AppContext.getInstance().getLimit());
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return new TopAlbums(albums, albumsCount);
    }

    public void deleteTopAlbums() throws SQLException {
        deleteTopAlbums(null);
    }

    public void deleteTopAlbums(String pPeriod) throws SQLException {
        deleteRecordsFromTable(DATABASE_TOP_ALBUMS_TABLE, pPeriod);
    }

    public void insertTopArtists(final List<Artist> items, final String pPeriod) throws SQLException {
        final SQLiteDatabase database = getWritableDatabase();

        database.beginTransaction();

        try {
            for (Artist item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_RANK, item.getRank());
                contentValues.put(COLUMN_ARTIST, item.getName());
                contentValues.put(COLUMN_PLAYCOUNT, item.getPlayCount());
                contentValues.put(COLUMN_IMAGEURI, item.getImageUrl());
                contentValues.put(COLUMN_PERIOD, pPeriod);

                database.insertWithOnConflict(DATABASE_TOP_ARTISTS_TABLE, EMPTY_STRING, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public TopArtists getTopArtists(String pPeriod, int pPage) throws SQLException {
        final SQLiteDatabase database = getReadableDatabase();
        List<Artist> artists = new ArrayList<>();
        int artistsCount;
        Cursor cursor = null;

        try {
            cursor = database.query(DATABASE_TOP_ARTISTS_TABLE, null, COLUMN_PERIOD + " = ?", new String[]{pPeriod}, null, null, COLUMN_RANK);
            artistsCount = cursor.getCount();

            if (cursor.moveToFirst()) {
                if (cursor.moveToPosition((pPage - 1) * AppContext.getInstance().getLimit())) {
                    int rankColumn = cursor.getColumnIndexOrThrow(COLUMN_RANK);
                    int artistColumn = cursor.getColumnIndexOrThrow(COLUMN_ARTIST);
                    int playCountColumn = cursor.getColumnIndexOrThrow(COLUMN_PLAYCOUNT);
                    int imageUriColumn = cursor.getColumnIndexOrThrow(COLUMN_IMAGEURI);
                    do {
                        String rank = cursor.getString(rankColumn);
                        String artist = cursor.getString(artistColumn);
                        int playCount = cursor.getInt(playCountColumn);
                        String imageUri = cursor.getString(imageUriColumn);

                        Artist topArtist = new Artist(artist, imageUri, playCount, rank);
                        artists.add(topArtist);
                    }
                    while (cursor.moveToNext() && artists.size() < AppContext.getInstance().getLimit());
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return new TopArtists(artists, artistsCount);
    }

    public void deleteTopArtists() throws SQLException {
        deleteTopArtists(null);
    }

    public void deleteTopArtists(String pPeriod) throws SQLException {
        deleteRecordsFromTable(DATABASE_TOP_ARTISTS_TABLE, pPeriod);
    }

    public void insertTopTracks(final List<Track> items, final String pPeriod) throws SQLException {
        final SQLiteDatabase database = getWritableDatabase();

        database.beginTransaction();

        try {
            for (Track item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_RANK, item.getRank());
                contentValues.put(COLUMN_TRACK, item.getTitle());
                contentValues.put(COLUMN_ARTIST, item.getArtistName());
                contentValues.put(COLUMN_PLAYCOUNT, item.getPlayCount());
                contentValues.put(COLUMN_IMAGEURI, item.getImageUrl());
                contentValues.put(COLUMN_PERIOD, pPeriod);

                database.insertWithOnConflict(DATABASE_TOP_TRACKS_TABLE, EMPTY_STRING, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public TopTracks getTopTracks(String pPeriod, int pPage) throws SQLException {
        final SQLiteDatabase database = getReadableDatabase();
        List<Track> result = new ArrayList<>();
        int tracksCount;
        Cursor cursor = null;

        try {
            cursor = database.query(DATABASE_TOP_TRACKS_TABLE, null, COLUMN_PERIOD + " = ?", new String[]{pPeriod}, null, null, COLUMN_RANK);
            tracksCount = cursor.getCount();

            if (cursor.moveToFirst()) {
                if (cursor.moveToPosition((pPage - 1) * AppContext.getInstance().getLimit())){
                    int rankColumn = cursor.getColumnIndexOrThrow(COLUMN_RANK);
                    int artistColumn = cursor.getColumnIndexOrThrow(COLUMN_ARTIST);
                    int trackTitleColumn = cursor.getColumnIndexOrThrow(COLUMN_TRACK);
                    int playCountColumn = cursor.getColumnIndexOrThrow(COLUMN_PLAYCOUNT);
                    int imageUriColumn = cursor.getColumnIndexOrThrow(COLUMN_IMAGEURI);
                    do {
                        String rank = cursor.getString(rankColumn);
                        String trackTitle = cursor.getString(trackTitleColumn);
                        String artist = cursor.getString(artistColumn);
                        int playCount = cursor.getInt(playCountColumn);
                        String imageUri = cursor.getString(imageUriColumn);

                        Track topTrack = new Track(trackTitle, artist, playCount, imageUri, rank);
                        result.add(topTrack);
                    }
                    while (cursor.moveToNext() && result.size() < AppContext.getInstance().getLimit());
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return new TopTracks(result, tracksCount);
    }

    public void deleteTopTracks() throws SQLException {
        deleteTopTracks(null);
    }

    public void deleteTopTracks(String pPeriod) throws SQLException {
        deleteRecordsFromTable(DATABASE_TOP_TRACKS_TABLE, pPeriod);
    }
}
