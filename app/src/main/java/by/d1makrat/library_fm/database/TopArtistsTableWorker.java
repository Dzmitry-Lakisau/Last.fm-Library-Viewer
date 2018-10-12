package by.d1makrat.library_fm.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.model.Artist;
import by.d1makrat.library_fm.model.TopArtists;

import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_ARTIST;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_IMAGEURI;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_PERIOD;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_PLAYCOUNT;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_RANK;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.DATABASE_TOP_ARTISTS_TABLE;
import static by.d1makrat.library_fm.Constants.EMPTY_STRING;

public class TopArtistsTableWorker {

    private final SQLiteOpenHelper mDatabaseHelper;

    TopArtistsTableWorker(SQLiteOpenHelper pDatabaseHelper) {
        mDatabaseHelper = pDatabaseHelper;
    }

    public void insertTopArtists(final List<Artist> items, final String pPeriod) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

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
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
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
                        String playCount = cursor.getString(playCountColumn);
                        String imageUri = cursor.getString(imageUriColumn);

                        Artist topArtist = new Artist(artist, null, playCount, "", imageUri, rank);//TODO remove url
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
}
