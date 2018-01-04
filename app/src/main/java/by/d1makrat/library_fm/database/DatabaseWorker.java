package by.d1makrat.library_fm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.model.RankedItem;
import by.d1makrat.library_fm.model.Scrobble;

public class DatabaseWorker {

    private static final String DATABASE_NAME = "Last.fm Library Viewer.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_SCROBBLES_TABLE = "Scrobbles";
    private static final String DATABASE_TOP_ALBUMS_TABLE = "TopAlbums";
    private static final String DATABASE_TOP_ARTISTS_TABLE = "TopArtists";
    private static final String DATABASE_TOP_TRACKS_TABLE = "TopTracks";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TRACK = "track";
    private static final String COLUMN_ARTIST = "artist";
    private static final String COLUMN_ALBUM = "album";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_IMAGEURI = "imageUri";
    private static final String COLUMN_RANK = "rank";
    private static final String COLUMN_PLAYCOUNT = "playcount";
    private static final String COLUMN_PERIOD = "period";

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
    private static final String SORTING_DATE_DESCENDING = COLUMN_DATE + " DESC";
    private static final String DATE_INTERVAL_CONDITION = COLUMN_DATE + " BETWEEN ? AND ?";
    private static final String ARTIST_EQUALS_CONDITION = COLUMN_ARTIST + " = ?";
    private static final String ARTIST_EQUALS_AND_DATE_INTERVAL_CONDITION = ARTIST_EQUALS_CONDITION + " AND " + DATE_INTERVAL_CONDITION;
    private static final String ARTIST_AND_TRACK_EQUALS_CONDITION = ARTIST_EQUALS_CONDITION + " AND " + COLUMN_TRACK + " = ?";
    private static final String ARTIST_AND_TRACK_EQUALS_AND_DATE_INTERVAL_CONDITION = ARTIST_AND_TRACK_EQUALS_CONDITION + " AND " + DATE_INTERVAL_CONDITION;
    private static final String ARTIST_AND_ALBUM_EQUALS_CONDITION = ARTIST_EQUALS_CONDITION + " AND " + COLUMN_ALBUM + " = ?";
    private static final String ARTIST_AND_ALBUM_EQUALS_AND_DATE_INTERVAL_CONDITION = ARTIST_AND_ALBUM_EQUALS_CONDITION + " AND " +DATE_INTERVAL_CONDITION;

    private final Context mCtx;

    private DatabaseHelper mDatabaseHelper;

    public DatabaseWorker(Context ctx) {
        mCtx = ctx;
    }

    public void openDatabase() {
        mDatabaseHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void closeDatabase() {
        if (mDatabaseHelper !=null) mDatabaseHelper.close();
    }

    public int bulkInsertScrobbles(final List<Scrobble> items) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        int inserted = 0;

        database.beginTransaction();

        try {
            for (Scrobble item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_TRACK, item.getTrackTitle());
                contentValues.put(COLUMN_ARTIST, item.getArtist());
                contentValues.put(COLUMN_ALBUM, item.getAlbum());
                contentValues.put(COLUMN_DATE, item.getDate());
                contentValues.put(COLUMN_IMAGEURI, item.getImageUri());

                database.insertWithOnConflict(DATABASE_SCROBBLES_TABLE, "", contentValues, SQLiteDatabase.CONFLICT_IGNORE);

                inserted++;
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        return inserted;
    }

    @Nullable
    public List<Scrobble> getScrobbles(String pPage, String pFrom, String pTo) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<Scrobble> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            if (pFrom != null && pTo != null)
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, DATE_INTERVAL_CONDITION, new String[]{pFrom, pTo}, null, null, SORTING_DATE_DESCENDING);
            else
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, null, null, null, null, SORTING_DATE_DESCENDING);

            if (cursor != null) {
                if (cursor.moveToPosition((Integer.valueOf(pPage) - 1) * AppContext.getInstance().getLimit())){
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

                        Scrobble scrobble = new Scrobble();
                        scrobble.setTrackTitle(trackTitle);
                        scrobble.setArtist(artist);
                        scrobble.setAlbum(album);
                        scrobble.setDate(unixDate);
                        scrobble.setImageUri(imageUri);
                        result.add(scrobble);
                    }
                    while (cursor.moveToNext() && result.size() < AppContext.getInstance().getLimit());
                }
            }

            database.setTransactionSuccessful();
        } finally {
            if (cursor != null)
                cursor.close();
            database.endTransaction();
        }

        return result;
    }

    public List<Scrobble> getScrobblesOfArtist(String pArtist, String pPage, String pFrom, String pTo) {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<Scrobble> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            if (pFrom != null && pTo != null)
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_EQUALS_AND_DATE_INTERVAL_CONDITION, new String[]{pArtist, pFrom, pTo}, null, null, SORTING_DATE_DESCENDING);
            else
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_EQUALS_CONDITION, new String[]{pArtist}, null, null, SORTING_DATE_DESCENDING);

            if (cursor != null) {
                if (cursor.moveToPosition((Integer.valueOf(pPage) - 1) * AppContext.getInstance().getLimit())){
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

                        Scrobble scrobble = new Scrobble();
                        scrobble.setTrackTitle(trackTitle);
                        scrobble.setArtist(artist);
                        scrobble.setAlbum(album);
                        scrobble.setDate(unixDate);
                        scrobble.setImageUri(imageUri);
                        result.add(scrobble);
                    }
                    while (cursor.moveToNext() && result.size() < AppContext.getInstance().getLimit());
                }
            }

            database.setTransactionSuccessful();
        } finally {
            if (cursor != null)
                cursor.close();
            database.endTransaction();
        }

        return result;
    }

    public List<Scrobble> getScrobblesOfTrack(String pArtist, String pTrack, String pFrom, String pTo) {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<Scrobble> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            if (pFrom != null && pTo != null)
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_AND_TRACK_EQUALS_AND_DATE_INTERVAL_CONDITION, new String[]{pArtist, pTrack, pFrom, pTo}, null, null, SORTING_DATE_DESCENDING);
            else
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_AND_TRACK_EQUALS_CONDITION, new String[]{pArtist, pTrack}, null, null, SORTING_DATE_DESCENDING);

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

                        Scrobble scrobble = new Scrobble();
                        scrobble.setTrackTitle(trackTitle);
                        scrobble.setArtist(artist);
                        scrobble.setAlbum(album);
                        scrobble.setDate(unixDate);
                        scrobble.setImageUri(imageUri);
                        result.add(scrobble);
                    }
                    while (cursor.moveToNext());
                }
            }

            database.setTransactionSuccessful();
        } finally {
            if (cursor != null)
                cursor.close();
            database.endTransaction();
        }

        return result;
    }

    public List<Scrobble> getScrobblesOfAlbum(String pArtist, String pAlbum, String pFrom, String pTo) {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<Scrobble> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            if (pFrom != null && pTo != null)
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_AND_ALBUM_EQUALS_AND_DATE_INTERVAL_CONDITION, new String[]{pArtist, pAlbum, pFrom, pTo}, null, null, SORTING_DATE_DESCENDING);
            else
                cursor = database.query(DATABASE_SCROBBLES_TABLE, null, ARTIST_AND_ALBUM_EQUALS_CONDITION, new String[]{pArtist, pAlbum}, null, null, SORTING_DATE_DESCENDING);

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

                        Scrobble scrobble = new Scrobble();
                        scrobble.setTrackTitle(trackTitle);
                        scrobble.setArtist(artist);
                        scrobble.setAlbum(album);
                        scrobble.setDate(unixDate);
                        scrobble.setImageUri(imageUri);
                        result.add(scrobble);
                    }
                    while (cursor.moveToNext());
                }
            }

            database.setTransactionSuccessful();
        } finally {
            if (cursor != null)
                cursor.close();
            database.endTransaction();
        }

        return result;
    }

    public int bulkInsertTopAlbums(final List<RankedItem> items, final String pPeriod) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        int inserted = 0;

        database.beginTransaction();

        try {
            for (RankedItem item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_RANK, item.getRank());
                contentValues.put(COLUMN_ARTIST, item.getSecondaryField());
                contentValues.put(COLUMN_ALBUM, item.getPrimaryField());
                contentValues.put(COLUMN_PLAYCOUNT, item.getPlaycount());
                contentValues.put(COLUMN_IMAGEURI, item.getImageUri());
                contentValues.put(COLUMN_PERIOD, pPeriod);

                database.insertWithOnConflict(DATABASE_TOP_ALBUMS_TABLE, "", contentValues, SQLiteDatabase.CONFLICT_IGNORE);

                inserted++;
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        return inserted;
    }

    public List<RankedItem> getTopAlbums(String pPeriod, String pPage) {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<RankedItem> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            cursor = database.query(DATABASE_TOP_ALBUMS_TABLE, null, COLUMN_PERIOD + " = ?", new String[]{pPeriod}, null, null, COLUMN_RANK);

            if (cursor != null) {
                if (cursor.moveToPosition((Integer.valueOf(pPage) - 1) * AppContext.getInstance().getLimit())){
                    int rankColumn = cursor.getColumnIndexOrThrow(COLUMN_RANK);
                    int artistColumn = cursor.getColumnIndexOrThrow(COLUMN_ARTIST);
                    int albumColumn = cursor.getColumnIndexOrThrow(COLUMN_ALBUM);
                    int playcountColumn = cursor.getColumnIndexOrThrow(COLUMN_PLAYCOUNT);
                    int imageUriColumn = cursor.getColumnIndexOrThrow(COLUMN_IMAGEURI);
                    do {
                        String rank = cursor.getString(rankColumn);
                        String artist = cursor.getString(artistColumn);
                        String album = cursor.getString(albumColumn);
                        String playcount = cursor.getString(playcountColumn);
                        String imageUri = cursor.getString(imageUriColumn);

                        RankedItem rankedItem = new RankedItem();
                        rankedItem.setPrimaryField(album);
                        rankedItem.setSecondaryField(artist);
                        rankedItem.setRank(rank);
                        rankedItem.setPlaycount(playcount);
                        rankedItem.setImageUri(imageUri);
                        result.add(rankedItem);
                    }
                    while (cursor.moveToNext() && result.size() < AppContext.getInstance().getLimit());
                }
            }

            database.setTransactionSuccessful();
        } finally {
            if (cursor != null)
                cursor.close();
            database.endTransaction();
        }

        return result;
    }
    //TODO throws SQLexception , save placeholder of images when loading from database
    public void deleteTopAlbums(String pPeriod){
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        database.beginTransaction();

        try {
            database.delete(DATABASE_TOP_ALBUMS_TABLE, COLUMN_PERIOD + " = ?", new String[]{pPeriod});

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    public int bulkInsertTopArtists(final List<RankedItem> items, final String pPeriod) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        int inserted = 0;

        database.beginTransaction();

        try {
            for (RankedItem item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_RANK, item.getRank());
                contentValues.put(COLUMN_ARTIST, item.getPrimaryField());
                contentValues.put(COLUMN_PLAYCOUNT, item.getPlaycount());
                contentValues.put(COLUMN_IMAGEURI, item.getImageUri());
                contentValues.put(COLUMN_PERIOD, pPeriod);

                database.insertWithOnConflict(DATABASE_TOP_ARTISTS_TABLE, "", contentValues, SQLiteDatabase.CONFLICT_IGNORE);

                inserted++;
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        return inserted;
    }

    public List<RankedItem> getTopArtists(String pPeriod, String pPage) {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<RankedItem> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            cursor = database.query(DATABASE_TOP_ARTISTS_TABLE, null, COLUMN_PERIOD + " = ?", new String[]{pPeriod}, null, null, COLUMN_RANK);

            if (cursor != null) {
                if (cursor.moveToPosition((Integer.valueOf(pPage) - 1) * AppContext.getInstance().getLimit())){
                    int rankColumn = cursor.getColumnIndexOrThrow(COLUMN_RANK);
                    int artistColumn = cursor.getColumnIndexOrThrow(COLUMN_ARTIST);
                    int playcountColumn = cursor.getColumnIndexOrThrow(COLUMN_PLAYCOUNT);
                    int imageUriColumn = cursor.getColumnIndexOrThrow(COLUMN_IMAGEURI);
                    do {
                        String rank = cursor.getString(rankColumn);
                        String artist = cursor.getString(artistColumn);
                        String playcount = cursor.getString(playcountColumn);
                        String imageUri = cursor.getString(imageUriColumn);

                        RankedItem rankedItem = new RankedItem();
                        rankedItem.setPrimaryField(artist);
                        rankedItem.setRank(rank);
                        rankedItem.setPlaycount(playcount);
                        rankedItem.setImageUri(imageUri);
                        result.add(rankedItem);
                    }
                    while (cursor.moveToNext() && result.size() < AppContext.getInstance().getLimit());
                }
            }

            database.setTransactionSuccessful();
        } finally {
            if (cursor != null)
                cursor.close();
            database.endTransaction();
        }

        return result;
    }

    public void deleteTopArtists(String pPeriod){
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        database.beginTransaction();

        try {
            database.delete(DATABASE_TOP_ARTISTS_TABLE, COLUMN_PERIOD + " = ?", new String[]{pPeriod});

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    public int bulkInsertTopTracks(final List<RankedItem> items, final String pPeriod) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        int inserted = 0;

        database.beginTransaction();

        try {
            for (RankedItem item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_RANK, item.getRank());
                contentValues.put(COLUMN_TRACK, item.getPrimaryField());
                contentValues.put(COLUMN_ARTIST, item.getSecondaryField());
                contentValues.put(COLUMN_PLAYCOUNT, item.getPlaycount());
                contentValues.put(COLUMN_IMAGEURI, item.getImageUri());
                contentValues.put(COLUMN_PERIOD, pPeriod);

                database.insertWithOnConflict(DATABASE_TOP_TRACKS_TABLE, "", contentValues, SQLiteDatabase.CONFLICT_IGNORE);

                inserted++;
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        return inserted;
    }

    public List<RankedItem> getTopTracks(String pPeriod, String pPage) {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<RankedItem> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            cursor = database.query(DATABASE_TOP_TRACKS_TABLE, null, COLUMN_PERIOD + " = ?", new String[]{pPeriod}, null, null, COLUMN_RANK);

            if (cursor != null) {
                if (cursor.moveToPosition((Integer.valueOf(pPage) - 1) * AppContext.getInstance().getLimit())){
                    int rankColumn = cursor.getColumnIndexOrThrow(COLUMN_RANK);
                    int artistColumn = cursor.getColumnIndexOrThrow(COLUMN_ARTIST);
                    int trackTitleColumn = cursor.getColumnIndexOrThrow(COLUMN_TRACK);
                    int playcountColumn = cursor.getColumnIndexOrThrow(COLUMN_PLAYCOUNT);
                    int imageUriColumn = cursor.getColumnIndexOrThrow(COLUMN_IMAGEURI);
                    do {
                        String rank = cursor.getString(rankColumn);
                        String trackTitle = cursor.getString(trackTitleColumn);
                        String artist = cursor.getString(artistColumn);
                        String playcount = cursor.getString(playcountColumn);
                        String imageUri = cursor.getString(imageUriColumn);

                        RankedItem rankedItem = new RankedItem();
                        rankedItem.setPrimaryField(trackTitle);
                        rankedItem.setSecondaryField(artist);
                        rankedItem.setRank(rank);
                        rankedItem.setPlaycount(playcount);
                        rankedItem.setImageUri(imageUri);
                        result.add(rankedItem);
                    }
                    while (cursor.moveToNext() && result.size() < AppContext.getInstance().getLimit());
                }
            }

            database.setTransactionSuccessful();
        } finally {
            if (cursor != null)
                cursor.close();
            database.endTransaction();
        }

        return result;
    }

    public void deleteTopTracks(String pPeriod){
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        database.beginTransaction();

        try {
            database.delete(DATABASE_TOP_TRACKS_TABLE, COLUMN_PERIOD + " = ?", new String[]{pPeriod});

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_SCROBBLES_TABLE_QUERY);
            db.execSQL(CREATE_TOP_TRACKS_TABLE_QUERY);
            db.execSQL(CREATE_TOP_ARTISTS_TABLE_QUERY);
            db.execSQL(CREATE_TOP_ALBUMS_TABLE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}