package by.d1makrat.library_fm.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.model.Track;

import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_ARTIST;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_IMAGEURI;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_PERIOD;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_PLAYCOUNT;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_RANK;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_TRACK;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.DATABASE_TOP_TRACKS_TABLE;
import static by.d1makrat.library_fm.Constants.EMPTY_STRING;

public class TopTracksTableWorker {

    private final SQLiteOpenHelper mDatabaseHelper;

    TopTracksTableWorker(SQLiteOpenHelper pDatabaseHelper) {
        mDatabaseHelper = pDatabaseHelper;
    }

    public void bulkInsertTopTracks(final List<Track> items, final String pPeriod) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        database.beginTransaction();

        try {
            for (Track item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_RANK, item.getRank());
                contentValues.put(COLUMN_TRACK, item.getTitle());
                contentValues.put(COLUMN_ARTIST, item.getArtistName());
                contentValues.put(COLUMN_PLAYCOUNT, item.getPlaycount());
                contentValues.put(COLUMN_IMAGEURI, item.getImageUrl());
                contentValues.put(COLUMN_PERIOD, pPeriod);

                database.insertWithOnConflict(DATABASE_TOP_TRACKS_TABLE, EMPTY_STRING, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }

    }

    public List<Track> getTopTracks(String pPeriod, int pPage) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<Track> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            cursor = database.query(DATABASE_TOP_TRACKS_TABLE, null, COLUMN_PERIOD + " = ?", new String[]{pPeriod}, null, null, COLUMN_RANK);

            if (cursor != null) {
                if (cursor.moveToPosition((pPage - 1) * AppContext.getInstance().getLimit())){
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

                        Track topTrack = new Track(trackTitle, artist, playcount, imageUri, rank);
                        result.add(topTrack);
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

    public int getTracksCount(String pPeriod) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        int count;
        Cursor cursor = null;

        database.beginTransaction();

        try {
            cursor = database.query(DATABASE_TOP_TRACKS_TABLE, null, COLUMN_PERIOD + " = ?", new String[]{pPeriod}, null, null, COLUMN_RANK);

            count = cursor.getCount();

            database.setTransactionSuccessful();
        } finally {
            if (cursor != null)
                cursor.close();
            database.endTransaction();
            database.close();
        }

        return count;
    }
}
