package by.d1makrat.library_fm.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.model.TopAlbum;

import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_ALBUM;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_ARTIST;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_IMAGEURI;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_PERIOD;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_PLAYCOUNT;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.COLUMN_RANK;
import static by.d1makrat.library_fm.Constants.DatabaseConstants.DATABASE_TOP_ALBUMS_TABLE;
import static by.d1makrat.library_fm.Constants.EMPTY_STRING;

public class TopAlbumsTableWorker{

    private SQLiteOpenHelper mDatabaseHelper;

    TopAlbumsTableWorker(SQLiteOpenHelper pDatabaseHelper) {
        mDatabaseHelper = pDatabaseHelper;
    }

    public int bulkInsertTopAlbums(final List<TopAlbum> items, final String pPeriod) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        int inserted = 0;

        database.beginTransaction();

        try {
            for (TopAlbum item : items) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_RANK, item.getRank());
                contentValues.put(COLUMN_ARTIST, item.getArtistName());
                contentValues.put(COLUMN_ALBUM, item.getTitle());
                contentValues.put(COLUMN_PLAYCOUNT, item.getPlaycount());
                contentValues.put(COLUMN_IMAGEURI, item.getImageUri());
                contentValues.put(COLUMN_PERIOD, pPeriod);

                database.insertWithOnConflict(DATABASE_TOP_ALBUMS_TABLE, EMPTY_STRING, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

                inserted++;
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }

        return inserted;
    }

    public List<TopAlbum> getTopAlbums(String pPeriod, int pPage) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        List<TopAlbum> result = new ArrayList<>();
        Cursor cursor = null;

        database.beginTransaction();

        try {
            cursor = database.query(DATABASE_TOP_ALBUMS_TABLE, null, COLUMN_PERIOD + " = ?", new String[]{pPeriod}, null, null, COLUMN_RANK);

            if (cursor != null) {
                if (cursor.moveToPosition((pPage - 1) * AppContext.getInstance().getLimit())){
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

                        TopAlbum topAlbum = new TopAlbum();
                        topAlbum.setTitle(album);
                        topAlbum.setArtistName(artist);
                        topAlbum.setRank(rank);
                        topAlbum.setPlaycount(playcount);
                        topAlbum.setImageUri(imageUri);
                        result.add(topAlbum);
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

    public String getAlbumsCount(String pPeriod) throws SQLException {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        int count;
        Cursor cursor = null;

        database.beginTransaction();

        try {
            cursor = database.query(DATABASE_TOP_ALBUMS_TABLE, null, COLUMN_PERIOD + " = ?", new String[]{pPeriod}, null, null, COLUMN_RANK);

            count = cursor.getCount();

            database.setTransactionSuccessful();
        } finally {
            if (cursor != null)
                cursor.close();
            database.endTransaction();
            database.close();
        }

        return String.valueOf(count);
    }
}