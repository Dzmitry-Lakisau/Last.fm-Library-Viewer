package by.d1makrat.library_fm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.model.Scrobble;

public class DatabaseWorker {

    private static final String DATABASE_NAME = "Last.fm Library Viewer.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_SCROBBLES_TABLE = "Scrobbles";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TRACK = "track";
    private static final String COLUMN_ARTIST = "artist";
    private static final String COLUMN_ALBUM = "album";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_IMAGEURI = "imageUri";

    private static final String TABLE_SCROBBLES_CREATE_QUERY =
            "create table if not exists " + DATABASE_SCROBBLES_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_TRACK + " text, " +
                    COLUMN_ARTIST + " text, " +
                    COLUMN_ALBUM + " text, " +
                    COLUMN_DATE + " int unsigned, " +
                    COLUMN_IMAGEURI + " text, " +
                    "CONSTRAINT uniqueScrobble UNIQUE (" + COLUMN_TRACK + ", " +  COLUMN_ARTIST + ", " + COLUMN_DATE +
                    "));";

    private final Context mCtx;

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqliteDatabase;

    public DatabaseWorker(Context ctx) {
        mCtx = ctx;
    }

    public void open() {
        mDatabaseHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mSqliteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        if (mDatabaseHelper !=null) mDatabaseHelper.close();
    }

    @Nullable
    public List<Scrobble> getScrobbles(String pPage, String pFrom, String pTo) {
        List<Scrobble> result = new ArrayList<>();
        Cursor cursor = null;

        if (pFrom != null && pTo != null)
                cursor = mSqliteDatabase.query(DATABASE_SCROBBLES_TABLE, null, "date > ? AND date < ?", new String[]{pFrom, pTo}, null, null, "date DESC");
            else
                cursor = mSqliteDatabase.query(DATABASE_SCROBBLES_TABLE, null, null, null, null,null, null);

        if (cursor != null) {
            if (cursor.moveToPosition((Integer.parseInt(pPage) - 1) * AppContext.getInstance().getLimit())) {
                do {
                    String trackTitle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRACK));
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST));
                    String album = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALBUM));
                    long unixDate = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                    String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGEURI));

                    Scrobble scrobble = new Scrobble();
                    scrobble.setTrackTitle(trackTitle);
                    scrobble.setArtist(artist);
                    scrobble.setAlbum(album);
                    scrobble.setDate(unixDate);
                    scrobble.setImageUri(imageUri);
                    result.add(scrobble);
                } while (cursor.moveToNext() && result.size() < AppContext.getInstance().getLimit());
            }
            cursor.close();
        }

        return result;
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

    private class DatabaseHelper extends SQLiteOpenHelper {

        private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_SCROBBLES_CREATE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}