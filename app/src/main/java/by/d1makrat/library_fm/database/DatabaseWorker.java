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
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_SCROBBLES_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_SCROBBLES_TABLE + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TRACK + " TEXT, " +
                    COLUMN_ARTIST + " TEXT, " +
                    COLUMN_ALBUM + " TEXT, " +
                    COLUMN_DATE + " INT UNSIGNED, " +
                    COLUMN_IMAGEURI + " TEXT, " +
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

    public void openDatabase() {
        mDatabaseHelper = new DatabaseHelper(AppContext.getInstance().getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void closeDatabase() {
        if (mDatabaseHelper != null) mDatabaseHelper.close();
    }

    void createTables(SQLiteDatabase database) throws SQLException{
        database.execSQL(DatabaseWorker.CREATE_SCROBBLES_TABLE_QUERY);
        database.execSQL(DatabaseWorker.CREATE_TOP_TRACKS_TABLE_QUERY);
        database.execSQL(DatabaseWorker.CREATE_TOP_ARTISTS_TABLE_QUERY);
        database.execSQL(DatabaseWorker.CREATE_TOP_ALBUMS_TABLE_QUERY);
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
            database.close();
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

    public TopAlbumsTableWorker getTopAlbumsTable(){
        return new TopAlbumsTableWorker(mDatabaseHelper);
    }

    public TopArtistsTableWorker getTopArtistsTable(){
        return new TopArtistsTableWorker(mDatabaseHelper);
    }

    public TopTracksTableWorker getTopTracksTable(){
        return new TopTracksTableWorker(mDatabaseHelper);
    }
}
