package by.d1makrat.library_fm.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.model.Scrobble;

import static by.d1makrat.library_fm.Constants.DATE_LONG_DEFAUT_VALUE;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_ALBUM;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_ARTIST;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_DATE;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_IMAGEURI;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_TRACK;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.DATABASE_SCROBBLES_TABLE;
import static by.d1makrat.library_fm.Constants.EMPTY_STRING;

public class ScrobblesTableWorker {

    private static final String SORTING_DATE_DESCENDING = COLUMN_DATE + " DESC";
    private static final String DATE_INTERVAL_CONDITION = COLUMN_DATE + " BETWEEN ? AND ?";
    private static final String ARTIST_EQUALS_CONDITION = COLUMN_ARTIST + " = ?";
    private static final String ARTIST_EQUALS_AND_DATE_INTERVAL_CONDITION = ARTIST_EQUALS_CONDITION + " AND " + DATE_INTERVAL_CONDITION;
    private static final String ARTIST_AND_TRACK_EQUALS_CONDITION = ARTIST_EQUALS_CONDITION + " AND " + COLUMN_TRACK + " = ?";
    private static final String ARTIST_AND_TRACK_EQUALS_AND_DATE_INTERVAL_CONDITION = ARTIST_AND_TRACK_EQUALS_CONDITION + " AND " + DATE_INTERVAL_CONDITION;
    private static final String ARTIST_AND_ALBUM_EQUALS_CONDITION = ARTIST_EQUALS_CONDITION + " AND " + COLUMN_ALBUM + " = ?";
    private static final String ARTIST_AND_ALBUM_EQUALS_AND_DATE_INTERVAL_CONDITION = ARTIST_AND_ALBUM_EQUALS_CONDITION + " AND " +DATE_INTERVAL_CONDITION;

    private final SQLiteOpenHelper mDatabaseHelper;

    ScrobblesTableWorker(SQLiteOpenHelper pDatabaseHelper) {
        mDatabaseHelper = pDatabaseHelper;
    }

    public void bulkInsertScrobbles(final List<Scrobble> items) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        database.beginTransaction();

        try {
            for (Scrobble item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_TRACK, item.getTrackTitle());
                contentValues.put(COLUMN_ARTIST, item.getArtist());
                contentValues.put(COLUMN_ALBUM, item.getAlbum());
                contentValues.put(COLUMN_DATE, item.getRawDate());
                contentValues.put(COLUMN_IMAGEURI, item.getImageUri());

                database.insertWithOnConflict(DATABASE_SCROBBLES_TABLE, EMPTY_STRING, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }

    }

    @Nullable
    public List<Scrobble> getScrobbles(int pPage, Long pFrom, Long pTo) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<Scrobble> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            if (pFrom.equals(DATE_LONG_DEFAUT_VALUE) && pTo.equals(DATE_LONG_DEFAUT_VALUE))
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
            database.close();
        }

        return result;
    }

    public List<Scrobble> getScrobblesOfArtist(String pArtist, int pPage, Long pFrom, Long pTo) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<Scrobble> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            if (pFrom.equals(DATE_LONG_DEFAUT_VALUE) && pTo.equals(DATE_LONG_DEFAUT_VALUE))
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
            database.close();
        }

        return result;
    }

    public List<Scrobble> getScrobblesOfTrack(String pArtist, String pTrack, Long pFrom, Long pTo) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<Scrobble> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            if (pFrom.equals(DATE_LONG_DEFAUT_VALUE) && pTo.equals(DATE_LONG_DEFAUT_VALUE))
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
            database.close();
        }

        return result;
    }

    public List<Scrobble> getScrobblesOfAlbum(String pArtist, String pAlbum, Long pFrom, Long pTo) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<Scrobble> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            if (pFrom.equals(DATE_LONG_DEFAUT_VALUE) && pTo.equals(DATE_LONG_DEFAUT_VALUE))
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
            database.close();
        }

        return result;
    }
}
