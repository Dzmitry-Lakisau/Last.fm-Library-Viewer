package by.d1makrat.library_fm.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import by.d1makrat.library_fm.AppContext;

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

public class DatabaseWorker {

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

    private DatabaseHelper mDatabaseHelper;

    public DatabaseWorker(){
        mDatabaseHelper = new DatabaseHelper(AppContext.getInstance().getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    void createTables(SQLiteDatabase database) throws SQLException {
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

    private void deleteRecordsFromTable(String pTableName, @Nullable String pPeriod) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

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

    public void deleteScrobbles() throws SQLException{
        deleteRecordsFromTable(DATABASE_SCROBBLES_TABLE, null);
    }

    public void deleteTopAlbums(String pPeriod) throws SQLException{
        deleteRecordsFromTable(DATABASE_TOP_ALBUMS_TABLE, pPeriod);
    }

    public void deleteTopArtists(String pPeriod) throws SQLException{
        deleteRecordsFromTable(DATABASE_TOP_ARTISTS_TABLE, pPeriod);
    }

    public void deleteTopTracks(String pPeriod) throws SQLException{
        deleteRecordsFromTable(DATABASE_TOP_TRACKS_TABLE, pPeriod);
    }

    public ScrobblesTableWorker getScrobblesTable() {
        return new ScrobblesTableWorker(mDatabaseHelper);
    }

    public TopAlbumsTableWorker getTopAlbumsTable(){
        return new TopAlbumsTableWorker(mDatabaseHelper);
    }

    public TopArtistsTableWorker getTopArtistsTable(){
        return new TopArtistsTableWorker(mDatabaseHelper);
    }

    public TopTracksTableWorker getTopTracksTable(){
        return new TopTracksTableWorker(mDatabaseHelper);
    }

    void upgradeScrobblesTable(SQLiteDatabase database) throws SQLException {
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
}
